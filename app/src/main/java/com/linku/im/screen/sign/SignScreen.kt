package com.linku.im.screen.sign

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.insets.ui.Scaffold
import com.linku.im.R
import com.linku.im.ui.components.MaterialButton
import com.linku.im.ui.components.MaterialTextButton
import com.linku.im.ui.components.TextInputFieldOne
import com.linku.im.ui.theme.LocalNavController
import com.linku.im.ui.theme.LocalSpacing
import com.linku.im.vm

@Composable
fun LoginScreen(
    viewModel: SignViewModel = hiltViewModel()
) {
    val state = viewModel.readable
    val scaffoldState = rememberScaffoldState()
    val navController = LocalNavController.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

    val focusRequester = remember(::FocusRequester)

    LaunchedEffect(viewModel.message, vm.message) {
        viewModel.message.handle {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
        vm.message.handle {
            scaffoldState.snackbarHostState.showSnackbar(it)
        }
    }

    LaunchedEffect(state.loginEvent) {
        state.loginEvent.handle {
            navController.navigateUp()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(PaddingValues(horizontal = LocalSpacing.current.extraLarge)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                LottieAnimation(
                    composition = composition,
                    modifier = Modifier
                        .padding(bottom = LocalSpacing.current.medium)
                        .size(160.dp),
                    iterations = LottieConstants.IterateForever
                )
                TextInputFieldOne(
                    textFieldValue = state.email,
                    onValueChange = { viewModel.onEvent(SignEvent.OnEmail(it)) },
                    placeholder = stringResource(id = R.string.screen_login_tag_email),
                    enabled = !state.loading,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            // TODO
                        }
                    ),
                    modifier = Modifier
                        .focusRequester(focusRequester)
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalSpacing.current.medium)
                )

                TextInputFieldOne(
                    textFieldValue = state.password,
                    onValueChange = { viewModel.onEvent(SignEvent.OnPassword(it)) },
                    placeholder = stringResource(id = R.string.screen_login_tag_password),
                    enabled = !state.loading,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.onEvent(SignEvent.SignIn)
                            focusRequester.captureFocus()
                        }
                    )
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(LocalSpacing.current.extraLarge)
                )

                MaterialButton(
                    textRes = if (state.syncing) R.string.syncing else
                        R.string.screen_login_btn_login,
                    enabled = !state.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    viewModel.onEvent(SignEvent.SignIn)
                    focusRequester.captureFocus()
                }
                MaterialTextButton(
                    textRes = R.string.screen_login_btn_register,
                    enabled = !state.loading,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    viewModel.onEvent(SignEvent.SignUp)
                    focusRequester.captureFocus()
                }

                Spacer(modifier = Modifier.height(LocalSpacing.current.large))
            }
        }
    }

}