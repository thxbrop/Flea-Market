package com.linku.data.usecase

import android.net.Uri
import com.linku.domain.Resource
import com.linku.domain.Strategy
import com.linku.domain.entity.Message
import com.linku.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class MessageUseCases @Inject constructor(
    val observeAllMessages: ObserveAllMessagesUseCase,
    val observeMessages: ObserveMessagesUseCase,
    val observeLatestMessage: ObserveLatestMessagesUseCase,
    val textMessage: TextMessageUseCase,
    val imageMessage: ImageMessageUseCase,
    val graphicsMessage: GraphicsMessageUseCase,
    val getMessage: GetMessageUseCase,
    val fetchUnreadMessages: FetchUnreadMessagesUseCase,
    val fetchMessagesAtLeastUseCase: FetchMessagesAtLeastUseCase,
)

data class FetchUnreadMessagesUseCase @Inject constructor(
    val repository: MessageRepository
) {
    suspend operator fun invoke() = repository.fetchUnreadMessages()
}

data class ObserveLatestMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke(cid: Int): Flow<Message> = repository.observeLatestMessages(cid)
}

data class FetchMessagesAtLeastUseCase @Inject constructor(
    val repository: MessageRepository
) {
    suspend operator fun invoke(
        after: Long
    ) = repository.fetchMessagesAtLeast(after)
}


data class GetMessageUseCase @Inject constructor(
    val repository: MessageRepository
) {
    suspend operator fun invoke(
        mid: Int,
        strategy: Strategy
    ): Message? = repository.getMessageById(mid, strategy)
}

data class TextMessageUseCase @Inject constructor(
    val repository: MessageRepository
) {
    suspend operator fun invoke(
        cid: Int,
        text: String,
        reply: Int? = null
    ): Flow<Resource<Unit>> = repository.sendTextMessage(cid, text, reply)
}

data class ImageMessageUseCase @Inject constructor(
    val messageRepository: MessageRepository
) {
    operator fun invoke(
        cid: Int,
        uri: Uri,
        reply: Int? = null
    ): Flow<Resource<Unit>> = messageRepository.sendImageMessage(cid, uri, reply)
}

data class GraphicsMessageUseCase @Inject constructor(
    val messageRepository: MessageRepository
) {
    operator fun invoke(
        cid: Int,
        text: String,
        uri: Uri,
        reply: Int? = null
    ): Flow<Resource<Unit>> = messageRepository.sendGraphicsMessage(cid, text, uri, reply)
}

data class ObserveAllMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke(): Flow<List<Message>> {
        return repository.incoming()
    }
}

data class ObserveMessagesUseCase @Inject constructor(
    private val repository: MessageRepository
) {
    operator fun invoke(cid: Int): Flow<List<Message>> {
        return repository.incoming(cid)
    }
}
