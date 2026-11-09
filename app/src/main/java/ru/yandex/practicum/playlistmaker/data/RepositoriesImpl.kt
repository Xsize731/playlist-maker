package ru.yandex.practicum.playlistmaker.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import ru.yandex.practicum.playlistmaker.domain.*

class TracksRepositoryImpl(private val database: DatabaseMock) : TracksRepository {
    override suspend fun searchTracks(expression: String): List<Track> {
        delay(3000)
        val query = expression.trim()
        return if (query.isBlank()) emptyList() else database.tracks.value.filter {
            it.trackName.contains(query, ignoreCase = true) || it.artistName.contains(query, ignoreCase = true)
        }
    }
    override fun getTrack(id: Long) = database.tracks.map { tracks -> tracks.find { it.id == id } }
    override suspend fun updateFavorite(id: Long) = database.toggleFavorite(id)
    override suspend fun addToPlaylist(trackId: Long, playlistId: Long) = database.addToPlaylist(trackId, playlistId)
}
class PlaylistsRepositoryImpl(private val database: DatabaseMock) : PlaylistsRepository {
    override val playlists = database.playlists
    override suspend fun create(name: String, description: String) = database.createPlaylist(name, description)
}
class SearchHistoryRepositoryImpl(private val database: DatabaseMock) : SearchHistoryRepository {
    override val history = database.history
    override suspend fun add(query: String) = database.addHistory(query)
    override suspend fun clear() = database.clearHistory()
}
