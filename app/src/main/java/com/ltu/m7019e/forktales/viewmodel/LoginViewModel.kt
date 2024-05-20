package com.ltu.m7019e.forktales.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the authentication of the ForkTales application.
 */
class LoginViewModel : ViewModel() {
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _isWrong = MutableStateFlow(false)
    val isWrong: StateFlow<Boolean> = _isWrong

    /**
     * Called when the username is changed.
     */
    fun onUsernameChange(newUsername: String) {
        _username.value = newUsername
    }

    /**
     * Called when the password is changed.
     */
    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    /**
     * Called when the login button is clicked, checks that the username and password are correct.
     */
    fun onLoginClick() {
        if (_username.value == "admin" && _password.value == "admin") {
            _isLoggedIn.value = true
        } else {
            _isWrong.value = true
            viewModelScope.launch {
                delay(1000)
                _isWrong.value = false
                _username.value = ""
                _password.value = ""
            }
        }
    }

    /**
     * Called when the logout button is clicked, logs the user out.
     */
    fun onLogoutClick() {
        _isLoggedIn.value = false
        _isWrong.value = false
        _username.value = ""
        _password.value = ""
    }
}
