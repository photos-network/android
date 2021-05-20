package network.photos.android.app

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import network.photos.android.MainActivity
import org.junit.Rule
import org.junit.Test

class NavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun app_launches() {
        // Check app launches at the correct destination
//        assertEquals(getNavController().currentDestination?.id, R.id.nav_setup)
    }

//    private fun getNavController(): NavController {
//        return composeTestRule.activity.findNavController(R.id.nav_host_fragment)
//    }
}
