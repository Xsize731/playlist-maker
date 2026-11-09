package ru.yandex.practicum.playlistmaker.di

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import ru.yandex.practicum.playlistmaker.data.*
import ru.yandex.practicum.playlistmaker.domain.*
import ru.yandex.practicum.playlistmaker.ui.SearchViewModel
import ru.yandex.practicum.playlistmaker.ui.LibraryViewModel

val appModule = module {
    single { DatabaseMock() }
    single<TracksRepository> { TracksRepositoryImpl(get()) }
    single<PlaylistsRepository> { PlaylistsRepositoryImpl(get()) }
    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get()) }
    viewModelOf(::SearchViewModel)
    viewModelOf(::LibraryViewModel)
}
