package ru.yandex.practicum.playlistmaker.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.yandex.practicum.playlistmaker.domain.*

/** One application-scoped in-memory store; replaced by Room in a later sprint. */
class DatabaseMock {
    private val names = listOf("Yesterday (Remastered 2009)", "Here Comes The Sun (Remastered 2009)", "No Reply", "Let It Be", "Girl", "Michelle", "Eleanor Rigby", "Come Together")
    private val times = listOf("02:05", "03:07", "02:32", "04:03", "02:18", "02:47", "03:18", "04:19")
    private val _tracks = MutableStateFlow(names.mapIndexed { index, name -> Track(index + 1L, name, "The Beatles", times[index]) })
    val tracks = _tracks.asStateFlow()
    private val _playlists = MutableStateFlow(listOf(Playlist(1, "Best songs 2021", "My favorite tracks"), Playlist(2, "Summer Party", "Beach vibes")))
    val playlists = _playlists.asStateFlow()
    private val _history = MutableStateFlow(listOf("Rammstein", "Queen", "Bruno Mars"))
    val history = _history.asStateFlow()
    private var nextPlaylistId = 3L

    @Synchronized
    fun createPlaylist(name: String, description: String): Long {
        require(name.isNotBlank())
        val id = nextPlaylistId++
        _playlists.update { it + Playlist(id, name.trim(), description.trim()) }
        return id
    }
    fun toggleFavorite(id: Long) { _tracks.update { list -> list.map { if (it.id == id) it.copy(favorite = !it.favorite) else it } } }
    fun addToPlaylist(trackId: Long, playlistId: Long) {
        require(tracks.value.any { it.id == trackId })
        require(playlists.value.any { it.id == playlistId })
        _playlists.update { list -> list.map { if (it.id == playlistId) it.copy(trackIds = it.trackIds + trackId) else it } }
        _tracks.update { list -> list.map { if (it.id == trackId) it.copy(playlistId = playlistId) else it } }
    }
    fun addHistory(query: String) {
        val trimmed = query.trim()
        if (trimmed.isNotEmpty()) _history.update { old -> (listOf(trimmed) + old.filterNot { it.equals(trimmed, true) }).take(10) }
    }
    fun clearHistory() { _history.value = emptyList() }
}
