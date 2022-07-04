package com.linku.im.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.linku.domain.Auth
import com.linku.im.screen.global.GlobalViewModel
import com.linku.im.R
import com.linku.im.screen.Screen
import com.linku.im.screen.main.composable.MainConversationItem
import com.linku.im.screen.main.composable.MainDrawer
import com.linku.im.ui.verticalScrollbar

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
    globalViewModel: GlobalViewModel,
    toggleDrawer: () -> Unit
) {
    with(globalViewModel) {
        icon.value = Icons.Default.Menu
        title.value = stringResource(id = R.string.app_name)
        navClick.value = {
            toggleDrawer()
        }
        actions.value = {
            IconButton(onClick = { isDarkMode.value = !isDarkMode.value }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "")
            }
        }
    }
    val state by mainViewModel.state
    MainDrawer(
        drawerState = scaffoldState.drawerState,
        onNavigate = {
            when (it) {
                Screen.ProfileScreen ->
                    navController.navigate(
                        if (Auth.current == null) Screen.LoginScreen.route else it.route
                    )
                else -> navController.navigate(it.route)
            }
        }
    ) {
        val lazyListState = rememberLazyListState()
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .verticalScrollbar(
                    state = lazyListState,
                    color = MaterialTheme.colorScheme.tertiary
                ),
            userScrollEnabled = !state.loading
        ) {
            if (state.loading) {
                repeat(12) {
                    item {
                        MainConversationItem()
                        Divider()
                    }
                }
            } else {
                itemsIndexed(state.conversations) { index, conversation ->
                    MainConversationItem(
                        conversation,
                        pinned = index < 1,
                        unreadCount = index / 2
                    ) {
                        navController.navigate(
                            route = Screen.ChatScreen.withArgs(conversation.id)
                        )
                    }
                    Divider()
                }
            }

        }
    }
}