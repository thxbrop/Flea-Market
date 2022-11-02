package com.linku.im.screen.query

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumble.appyx.navmodel.backstack.operation.pop
import com.bumble.appyx.navmodel.backstack.operation.push
import com.linku.im.R
import com.linku.im.appyx.target.NavTarget
import com.linku.im.ui.components.TextField
import com.linku.im.ui.components.button.MaterialIconButton
import com.linku.im.ui.components.item.ConversationItem
import com.linku.im.ui.components.item.UserItem
import com.linku.im.ui.components.notify.NotifyHolder
import com.linku.im.ui.theme.LocalBackStack
import com.linku.im.ui.theme.LocalSpacing
import com.linku.im.ui.theme.LocalTheme
import com.linku.im.vm

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun QueryScreen(
    viewModel: QueryViewModel = hiltViewModel()
) {
    val state = viewModel.readable
    val scaffoldState = rememberScaffoldState()
    val backStack = LocalBackStack.current

    LaunchedEffect(viewModel.message, vm.message) {
        viewModel.message.handle {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
        vm.message.handle {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }
    Scaffold(
        snackbarHost = {
            NotifyHolder(
                state = it,
                modifier = Modifier.fillMaxWidth()
            )
        },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        CompositionLocalProvider(
            LocalContentColor provides if (vm.readable.isDarkMode) LocalTheme.current.onSurface else LocalTheme.current.onPrimary
        ) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(if (vm.readable.isDarkMode) LocalTheme.current.surface else LocalTheme.current.primary)
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MaterialIconButton(
                        icon = Icons.Rounded.ArrowBack,
                        onClick = { backStack.pop() },
                        modifier = Modifier.padding(LocalSpacing.current.extraSmall),
                        contentDescription = "back"
                    )
                    Spacer(modifier = Modifier.width(LocalSpacing.current.small))
                    TextField(
                        placeholder = stringResource(R.string.query_input),
                        background = LocalTheme.current.surface,
                        textFieldValue = state.text,
                        onValueChange = { viewModel.onEvent(QueryEvent.OnText(it)) },
                        imeAction = ImeAction.Search,
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.onEvent(QueryEvent.Query)
                            }
                        ),
                        modifier = Modifier.padding(LocalSpacing.current.small)
                    )

                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            color = LocalTheme.current.background,
                        )
                ) {
                    if (state.conversations.isNotEmpty())
                        stickyHeader {
                            Surface(
                                color = (
                                        if (vm.readable.isDarkMode) LocalTheme.current.surface
                                        else LocalTheme.current.primary
                                        ).copy(alpha = 0.8f),
                                contentColor = if (vm.readable.isDarkMode) LocalTheme.current.onSurface
                                else LocalTheme.current.onPrimary,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.query_result_conversation),
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(
                                        horizontal = LocalSpacing.current.medium,
                                        vertical = LocalSpacing.current.extraSmall
                                    )
                                )
                            }
                        }
                    items(state.conversations) { conversation ->
                        ConversationItem(conversation = conversation) {
                            backStack.push(NavTarget.ChatTarget.Messages(conversation.id))
                        }
                    }
                    if (state.users.isNotEmpty())
                        stickyHeader {
                            Surface(
                                color = (
                                        if (vm.readable.isDarkMode) LocalTheme.current.surface
                                        else LocalTheme.current.primary
                                        ).copy(alpha = 0.8f),
                                contentColor = if (vm.readable.isDarkMode) LocalTheme.current.onSurface
                                else LocalTheme.current.onPrimary,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.query_result_user),
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(
                                        horizontal = LocalSpacing.current.medium,
                                        vertical = LocalSpacing.current.extraSmall
                                    )
                                )
                            }
                        }
                    items(state.users) { user ->
                        UserItem(user = user) {
                            backStack.push(NavTarget.Introduce(user.id))
                        }
                    }
                    if (state.messages.isNotEmpty())
                        stickyHeader {
                            Surface(
                                color = (
                                        if (vm.readable.isDarkMode) LocalTheme.current.surface
                                        else LocalTheme.current.primary
                                        ).copy(alpha = 0.8f),
                                contentColor = if (vm.readable.isDarkMode) LocalTheme.current.onSurface
                                else LocalTheme.current.onPrimary,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.query_result_message),
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.padding(
                                        horizontal = LocalSpacing.current.medium,
                                        vertical = LocalSpacing.current.extraSmall
                                    )
                                )
                            }
                        }
                    items(state.messages) { message ->
                        ListItem(
                            headlineText = {
                                Text(text = message.content)
                            }
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = LocalTheme.current.background,
                        )
                        .imePadding(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilterChip(
                        selected = state.isDescription,
                        onClick = { viewModel.onEvent(QueryEvent.ToggleIncludeDescription) },
                        label = {
                            Text(
                                text = stringResource(R.string.query_filter_description),
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            labelColor = LocalTheme.current.onBackground,
                            iconColor = LocalTheme.current.onBackground,
                            containerColor = LocalTheme.current.background
                        )
                    )
                    FilterChip(
                        selected = state.isEmail,
                        onClick = { viewModel.onEvent(QueryEvent.ToggleIncludeEmail) },
                        label = {
                            Text(
                                text = stringResource(R.string.query_filter_email),
                                style = MaterialTheme.typography.titleSmall
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            labelColor = LocalTheme.current.onBackground,
                            iconColor = LocalTheme.current.onBackground,
                            containerColor = LocalTheme.current.background
                        )
                    )
                }
            }
        }
    }

}
