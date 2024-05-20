package com.ltu.m7019e.forktales.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.ltu.m7019e.forktales.ForkTalesNavScreen
import com.ltu.m7019e.forktales.R
import com.ltu.m7019e.forktales.viewmodel.LoginViewModel

/**
 * This is a Composable function that displays the login screen of the application.
 *  * It displays a form to enter a username and password, and a button to login.
 *
 *  @param navController The navigation controller used to navigate between different screens.
 *  @param loginViewModel The Login View Model.
 */
@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel
) {
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()
    val isWrong by loginViewModel.isWrong.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colors.primary,
        unfocusedBorderColor = if (isSystemInDarkTheme()) Color.Gray else MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled) ,
        focusedLabelColor = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colors.primary,
        unfocusedLabelColor = if (isSystemInDarkTheme()) Color.Gray else MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium)
    )

    val logo = if (isSystemInDarkTheme()) R.drawable.forktales_icon_foreground else R.drawable.forktales_icon

    if(isLoggedIn) {
        LaunchedEffect(Unit) {
            navController.navigate(ForkTalesNavScreen.Home.route) {
                popUpTo(ForkTalesNavScreen.Login.route) { inclusive = true }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(logo),
                contentDescription = null,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { loginViewModel.onUsernameChange(it) },
                label = {
                    if(isWrong) {
                        Text(stringResource(R.string.wrong))
                    } else {
                        Text(stringResource(R.string.username))
                    }
                },
                colors = textFieldColors
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = {
                    if(isWrong) {
                        Text(stringResource(R.string.wrong))
                    } else {
                        Text(stringResource(R.string.password))
                    }
                },
                colors = textFieldColors,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        loginViewModel.onLoginClick()
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { loginViewModel.onLoginClick() }) {
                Text(stringResource(R.string.login))
            }
        }
    }
}
