package com.example.deviceinfogetter.ui.screen

import android.content.Context
import android.view.View
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
class DeviceInfoScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<HiltTestActivity>()

    @BindValue
    @JvmField
    val fakeDeviceInfoRepository: DeviceInfoRepository = object : DeviceInfoRepository {
        override fun getDeviceInfo() = listOf(
            DeviceInfoDto("Model Name", "Galaxy S25"),
            DeviceInfoDto("Manufacture", "Samsung"),
        )
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
    fun deviceInfoScreen_navigationButton_isDisplayed() {
        composeRule.setContent {
            DeviceInfoTheme {
                DeviceInfoScreen(moveToSquareScreen = {})
            }
        }
        composeRule.onNodeWithText("Check the square").assertIsDisplayed()
    }

    @Test
    fun deviceInfoScreen_showsItemsFromRepository() {
        composeRule.setContent {
            DeviceInfoTheme {
                DeviceInfoScreen(moveToSquareScreen = {})
            }
        }
        composeRule.onNodeWithText("Model Name").assertIsDisplayed()
        composeRule.onNodeWithText("Galaxy S25").assertIsDisplayed()
        composeRule.onNodeWithText("Manufacture").assertIsDisplayed()
        composeRule.onNodeWithText("Samsung").assertIsDisplayed()
    }

    @Test
    fun deviceInfoScreen_clickNavigationButton_triggersCallback() {
        var navigationCalled = false
        composeRule.setContent {
            DeviceInfoTheme {
                DeviceInfoScreen(moveToSquareScreen = { navigationCalled = true })
            }
        }
        composeRule.onNodeWithText("Check the square").performClick()
        assertTrue(navigationCalled)
    }
}
