package kaosborn.kromatron.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    surface = Olive,
    onSurface = Gold,
    background = BlackForext,
    onBackground = Orchid,
    primary = Tomato,
    onPrimary = Black,
    secondary = BrightGray,
    onSecondary = Fuscia,
    tertiary = Yellow,
    onTertiary = Red
)

private val LightColorScheme = lightColorScheme(
    surface = Gold4,
    onSurface = Black,
    background = Silver,
    onBackground = Indigo,
    primary = Plum,
    onPrimary = Black,
    secondary = LightGray,
    onSecondary = Blue,
    tertiary = Yellow,
    onTertiary = Red
)

@Composable
fun KromatronTheme (darkTheme:Boolean=isSystemInDarkTheme(), content:@Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content
    )
}
