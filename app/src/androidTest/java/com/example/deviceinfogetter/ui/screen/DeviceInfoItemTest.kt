package com.example.deviceinfogetter.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.deviceinfogetter.DeviceInfoTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeviceInfoItemTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun deviceInfoItem_displaysLabel() {
        composeRule.setContent {
            DeviceInfoTheme {
                DeviceInfoItem(label = "Model Name", value = "Galaxy S25")
            }
        }
        composeRule.onNodeWithText("Model Name").assertIsDisplayed()
    }

    @Test
    fun deviceInfoItem_displaysValue() {
        composeRule.setContent {
            DeviceInfoTheme {
                DeviceInfoItem(label = "Model Name", value = "Galaxy S25")
            }
        }
        composeRule.onNodeWithText("Galaxy S25").assertIsDisplayed()
    }

    @Test
    fun deviceInfoItem_displaysLabelAndValueTogether() {
        composeRule.setContent {
            DeviceInfoTheme {
                DeviceInfoItem(label = "DPI", value = "480 dpi")
            }
        }
        composeRule.onNodeWithText("DPI").assertIsDisplayed()
        composeRule.onNodeWithText("480 dpi").assertIsDisplayed()
    }
}
