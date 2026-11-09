package ru.yandex.practicum.playlistmaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*
import org.koin.androidx.compose.koinViewModel
import ru.yandex.practicum.playlistmaker.R

enum class Destination { MAIN, SEARCH, SETTINGS, PLAYLISTS, FAVORITES, NEW_PLAYLIST }

@Composable
fun PlaylistHost(navController: NavHostController = rememberNavController()) {
    val library: LibraryViewModel = koinViewModel()
    val search: SearchViewModel = koinViewModel()
    val back: () -> Unit = { navController.popBackStack(); Unit }
    NavHost(navController, startDestination = Destination.MAIN.name) {
        composable(Destination.MAIN.name) {
            MainScreen { destination -> navController.navigate(destination.name) { launchSingleTop = true } }
        }
        composable(Destination.SEARCH.name) {
            SearchScreen(search, { id -> navController.navigate("track/$id") }, back)
        }
        composable(Destination.SETTINGS.name) { SettingsScreen(back) }
        composable(Destination.FAVORITES.name) { Screen(stringResource(R.string.favorites), back) { padding -> Box(Modifier.padding(padding).fillMaxSize()) } }
        composable(Destination.PLAYLISTS.name) {
            PlaylistsScreen(library, { library.resetSave(); navController.navigate(Destination.NEW_PLAYLIST.name) },
                { id -> navController.navigate("playlist/$id") }, back)
        }
        composable(Destination.NEW_PLAYLIST.name) { NewPlaylistScreen(library, back) }
        composable("track/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) { entry ->
            TrackDetailsScreen(entry.arguments?.getLong("id") ?: -1, library, back)
        }
        composable("playlist/{id}", arguments = listOf(navArgument("id") { type = NavType.LongType })) { entry ->
            PlaylistDetailsScreen(entry.arguments?.getLong("id") ?: -1, library, { id -> navController.navigate("track/$id") }, back)
        }
    }
}

@Composable
fun MainScreen(navigate: (Destination) -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFF3772E7)).safeDrawingPadding().padding(16.dp)) {
        Text(stringResource(R.string.app_name), color = Color.White, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(40.dp))
        val entries = listOf(Triple(R.string.search, Icons.Default.Search, Destination.SEARCH),
            Triple(R.string.playlists, Icons.Default.QueueMusic, Destination.PLAYLISTS),
            Triple(R.string.favorites, Icons.Default.Favorite, Destination.FAVORITES),
            Triple(R.string.settings, Icons.Default.Settings, Destination.SETTINGS))
        entries.forEach { (label, icon, destination) ->
            Button({ navigate(destination) }, Modifier.fillMaxWidth().height(88.dp).padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)) {
                Icon(icon, null); Spacer(Modifier.width(12.dp)); Text(stringResource(label), style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
