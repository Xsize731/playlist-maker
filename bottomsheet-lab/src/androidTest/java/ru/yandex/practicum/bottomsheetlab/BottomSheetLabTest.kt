package ru.yandex.practicum.bottomsheetlab

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.geometry.Offset
import org.junit.Rule
import org.junit.Test

class BottomSheetLabTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    @Test fun fabOpensPanelAndOutsideTapClosesIt() {
        compose.onNodeWithContentDescription("Открыть панель").performClick()
        compose.onNodeWithText("Это панель BottomSheet").assertIsDisplayed()
        compose.onAllNodes(isRoot()).onLast().performTouchInput { click(Offset(10f, 10f)) }
        compose.waitForIdle()
        compose.onNodeWithText("Это панель BottomSheet").assertDoesNotExist()
        compose.onNodeWithContentDescription("Открыть панель").performClick()
        compose.onNodeWithText("Закрыть").performClick()
        compose.onNodeWithText("Это панель BottomSheet").assertDoesNotExist()
    }
}

