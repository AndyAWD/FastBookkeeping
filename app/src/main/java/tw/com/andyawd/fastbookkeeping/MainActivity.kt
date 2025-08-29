package tw.com.andyawd.fastbookkeeping

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import tw.com.andyawd.fastbookkeeping.ui.FastBookkeepingApp
import tw.com.andyawd.fastbookkeeping.ui.theme.FastBookkeepingTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FastBookkeepingTheme {
                FastBookkeepingApp()
            }
        }
    }
}
