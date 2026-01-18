package kaosborn.kromatron.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun Scoreboard (score:Int, moves:Int, hiScore:Int, loMoves:Int, isGameOver:Boolean, onReset:() -> Unit) {
    Column (verticalArrangement=Arrangement.spacedBy(8.dp)) {
        Row (Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.weight (1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Score",
                    modifier = Modifier
                        .clip (RoundedCornerShape (topStart=8.dp, bottomStart=8.dp))
                        .background (colorScheme.secondary)
                        .padding (start=8.dp),
                    textAlign = TextAlign.Center,
                    style = typography.headlineMedium
                )
            }
            Box (Modifier.weight(1f)) {
                Row (
                    modifier = Modifier
                        .clip (RoundedCornerShape (topEnd=8.dp, bottomEnd=8.dp))
                        .background (colorScheme.secondary)
                ) {
                    Text(
                        text = ": $score",
                        modifier = Modifier.padding (end=8.dp),
                        style = typography.headlineMedium
                    )
                }
            }
        }

        Row (Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.weight (1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "High Score",
                    modifier = Modifier
                        .clip (RoundedCornerShape (topStart=8.dp, bottomStart=8.dp))
                        .background (colorScheme.secondary)
                        .padding (start=8.dp),
                    textAlign = TextAlign.Center,
                    style = typography.headlineSmall
                )
            }
            Box (Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .clip (RoundedCornerShape (topEnd=8.dp, bottomEnd=8.dp))
                        .background (colorScheme.secondary)
                ) {
                    Text(
                        text = ": $hiScore",
                        modifier = Modifier.padding (end=8.dp),
                        style = typography.headlineSmall
                    )
                }
            }
        }

        Row (Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.weight (1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text (
                    text = "Moves",
                    modifier = Modifier
                        .clip (RoundedCornerShape (topStart=8.dp, bottomStart=8.dp))
                        .background (colorScheme.secondary)
                        .padding (start=8.dp),
                    textAlign = TextAlign.Center,
                    style = typography.headlineMedium
                )
            }
            Box (Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .clip (RoundedCornerShape (topEnd=8.dp, bottomEnd=8.dp))
                        .background (colorScheme.secondary)
                ) {
                    Text(
                        text = ": $moves",
                        modifier = Modifier.padding (end=8.dp),
                        style = typography.headlineMedium
                    )
                }
            }
        }

        Row (Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "Low Moves",
                    modifier = Modifier
                        .clip (RoundedCornerShape (topStart=8.dp, bottomStart=8.dp))
                        .background (colorScheme.secondary)
                        .padding (start=8.dp),
                    textAlign = TextAlign.Center,
                    style = typography.headlineSmall
                )
            }
            Box (Modifier.weight(1f)) {
                Row(
                    modifier = Modifier
                        .clip (RoundedCornerShape (topEnd=8.dp, bottomEnd=8.dp))
                        .background (colorScheme.secondary)
                ) {
                    Text(
                        text = if (loMoves<0) ": -" else ": $loMoves",
                        modifier = Modifier.padding (end=8.dp),
                        style = typography.headlineSmall
                    )
                }
            }
        }

        Row (modifier=Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.Center) {
            Button (onClick=onReset) {
                Text(
                    text = if (isGameOver) "Play Again" else "Reset",
                    modifier = Modifier,
                    style = typography.headlineLarge)
            }
        }
    }
}
