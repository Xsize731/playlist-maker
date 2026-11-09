package ru.yandex.practicum.playlistmaker.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.yandex.practicum.playlistmaker.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen(title: String, back: (() -> Unit)? = null, content: @Composable (PaddingValues) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(title) }, navigationIcon = {
            if (back != null) IconButton(onClick = back) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
            }
        })
    }, content = content)
}
