package ru.yandex.practicum.playlistmaker
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { Column(Modifier.fillMaxSize().background(Color(0xFF3772E7)).padding(16.dp)) {
            Text(stringResource(R.string.app_name), color=Color.White, style=MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(40.dp))
            Button(onClick={startActivity(Intent(this@MainActivity,SearchActivity::class.java))}, modifier=Modifier.fillMaxWidth()) { Text(stringResource(R.string.search)) }
            Button(onClick={startActivity(Intent(this@MainActivity,SettingsActivity::class.java))}, modifier=Modifier.fillMaxWidth()) { Text(stringResource(R.string.settings)) }
        } } }
    }
}
class SearchActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { MaterialTheme { Text(stringResource(R.string.search)) } } }
}
class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); setContent { MaterialTheme { Text(stringResource(R.string.settings)) } } }
}
