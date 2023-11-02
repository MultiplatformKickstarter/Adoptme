@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)

package com.myprojectname.app.feature.loginsignup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.feature.loginsignup.viewmodels.EmailSignUpSideEffects
import com.myprojectname.app.feature.loginsignup.viewmodels.EmailSignUpViewModel
import com.myprojectname.app.localization.Localization
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.ui.components.ColoredSnackBarHost
import com.myprojectname.app.ui.components.SnackbarType
import com.myprojectname.app.ui.components.showSnackbar
import com.myprojectname.app.ui.icon.MyProjectNameIcons
import com.myprojectname.app.ui.theme.MyProjectNameTheme
import com.myprojectname.app.ui.theme.Typography
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi

class EmailSignUpScreen : Screen {
    @Composable
    override fun Content() {
        MyProjectNameTheme {
            val localization = getCurrentLocalization()
            val snackbarHostState = remember { SnackbarHostState() }
            val viewModel = getScreenModel<EmailSignUpViewModel>()

            SetupSideEffects(viewModel, snackbarHostState)
            EmailSignUpView(viewModel, snackbarHostState, localization)
        }
    }

    @Composable
    private fun SetupSideEffects(viewModel: EmailSignUpViewModel, snackbarHostState: SnackbarHostState) {
        val coroutineScope = rememberCoroutineScope()
        val sideEffects = viewModel.sideEffects.collectAsState(EmailSignUpSideEffects.Initial)
        when (sideEffects.value) {
            EmailSignUpSideEffects.Initial -> {}
            EmailSignUpSideEffects.OnSignedUp -> viewModel.onSignedUp()
            is EmailSignUpSideEffects.OnSignUpError -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        type = SnackbarType.ERROR,
                        message = (sideEffects.value as EmailSignUpSideEffects.OnSignUpError).errorMessage
                    )
                }
            }
        }
    }
}

@Composable
fun EmailSignUpView(viewModel: EmailSignUpViewModel, snackbarHostState: SnackbarHostState, localization: Localization) {
    val currentNavigator = LocalNavigator.currentOrThrow
    val state by viewModel.state.collectAsState()

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    ColoredSnackBarHost(snackbarHostState)
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = localization.join,
                        style = Typography.get().bodyLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { currentNavigator.pop() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = localization.backLabel
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val name = remember { mutableStateOf(TextFieldValue()) }
            val email = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                leadingIcon = { Icon(imageVector = MyProjectNameIcons.Person, contentDescription = null) },
                label = { Text(text = localization.loginScreenNameLabel) },
                value = name.value,
                onValueChange = { email.value = it },
                textStyle = Typography.get().bodyMedium
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                leadingIcon = { Icon(imageVector = MyProjectNameIcons.Email, contentDescription = null) },
                label = { Text(text = localization.loginScreenEmailLabel) },
                value = email.value,
                onValueChange = { email.value = it },
                textStyle = Typography.get().bodyMedium,
                isError = state.isEmailError,
                supportingText = { Text(text = state.emailError) }
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                leadingIcon = { Icon(imageVector = MyProjectNameIcons.Password, contentDescription = null) },
                label = { Text(text = localization.loginScreenPasswordLabel) },
                value = password.value,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                onValueChange = { password.value = it },
                textStyle = Typography.get().bodyMedium,
                isError = state.isPasswordError,
                supportingText = { Text(text = state.passwordError) }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.onSubmit(name.value.text, email.value.text, password.value.text)
                },
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = localization.signUpCreateAnAccountButton,
                    style = Typography.get().bodyMedium
                )
            }

            Text(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
                text = localization.emailSignUpTermsAndConditions,
                textAlign = TextAlign.Center,
                style = Typography.get().bodySmall
            )
        }
    }
}
