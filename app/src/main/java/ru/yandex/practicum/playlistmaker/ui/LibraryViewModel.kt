package ru.yandex.practicum.playlistmaker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.yandex.practicum.playlistmaker.domain.*

class LibraryViewModel(private val tracksRepository: TracksRepository, private val playlistsRepository: PlaylistsRepository) : ViewModel() {
    val playlists = playlistsRepository.playlists.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _saveState = MutableStateFlow<SaveState>(SaveState.Idle)
    val saveState = _saveState.asStateFlow()
    fun track(id: Long) = tracksRepository.getTrack(id)
    fun toggleFavorite(id: Long) { viewModelScope.launch { tracksRepository.updateFavorite(id) } }
    fun addToPlaylist(trackId: Long, playlistId: Long) { viewModelScope.launch { tracksRepository.addToPlaylist(trackId, playlistId) } }
    fun resetSave() { _saveState.value = SaveState.Idle }
    fun createPlaylist(name: String, description: String) {
        if (name.isBlank() || _saveState.value == SaveState.Saving) return
        _saveState.value = SaveState.Saving
        viewModelScope.launch {
            try { playlistsRepository.create(name, description); _saveState.value = SaveState.Saved }
            catch (e: CancellationException) { throw e }
            catch (_: Exception) { _saveState.value = SaveState.Error }
        }
    }
}
sealed interface SaveState {
    data object Idle : SaveState
    data object Saving : SaveState
    data object Saved : SaveState
    data object Error : SaveState
}
