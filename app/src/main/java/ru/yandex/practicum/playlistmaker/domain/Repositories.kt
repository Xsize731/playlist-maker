package ru.yandex.practicum.playlistmaker.domain

import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    suspend fun searchTracks(expression: String): List<Track>
    fun getTrack(id: Long): Flow<Track?>
    suspend fun updateFavorite(id: Long)
    suspend fun addToPlaylist(trackId: Long, playlistId: Long)
}
interface PlaylistsRepository {
    val playlists: Flow<List<Playlist>>
    suspend fun create(name: String, description: String): Long
}
interface SearchHistoryRepository {
    val history: Flow<List<String>>
    suspend fun add(query: String)
    suspend fun clear()
}
