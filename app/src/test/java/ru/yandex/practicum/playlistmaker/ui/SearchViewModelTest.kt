package ru.yandex.practicum.playlistmaker.ui

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import ru.yandex.practicum.playlistmaker.data.*

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {
    @Before fun before() { Dispatchers.setMain(StandardTestDispatcher()) }
    @After fun after() { Dispatchers.resetMain() }
    private fun model(database: DatabaseMock) = SearchViewModel(TracksRepositoryImpl(database), SearchHistoryRepositoryImpl(database), SavedStateHandle())
    @Test fun newerQueryCancelsOldResultAndOnlyRecordsLatestHistory() = runTest {
        val database = DatabaseMock(); database.clearHistory()
        val model = model(database)
        model.changeQuery("Yesterday"); advanceTimeBy(501)
        assertEquals(SearchState.Searching, model.state.value)
        model.changeQuery("Michelle"); advanceUntilIdle()
        val result = model.state.value as SearchState.Success
        assertEquals("Michelle", result.tracks.single().trackName)
        assertEquals(listOf("Michelle"), database.history.value)
    }
    @Test fun clearCancelsRunningSearchWithoutRestoringOldResults() = runTest {
        val database = DatabaseMock(); database.clearHistory()
        val model = model(database)
        model.changeQuery("Yesterday"); advanceTimeBy(501)
        model.changeQuery(""); advanceUntilIdle()
        assertEquals(SearchState.Initial, model.state.value)
        assertTrue(database.history.value.isEmpty())
    }
    @Test fun emptySearchAndManualRetryWork() = runTest {
        val database = DatabaseMock(); val model = model(database)
        model.changeQuery("unknown"); model.search(); advanceUntilIdle()
        assertTrue((model.state.value as SearchState.Success).tracks.isEmpty())
        model.changeQuery("beatles"); model.search(); advanceUntilIdle()
        assertEquals(8, (model.state.value as SearchState.Success).tracks.size)
    }
}
