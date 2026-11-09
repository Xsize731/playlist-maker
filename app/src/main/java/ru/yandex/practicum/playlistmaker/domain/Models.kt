package ru.yandex.practicum.playlistmaker.domain

data class Track(val id: Long, val trackName: String, val artistName: String, val trackTime: String, val favorite: Boolean = false, val playlistId: Long? = null)
data class Playlist(val id: Long, val name: String, val description: String, val trackIds: Set<Long> = emptySet())
