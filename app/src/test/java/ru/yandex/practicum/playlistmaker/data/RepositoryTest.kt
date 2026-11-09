package ru.yandex.practicum.playlistmaker.data

import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

class RepositoryTest {
    @Test fun searchMatchesTitleAndArtistIgnoringCase() = runTest {
        val repository = TracksRepositoryImpl(DatabaseMock())
        assertEquals(1, repository.searchTracks("YESTERDAY").size)
        assertEquals(8, repository.searchTracks("beatles").size)
        assertTrue(repository.searchTracks("unknown artist").isEmpty())
        assertTrue(repository.searchTracks(" ").isEmpty())
    }
}
