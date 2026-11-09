package ru.yandex.practicum.playlistmaker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.domain.Track

@Composable
fun SearchScreen(viewModel: SearchViewModel, onTrack: (Long) -> Unit, back: () -> Unit) {
    val query by viewModel.query.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val history by viewModel.history.collectAsStateWithLifecycle()
    Screen(stringResource(R.string.search), back) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 16.dp)) {
            SearchField(query, viewModel::changeQuery, viewModel::search)
            when (val current = state) {
                SearchState.Initial -> if (query.isBlank() && history.isNotEmpty()) {
                    Text(stringResource(R.string.history), Modifier.padding(vertical = 16.dp), style = MaterialTheme.typography.titleMedium)
                    LazyColumn(Modifier.weight(1f)) {
                        items(history, key = { it }) { value -> ListItem(headlineContent = { Text(value) }, modifier = Modifier.clickable { viewModel.selectHistory(value) }) }
                        item { TextButton(viewModel::clearHistory) { Text(stringResource(R.string.clear_history)) } }
                    }
                }
                SearchState.Searching -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                SearchState.Failure -> Column { Text(stringResource(R.string.search_error)); Button(viewModel::search) { Text(stringResource(R.string.retry)) } }
                is SearchState.Success -> if (current.tracks.isEmpty()) Text(stringResource(R.string.empty_results)) else {
                    LazyColumn { items(current.tracks, key = { it.id }) { track -> TrackListItem(track) { onTrack(track.id) } } }
                }
            }
        }
    }
}

@Composable
fun TrackListItem(track: Track, onClick: () -> Unit) {
    ListItem(modifier = Modifier.clickable(onClick = onClick),
        leadingContent = { Icon(Icons.Default.MusicNote, stringResource(R.string.track_icon), Modifier.size(48.dp)) },
        headlineContent = { Text(track.trackName, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = { Text("${track.artistName} • ${track.trackTime}", maxLines = 1, overflow = TextOverflow.Ellipsis) })
}
