package ru.yandex.practicum.playlistmaker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.yandex.practicum.playlistmaker.di.appModule

class PlaylistApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin { androidContext(this@PlaylistApplication); modules(appModule) }
    }
}
