package com.example.deviceinfogetter.ui.screen

import android.content.Context
import android.view.View
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.deviceinfogetter.DeviceInfoDto
import com.example.deviceinfogetter.DeviceInfoTheme
import com.example.deviceinfogetter.HiltTestActivity
import com.example.deviceinfogetter.data.DeviceInfoRepository
import com.example.deviceinfogetter.data.ScreenshotRepository
import com.example.deviceinfogetter.di.RepositoryModule
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
class SquareScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @BindValue @JvmField
    val fakeDeviceInfoRepository: DeviceInfoRepository = object : DeviceInfoRepository {
        override fun getDeviceInfo() = emptyList<DeviceInfoDto>()
    }

    @BindValue @JvmField
    val fakeScreenshotRepository: ScreenshotRepository = object : ScreenshotRepository {
        override fun takeScreenshot(view: View, context: Context) {}
    }

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun squareScreen_showsDefaultBoxSizeText() {
        composeRule.setContent {
            DeviceInfoTheme { SquareScreen() }
        }
        composeRule.onNodeWithText("Size : 200 dp").assertIsDisplayed()
    }

    @Test
    fun squareScreen_screenshotButton_isDisplayed() {
        composeRule.setContent {
            DeviceInfoTheme { SquareScreen() }
        }
        composeRule.onNodeWithText("Screenshot").assertIsDisplayed()
    }

    @Test
    fun squareScreen_typingValidSize_updatesBoxText() {
        composeRule.setContent {
            DeviceInfoTheme { SquareScreen() }
        }
        composeRule.onNode(hasSetTextAction()).performTextReplacement("300")
        composeRule.onNodeWithText("Size : 300 dp").assertIsDisplayed()
    }

    @Test
    fun squareScreen_typingInvalidInput_doesNotChangeBoxText() {
        composeRule.setContent {
            DeviceInfoTheme { SquareScreen() }
        }
        composeRule.onNode(hasSetTextAction()).performTextReplacement("abc")
        composeRule.onNodeWithText("Size : 200 dp").assertIsDisplayed()
    }
}
