package com.linku.im.screen.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.linku.core.util.LinkedNode
import com.linku.core.util.forward
import com.linku.core.util.remain
import com.linku.data.usecase.ApplicationUseCases
import com.linku.data.usecase.ConversationUseCases
import com.linku.data.usecase.MessageUseCases
import com.linku.domain.bean.ui.ConversationUI
import com.linku.domain.bean.ui.toContactUI
import com.linku.domain.bean.ui.toUI
import com.linku.domain.entity.ContactRequest
import com.linku.domain.entity.Conversation
import com.linku.domain.entity.GraphicsMessage
import com.linku.domain.entity.ImageMessage
import com.linku.domain.entity.Message
import com.linku.domain.entity.TextMessage
import com.linku.im.R
import com.linku.im.screen.BaseViewModel
import com.linku.im.vm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val conversationUseCases: ConversationUseCases,
    private val applicationUseCases: ApplicationUseCases,
    private val messageUseCases: MessageUseCases
) : BaseViewModel<MainState, MainEvent>(MainState()) {

    private val _linkedNode = mutableStateOf<LinkedNode<MainMode>>(
        LinkedNode(MainMode.Conversations)
    )
    val linkedNode: State<LinkedNode<MainMode>> get() = _linkedNode

    override fun onEvent(event: MainEvent) {
        when (event) {
            MainEvent.ObserveConversations -> observeConversations()
            MainEvent.UnsubscribeConversations -> unsubscribe()
            is MainEvent.Pin -> pin(event)
            is MainEvent.Forward -> forward(event)
            MainEvent.Remain -> remain()
            MainEvent.FetchNotifications -> fetchNotifications()
        }
    }

    private fun fetchNotifications() {
        viewModelScope.launch {
            val requests =
                messageUseCases.findMessagesByType<ContactRequest>(Message.Type.ContactRequest)
            writable = readable.copy(
                requests = requests
            )
        }
    }

    private fun remain() {
        _linkedNode.value = linkedNode.value.remain()
    }

    private fun forward(event: MainEvent.Forward) {
        _linkedNode.value = linkedNode.value.forward(event.mode)
    }

    private var observeConversationsJob: Job? = null
    private fun observeConversations() {
        conversationUseCases.observeConversations()
            .onEach { conversations ->
                writable = readable.copy(
                    conversations = conversations
                        .filter { it.type == Conversation.Type.GROUP }
                        .map(Conversation::toUI)
                        .sortedByDescending { it.updatedAt },
                    contracts = conversations
                        .filter { it.type == Conversation.Type.PM }
//                            .map {
//                                val uid = conversationUseCases.convertConversationToContact(it.id)
//                                it.toContactUI(
//                                    username = uid?.let { notnull ->
//                                        users.findUser(
//                                            notnull,
//                                            Strategy.OnlyCache
//                                        )?.name.orEmpty()
//                                    }.orEmpty()
//                                )
//                            }
                        .map(Conversation::toContactUI)
                        .sortedByDescending { it.updatedAt }
                )
                observeConversationsJob?.cancel()
                observeConversationsJob = viewModelScope.launch {
                    conversations.forEach { conversation ->
                        messageUseCases.observeLatestMessage(conversation.id)
                            .onEach { message ->
                                val oldConversations = readable.conversations.toMutableList()
                                val oldContracts = readable.contracts.toMutableList()
                                val oldConversation: ConversationUI? =
                                    oldConversations.find { it.id == message.cid }
                                val oldContract = oldContracts.find { it.id == message.cid }
                                if (oldConversation != null) {
                                    oldConversations.remove(oldConversation)
                                    val copy = oldConversation.copy(
                                        content = when (message) {
                                            is TextMessage -> message.text
                                            is ImageMessage -> applicationUseCases.getString(R.string.image_message)
                                            is GraphicsMessage -> applicationUseCases.getString(R.string.graphics_message)
                                            else -> applicationUseCases.getString(R.string.unknown_message_type)
                                        }
                                    )
                                    oldConversations.add(copy)
                                } else if (oldContract != null) {
                                    oldContracts.remove(oldContract)
                                    val copy = oldContract.copy(
                                        content = when (message) {
                                            is TextMessage -> message.text
                                            is ImageMessage -> applicationUseCases.getString(R.string.image_message)
                                            is GraphicsMessage -> applicationUseCases.getString(R.string.graphics_message)
                                            else -> applicationUseCases.getString(R.string.unknown_message_type)
                                        }
                                    )
                                    oldContracts.add(copy)
                                } else {
                                    // TODO
                                }
                                writable = readable.copy(
                                    conversations = oldConversations,
                                    contracts = oldContracts
                                )
                            }
                            .launchIn(this)
                    }
                }
            }
            .launchIn(viewModelScope)
    }


    private fun unsubscribe() {
        observeConversationsJob?.cancel()
    }

    private fun pin(event: MainEvent.Pin) {
        viewModelScope.launch {
            conversationUseCases.pin(event.cid)
        }
    }
}

inline fun globalLabelOrElse(block: () -> String): String {
    return vm.readable.label ?: block()
}