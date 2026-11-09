package ru.yandex.practicum.bottomsheetlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { BottomSheetLab() } }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetLab() {
    var showPanel by rememberSaveable { mutableStateOf(false) }
    Box(Modifier.fillMaxSize().safeDrawingPadding()) {
        FloatingActionButton(onClick = { showPanel = true }, modifier = Modifier.align(Alignment.BottomEnd).padding(32.dp),
            shape = CircleShape, containerColor = Color.Gray, contentColor = Color.White) {
            Icon(Icons.Default.Add, stringResource(R.string.open_panel))
        }
    }
    if (showPanel) ModalBottomSheet(onDismissRequest = { showPanel = false }) {
        Column(Modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.panel_content))
            TextButton({ showPanel = false }) { Text(stringResource(R.string.close)) }
        }
    }
}
