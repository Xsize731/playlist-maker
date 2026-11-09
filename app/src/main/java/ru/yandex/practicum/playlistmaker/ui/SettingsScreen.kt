package ru.yandex.practicum.playlistmaker.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.yandex.practicum.playlistmaker.R

@Composable
fun SettingsScreen(back: () -> Unit) {
    val context = LocalContext.current
    val email = stringResource(R.string.support_email)
    val subject = stringResource(R.string.support_subject)
    val body = stringResource(R.string.support_body)
    val offer = stringResource(R.string.agreement_url)
    val share = stringResource(R.string.share_text)
    val noHandler = stringResource(R.string.no_handler)
    fun open(intent: Intent) {
        try { context.startActivity(intent) } catch (_: ActivityNotFoundException) { Toast.makeText(context, noHandler, Toast.LENGTH_SHORT).show() }
    }
    Screen(stringResource(R.string.settings), back) { padding ->
        Column(Modifier.padding(padding)) {
            ListItem(headlineContent = { Text(stringResource(R.string.dark_theme)) }, trailingContent = { Switch(false, null, enabled = false) })
            ListItem(modifier = Modifier.clickable { open(Intent.createChooser(Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, share) }, null)) },
                headlineContent = { Text(stringResource(R.string.share)) }, trailingContent = { Icon(Icons.Default.Share, null) })
            ListItem(modifier = Modifier.clickable {
                open(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + Uri.encode(email))).apply {
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(email)); putExtra(Intent.EXTRA_SUBJECT, subject); putExtra(Intent.EXTRA_TEXT, body)
                    data = Uri.parse("mailto:" + Uri.encode(email) + "?subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body))
                })
            }, headlineContent = { Text(stringResource(R.string.support)) }, trailingContent = { Icon(Icons.Default.Email, null) })
            ListItem(modifier = Modifier.clickable { open(Intent(Intent.ACTION_VIEW, Uri.parse(offer))) },
                headlineContent = { Text(stringResource(R.string.agreement)) }, trailingContent = { Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, null, Modifier.size(20.dp)) })
        }
    }
}
