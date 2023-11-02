@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)

package com.myprojectname.app.feature.loginsignup

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.myprojectname.app.localization.getCurrentLocalization
import com.myprojectname.app.platform.Resources
import com.myprojectname.app.ui.theme.MyProjectNameTheme
import com.myprojectname.app.ui.theme.Typography
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

class LoginSignUpLandingScreen : Screen {
    @Composable
    override fun Content() {
        MyProjectNameTheme {
            LoginSignUpView()
        }
    }
}

@Composable
fun LoginSignUpView() {
    val localization = getCurrentLocalization()
    val currentNavigator = LocalNavigator.currentOrThrow

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { currentNavigator.pop() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = localization.backLabel
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(Resources.Drawables.catAndDog),
                contentDescription = "",
                modifier = Modifier.size(150.dp)
            )

            Text(
                text = AnnotatedString(localization.loginLandingTitle),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                style = Typography.get().headlineMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = AnnotatedString(localization.loginLandingDescription),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                style = Typography.get().bodySmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = localization.loginLandingFacebook,
                    style = Typography.get().bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = localization.loginLandingGoogle,
                    style = Typography.get().bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    currentNavigator.push(EmailSignUpScreen())
                },
                colors = ButtonDefaults.buttonColors(),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 16.dp, end = 16.dp)
            ) {
                Text(
                    text = localization.loginLandingEmail,
                    style = Typography.get().bodyMedium
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = AnnotatedString(localization.loginLandingAlreadyHaveAccount),
                    style = Typography.get().bodyLarge
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = AnnotatedString(localization.loginLandingLogIn),
                    modifier = Modifier.clickable {
                        currentNavigator.push(EmailLoginScreen())
                    },
                    style = Typography.get().bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

/*
@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    MyProjectNameTheme {
        LoginScreen()
    }
}
*/
