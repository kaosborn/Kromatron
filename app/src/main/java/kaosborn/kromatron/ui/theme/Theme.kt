package kaosborn.kromatron.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    surface = Olive,
    onSurface = Black,
    background = Red,
    onBackground = Green,
    primary = Tomato,
    onPrimary = Black,
    secondary = LightGrayCool,
    onSecondary = Magenta,
    tertiary = Yellow,
    onTertiary = Red
)

private val LightColorScheme = lightColorScheme(
    surface = Gold4,
    onSurface = Black,
    background = Red,
    onBackground = Green,
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
