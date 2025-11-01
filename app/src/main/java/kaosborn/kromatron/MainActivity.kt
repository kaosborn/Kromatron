package kaosborn.kromatron
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kaosborn.kromatron.ui.theme.KromatronTheme
import kaosborn.kromatron.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate (savedInstanceState:Bundle?) {
        super.onCreate (savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KromatronTheme {
                Surface (Modifier.fillMaxSize()) {
                    MainScreen()
                }
            }
        }
    }
}

@Preview(showBackground=false)
@Composable
fun MainScreenPreview() {
    KromatronTheme {
        MainScreen()
    }
}
