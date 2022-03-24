package dev.w1zzrd.btpw

import android.content.ClipboardManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DisabledVisible
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.w1zzrd.btpw.data.PasswordEntry
import dev.w1zzrd.btpw.ui.theme.MyApplicationTheme

typealias ClipboardStore = (String, String) -> Unit
data class PasswordListTranslations(
    val usernameInput: String,
    val passwordInput: String,
    val passwordVisibility: String,
    val clipboardDescription: String
)
private val TRANSLATIONS_SAMPLE =
    PasswordListTranslations(
        "Username",
        "Password",
        "Toggle password visibility",
        "Copy to clipboard"
    )
private val NO_CLIP: ClipboardStore = { _, _ -> }

class PasswordListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val clipboardService = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                }
            }
        }
    }
}

@Preview("Single Entry", showBackground = true)
@Composable
fun PreviewPasswordCard() {
    val entry = PasswordEntry("My first password", "jimmy", "supersecret", arrayOf())

    MyApplicationTheme(darkTheme = true) {
        Box(Modifier.padding(8.dp)) {
            PasswordCard(
                entry,
                TRANSLATIONS_SAMPLE,
                NO_CLIP
            )
        }
    }
}

@Preview("Entry List", showBackground = true)
@Composable
fun PreviewPasswordList() {
    val entries = arrayOf (
        PasswordEntry("My first password", "jimmy", "supersecret", arrayOf()),
        PasswordEntry("My second password", "neutron", "hunter2", arrayOf()),
        PasswordEntry("Top secret company password", "corpo", "plsnohack", arrayOf())
    )

    MyApplicationTheme(darkTheme = true) {
        PasswordList(entries = entries, translations = TRANSLATIONS_SAMPLE, saveClip = NO_CLIP)
    }
}


@Composable
fun PasswordList(entries: Array<PasswordEntry>, translations: PasswordListTranslations, saveClip: ClipboardStore) {
    // Evenly spaced list
    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(entries.size) { index ->
            if (index > 0) Spacer(Modifier.height(8.dp))
            PasswordCard(
                entries[index],
                translations,
                saveClip
            )
        }
    }
}
@Composable
fun PasswordCard(
    entry: PasswordEntry,
    translations: PasswordListTranslations,
    saveClip: ClipboardStore
) {
    var username by rememberSaveable { mutableStateOf(entry.username) }
    var password by rememberSaveable { mutableStateOf(entry.password) }
    var showPassword by rememberSaveable { mutableStateOf(false) }

    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            Text(
                text = entry.label,
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontStyle = if (username != entry.username || password != entry.password) FontStyle.Italic else null,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(8f)
                        .align(CenterVertically),
                    value = username,
                    label = { Text(translations.usernameInput) },
                    onValueChange = { username = it },
                    singleLine = true
                )
                IconButton(onClick = { saveClip(translations.usernameInput, username) }, modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)) {
                    Icon(Icons.Filled.ContentPaste, contentDescription = translations.clipboardDescription)
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(8f)
                        .align(CenterVertically),
                    value = password,
                    label = { Text(translations.passwordInput) },
                    onValueChange = { password = it },
                    singleLine = true,
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                if(showPassword) Icons.Filled.DisabledVisible else Icons.Filled.Visibility,
                                translations.passwordVisibility
                            )
                        }
                    }
                )
                IconButton(onClick = { saveClip("password", password) }, modifier = Modifier
                    .weight(1f)
                    .align(CenterVertically)) {
                    Icon(Icons.Filled.ContentPaste, contentDescription = translations.clipboardDescription)
                }
            }
        }
    }
}