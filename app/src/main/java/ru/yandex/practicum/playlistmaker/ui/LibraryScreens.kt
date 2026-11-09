package ru.yandex.practicum.playlistmaker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.selected
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.domain.Playlist

@Composable
fun PlaylistsScreen(viewModel: LibraryViewModel, create: () -> Unit, open: (Long) -> Unit, back: () -> Unit) {
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    Screen(stringResource(R.string.playlists), back) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            if (playlists.isEmpty()) Text(stringResource(R.string.empty_playlists), Modifier.align(Alignment.Center))
            LazyColumn(contentPadding = PaddingValues(bottom = 104.dp)) {
                items(playlists, key = { it.id }) { playlist -> PlaylistItem(playlist) { open(playlist.id) } }
            }
            FloatingActionButton(create, Modifier.align(Alignment.BottomEnd).padding(24.dp), shape = CircleShape) {
                Icon(Icons.Default.Add, stringResource(R.string.add_playlist))
            }
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, click: () -> Unit) {
    ListItem(modifier = Modifier.clickable(onClick = click), leadingContent = { Icon(Icons.Default.QueueMusic, null, Modifier.size(48.dp)) },
        headlineContent = { Text(playlist.name) }, supportingContent = { Text(stringResource(R.string.track_count, playlist.trackIds.size)) })
}

@Composable
fun NewPlaylistScreen(viewModel: LibraryViewModel, back: () -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val saveState by viewModel.saveState.collectAsStateWithLifecycle()
    LaunchedEffect(saveState) { if (saveState == SaveState.Saved) { viewModel.resetSave(); back() } }
    Screen(stringResource(R.string.new_playlist), back) { padding ->
        Column(Modifier.padding(padding).padding(16.dp).imePadding(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(name, { name = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.playlist_name)) }, singleLine = true)
            OutlinedTextField(description, { description = it }, Modifier.fillMaxWidth(), label = { Text(stringResource(R.string.playlist_description)) }, minLines = 3)
            if (saveState == SaveState.Error) Text(stringResource(R.string.save_error), color = MaterialTheme.colorScheme.error)
            Button({ viewModel.createPlaylist(name, description) }, Modifier.fillMaxWidth(), enabled = name.isNotBlank() && saveState != SaveState.Saving) {
                Text(stringResource(R.string.save))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackDetailsScreen(id: Long, viewModel: LibraryViewModel, back: () -> Unit) {
    val flow = remember(id, viewModel) { viewModel.track(id) }
    val track by flow.collectAsStateWithLifecycle(initialValue = null)
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    var showPlaylists by rememberSaveable(id) { mutableStateOf(false) }
    Screen(stringResource(R.string.track_details), back) { padding ->
        val item = track
        if (item == null) Text(stringResource(R.string.details_missing), Modifier.padding(padding)) else {
            Column(Modifier.padding(padding).fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.MusicNote, stringResource(R.string.track_icon), Modifier.size(200.dp))
                Text(item.trackName, style = MaterialTheme.typography.headlineSmall)
                Text(item.artistName, style = MaterialTheme.typography.titleMedium)
                Text(item.trackTime)
                Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                    IconButton({ viewModel.toggleFavorite(item.id) }, Modifier.semantics { selected = item.favorite }) {
                        Icon(if (item.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            stringResource(R.string.toggle_favorite), tint = if (item.favorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton({ showPlaylists = true }) { Icon(Icons.Default.PlaylistAdd, stringResource(R.string.add_to_playlist)) }
                }
            }
        }
    }
    if (showPlaylists) ModalBottomSheet(onDismissRequest = { showPlaylists = false }) {
        Text(stringResource(R.string.add_to_playlist), Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
        if (playlists.isEmpty()) Text(stringResource(R.string.create_first_playlist), Modifier.padding(16.dp))
        LazyColumn(Modifier.fillMaxWidth(), contentPadding = PaddingValues(bottom = 24.dp)) {
            items(playlists, key = { it.id }) { playlist -> PlaylistItem(playlist) {
                viewModel.addToPlaylist(id, playlist.id); showPlaylists = false
            } }
        }
    }
}

@Composable
fun PlaylistDetailsScreen(id: Long, viewModel: LibraryViewModel, onTrack: (Long) -> Unit, back: () -> Unit) {
    val playlists by viewModel.playlists.collectAsStateWithLifecycle()
    val playlist = playlists.find { it.id == id }
    Screen(playlist?.name ?: stringResource(R.string.playlists), back) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            item { Text(playlist?.description.orEmpty(), Modifier.padding(16.dp)) }
            if (playlist?.trackIds.isNullOrEmpty()) item { Text(stringResource(R.string.empty_playlist), Modifier.padding(16.dp)) }
            items(playlist?.trackIds?.toList().orEmpty(), key = { it }) { trackId ->
                val flow = remember(trackId) { viewModel.track(trackId) }
                val track by flow.collectAsStateWithLifecycle(initialValue = null)
                track?.let { TrackListItem(it) { onTrack(it.id) } }
            }
        }
    }
}
