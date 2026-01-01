package kaosborn.kromatron
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import kaosborn.kromatron.ui.theme.KromatronTheme
import kaosborn.kromatron.ui.MainScreen
import kaosborn.kromatron.ui.GridGameViewModel

class MainActivity : ComponentActivity() {
    val vm:GridGameViewModel by viewModels()

    override fun onCreate (savedInstanceState:Bundle?) {
        super.onCreate (savedInstanceState)
        enableEdgeToEdge()
        vm.loadState (getPreferences (MODE_PRIVATE))
        setContent {
            AppScreen()
        }
    }

    override fun onPause() {
        vm.saveState (getPreferences (MODE_PRIVATE))
        super.onPause()
    }
}

@Preview(showBackground=false)
@Composable
fun MainScreenPreview() {
    AppScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen (vm:GridGameViewModel=viewModel()) {
    KromatronTheme {
        val showSettings = remember { mutableStateOf(false) }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text (text="KROMATRON", style=typography.headlineMedium)
                    },
                    actions = {
                        IconButton (onClick = { showSettings.value=true }) {
                            Icon (Icons.Filled.Settings, "settings")
                        }
                    },
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        ) {
            padVals ->
                Surface (Modifier.fillMaxSize().padding(padVals)) {
                    MainScreen (vm, showSettings)
                }
        }
    }
}
