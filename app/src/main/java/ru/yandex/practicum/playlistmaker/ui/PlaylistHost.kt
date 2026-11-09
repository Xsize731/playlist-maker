package ru.yandex.practicum.playlistmaker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import org.koin.androidx.compose.koinViewModel
import ru.yandex.practicum.playlistmaker.R

enum class Destination { MAIN, SEARCH, SETTINGS }

@Composable
fun PlaylistHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, startDestination = Destination.MAIN.name) {
        composable(Destination.MAIN.name) {
            MainScreen(
                onSearch = { navController.navigate(Destination.SEARCH.name) },
                onSettings = { navController.navigate(Destination.SETTINGS.name) }
            )
        }
        composable(Destination.SEARCH.name) { SearchScreen(koinViewModel(), { id -> navController.navigate("track/$id") }) { navController.popBackStack() } }
        composable("track/{id}") { Screen(stringResource(R.string.track_details), { navController.popBackStack() }) { padding -> Text("Track ${it.arguments?.getString("id")}", Modifier.padding(padding)) } }
        composable(Destination.SETTINGS.name) { SettingsScreen { navController.popBackStack() } }
    }
}

@Composable
fun MainScreen(onSearch: () -> Unit, onSettings: () -> Unit) {
    Column(Modifier.fillMaxSize().background(Color(0xFF3772E7)).safeDrawingPadding().padding(16.dp)) {
        Text(stringResource(R.string.app_name), color = Color.White, style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(40.dp))
        listOf(R.string.search to onSearch, R.string.settings to onSettings).forEach { (label, action) ->
            Button(action, Modifier.fillMaxWidth().height(100.dp).padding(bottom = 16.dp),
                shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)) {
                Text(stringResource(label), style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}
