package ru.yandex.practicum.playlistmaker.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import ru.yandex.practicum.playlistmaker.R

@Composable
fun SearchField(query: String, onChange: (String) -> Unit, onSearch: () -> Unit) {
    OutlinedTextField(value = query, onValueChange = onChange, modifier = Modifier.fillMaxWidth(),
        singleLine = true, placeholder = { Text(stringResource(R.string.search)) },
        leadingIcon = { IconButton(onSearch) { Icon(Icons.Default.Search, stringResource(R.string.search)) } },
        trailingIcon = { if (query.isNotEmpty()) IconButton({ onChange("") }) { Icon(Icons.Default.Clear, stringResource(R.string.clear)) } },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), keyboardActions = KeyboardActions(onSearch = { onSearch() }))
}
