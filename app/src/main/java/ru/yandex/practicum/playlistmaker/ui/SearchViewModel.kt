package ru.yandex.practicum.playlistmaker.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.yandex.practicum.playlistmaker.domain.*

sealed interface SearchState {
    data object Initial : SearchState
    data object Searching : SearchState
    data class Success(val tracks: List<Track>) : SearchState
    data object Failure : SearchState
}
class SearchViewModel(private val tracks: TracksRepository, private val historyRepository: SearchHistoryRepository, private val savedState: SavedStateHandle) : ViewModel() {
    val query = savedState.getStateFlow("query", "")
    val history = historyRepository.history.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    private val _state = MutableStateFlow<SearchState>(SearchState.Initial)
    val state = _state.asStateFlow()
    private var searchJob: Job? = null

    fun changeQuery(value: String) { savedState["query"] = value }
    fun search() {
        searchJob?.cancel()
        val value = query.value.trim()
        if (value.isBlank()) { _state.value = SearchState.Initial; return }
        searchJob = viewModelScope.launch {
            _state.value = SearchState.Searching
            try { _state.value = SearchState.Success(tracks.searchTracks(value)) }
            catch (e: CancellationException) { throw e }
            catch (_: Exception) { _state.value = SearchState.Failure }
        }
    }
    fun clearHistory() { viewModelScope.launch { historyRepository.clear() } }
}
