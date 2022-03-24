package dev.w1zzrd.btpw

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.w1zzrd.btpw.ui.theme.MyApplicationTheme

data class LoginTranslations(
    val labelUsername: String,
    val labelPassword: String,
    val descShowPassword: String,
    val descHidePassword: String,
    val labelLoginButton: String,
    val descErrorIcon: String,
    val descTitleIcon: String,
    val title: String
)

class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val translations = LoginTranslations(
            getString(R.string.label_username),
            getString(R.string.label_password),
            getString(R.string.desc_show_password),
            getString(R.string.desc_hide_password),
            getString(R.string.label_login),
            getString(R.string.error_icon),
            getString(R.string.desc_title),
            getString(R.string.title)
        )

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   Box(Modifier.fillMaxSize()) {
                       Column(Modifier.align(Alignment.Center)) {
                           var username by rememberSaveable { mutableStateOf("") }
                           var password by rememberSaveable { mutableStateOf("") }
                           var usernameError by rememberSaveable { mutableStateOf(false) }
                           var passwordError by rememberSaveable { mutableStateOf(false) }
                           var loginEnabled by rememberSaveable { mutableStateOf(true) }

                           Title(translations, modifier = Modifier.align(CenterHorizontally))
                           Spacer(Modifier.height(35.dp))
                           Login(
                               translations = translations,
                               username = username,
                               onUsernameChange = {
                                   username = it
                                   if (usernameError)
                                       usernameError = username.isBlank()
                               },
                               password = password,
                               onPasswordChange = {
                                   password = it
                                   if (passwordError)
                                       passwordError = password.isBlank()
                               },
                               usernameError = usernameError,
                               passwordError = passwordError,
                               loginEnabled = loginEnabled,
                               onLoginClick = {
                                   if (username.isBlank()) {
                                       username = ""
                                       usernameError = true
                                   }
                                   if (password.isBlank()) {
                                       password = ""
                                       passwordError = true
                                   }
                                   if (username.isNotBlank() && password.isNotBlank()) {
                                       loginEnabled = false

                                       // TODO: Implement login here
                                   }
                               }
                           )
                       }
                   }
                }
            }
        }
    }
}

@Composable
fun Title(translations: LoginTranslations, modifier: Modifier = Modifier) {
    Row(modifier) {
        Icon(Icons.Filled.VpnKey, translations.descTitleIcon, modifier = Modifier.align(CenterVertically))
        Spacer(Modifier.width(16.dp))
        Text(
            text = translations.title,
            fontSize = MaterialTheme.typography.h4.fontSize,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var usernameError by rememberSaveable { mutableStateOf(false) }
    var passwordError by rememberSaveable { mutableStateOf(false) }
    var loginEnabled by rememberSaveable { mutableStateOf(true) }

    MyApplicationTheme(darkTheme = true) {
        Box(Modifier.padding(8.dp)) {
            Login(
                LoginTranslations(
                    "Username",
                    "Password",
                    "Show password",
                    "Hide password",
                    "Log in",
                    "Error",
                    "BlueKey",
                    "BlueKey"
                ),
                username = username,
                onUsernameChange = {
                    username = it
                    if (usernameError)
                        usernameError = username.isBlank()
                },
                password = password,
                onPasswordChange = {
                    password = it
                    if (passwordError)
                        passwordError = password.isBlank()
                },
                usernameError = usernameError,
                passwordError = passwordError,
                loginEnabled = loginEnabled,
                onLoginClick = {
                    loginEnabled = false
                }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Login(
    translations: LoginTranslations,
    username: String,
    onUsernameChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    usernameError: Boolean,
    passwordError: Boolean,
    loginEnabled: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focus = LocalFocusManager.current

        fun onClick() {
            focus.clearFocus()
            keyboardController?.hide()
            if (loginEnabled) onLoginClick()
        }

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text(translations.labelUsername) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(onNext = { focus.moveFocus(FocusDirection.Down) }),
            trailingIcon = { if (usernameError) Icon(Icons.Filled.Error, translations.descErrorIcon) },
            isError = usernameError
        )
        Spacer(modifier = Modifier.height(8.dp))
        PasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text(translations.labelPassword) },
            descShowPassword = translations.descShowPassword,
            descHidePassword = translations.descHidePassword,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { onClick() }),
            isError = passwordError,
            descIconError = translations.descErrorIcon
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Button(onClick = ::onClick, enabled = loginEnabled) {
                Text(text = translations.labelLoginButton)
            }
        }
    }
}
