package ru.yandex.practicum.playlistmaker

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class ProjectFlowTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    @Test fun searchDetailsFavoriteAndPlaylistCreation() {
        compose.onNodeWithText("Плейлисты").performClick()
        compose.onNodeWithContentDescription("Создать плейлист").performClick()
        compose.onNodeWithText("Сохранить").assertIsNotEnabled()
        compose.onNodeWithText("Название").performTextInput("UI Test Playlist")
        compose.onNodeWithText("Описание").performTextInput("Created by instrumented test")
        compose.onNodeWithText("Сохранить").performClick()
        compose.waitUntil(5000) { compose.onAllNodesWithText("UI Test Playlist").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithText("Поиск").performClick()
        compose.onNode(hasSetTextAction()).performTextInput("Yesterday")
        compose.waitUntil(10000) { compose.onAllNodesWithText("Yesterday (Remastered 2009)").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Yesterday (Remastered 2009)").performClick()
        compose.onNodeWithContentDescription("Изменить статус избранного").performClick().assertIsSelected()
        compose.onNodeWithContentDescription("Добавить в плейлист").performClick()
        compose.onNodeWithText("UI Test Playlist").performClick()
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithContentDescription("Назад").performClick()
        compose.onNodeWithText("Плейлисты").performClick()
        compose.onNodeWithText("UI Test Playlist").performClick()
        compose.onNodeWithText("Yesterday (Remastered 2009)").assertExists()
    }
}
