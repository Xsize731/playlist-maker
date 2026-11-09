package ru.yandex.practicum.playlistmaker.data

import org.junit.Assert.*
import org.junit.Test

class DatabaseMockTest {
    @Test fun historyIsRecentUniqueAndLimited() {
        val database = DatabaseMock()
        database.clearHistory()
        repeat(12) { database.addHistory("query $it") }
        assertEquals(10, database.history.value.size)
        database.addHistory("QUERY 5")
        assertEquals("QUERY 5", database.history.value.first())
        assertEquals(1, database.history.value.count { it.equals("query 5", true) })
        database.addHistory(" ")
        assertEquals(10, database.history.value.size)
        database.clearHistory()
        assertTrue(database.history.value.isEmpty())
    }
    @Test fun playlistAndFavoriteChangesUseSameStore() {
        val database = DatabaseMock()
        val id = database.createPlaylist("  Road trip  ", "  Summer  ")
        assertEquals("Road trip", database.playlists.value.last().name)
        database.addToPlaylist(1, id)
        database.addToPlaylist(1, id)
        assertEquals(setOf(1L), database.playlists.value.last().trackIds)
        assertEquals(id, database.tracks.value.first().playlistId)
        database.toggleFavorite(1)
        assertTrue(database.tracks.value.first().favorite)
        database.toggleFavorite(1)
        assertFalse(database.tracks.value.first().favorite)
        assertTrue(database.playlists.value.last().trackIds.contains(1L))
    }
    @Test(expected = IllegalArgumentException::class) fun blankPlaylistCannotBeSaved() { DatabaseMock().createPlaylist(" ", "") }
}
