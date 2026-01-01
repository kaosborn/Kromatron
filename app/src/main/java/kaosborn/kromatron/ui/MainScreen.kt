package kaosborn.kromatron.ui
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import kotlinx.coroutines.delay

@Composable
fun getAppWidth(): Dp {
    val windowWidth = LocalWindowInfo.current.containerSize.width
    with<Density,Unit>(receiver = LocalDensity.current) {
        return windowWidth.toDp()
    }
}

@Composable
fun MainScreen (vm:GridGameViewModel, showSettings:MutableState<Boolean>) {
    Column(
        modifier = Modifier.padding (top=8.dp),
        verticalArrangement = Arrangement.spacedBy (8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showSettings.value)
            SettingsDialog(
                vals = Settings (vm.xSize,vm.palette.size,vm.baseColors.size),
                onConfirm = { v -> showSettings.value = false; vm.resetGame (v) },
                onDismiss = { showSettings.value = false })

        Scoreboard (score=vm.score, moves=vm.moves, hiScore=vm.hiScore, loMoves=vm.loMoves, isGameOver=vm.isMonochrome, onReset = { vm.resetGame() })
        Gameboard (vm)
        Palette (vm, vm.root)
    }
}

@Composable
fun Scoreboard (score:Int, moves:Int, hiScore:Int, loMoves:Int, isGameOver:Boolean, onReset:() -> Unit) {
    Column (verticalArrangement=Arrangement.spacedBy (8.dp)) {
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

@Composable
fun Gameboard (vm:GridGameViewModel) {
    val boardWindowWidth = getAppWidth() - 4.dp
    var space = 0.dp
    var sizePerCell:Dp = boardWindowWidth / vm.xSize
    if (sizePerCell>=20.dp)
        if (sizePerCell<60.dp) {
            space = 2.dp
            sizePerCell = (boardWindowWidth - space * vm.xSize) / vm.xSize
        }
        else {
            space = 4.dp
            sizePerCell = 56.dp
        }

    val tick = if (vm.area<50) 50L else if (vm.area<200) 10L else 3L
    Column (modifier=Modifier, verticalArrangement=Arrangement.spacedBy(space)) {
        vm.board.forEachIndexed { y, row ->
            Row (modifier=Modifier, horizontalArrangement=Arrangement.spacedBy(space)) {
                row.forEachIndexed { x, colorIx ->
                    val rank = vm.rank[y][x]
                    val text = remember { mutableStateOf("") }
                    val color = remember { Animatable(Color.Gray) }
                    LaunchedEffect (vm.heartbeat) {
                        if (vm.moves==0)
                            color.animateTo (vm.palette[colorIx], animationSpec=tween(25))
                        else if (rank>0)
                            if (rank<=vm.fillSize) {
                                delay (rank*tick)
                                color.animateTo (targetValue=vm.palette[colorIx], animationSpec=tween(250))
                            }
                            else {
                                delay (rank*tick)
                                text.value = "$"
                                delay (tick*2)
                                text.value = "$$"
                                delay (tick*2)
                                text.value = "$"
                                delay (tick*2)
                                text.value = " "
                            }
                    }

                    Box (Modifier.background(color.value)) {
                        Text(
                            text = text.value,
                            modifier = Modifier.size(sizePerCell),
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Palette (vm:GridGameViewModel, root:Int?) {
    var width:Dp = (getAppWidth() - 4.dp) / vm.palette.size
    if (width > 60.dp)
        width = 60.dp

    Row (verticalAlignment=Alignment.CenterVertically) {
        for (i in 0..<vm.palette.size) {
            Box(
                modifier = Modifier.height (60.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { vm.pushMove (i) },
                    modifier = Modifier
                        .height (if (root==null || root==i) 30.dp else 60.dp)
                        .width (width),
                    enabled = root!=null && root!=i,
                    colors = ButtonDefaults.buttonColors (containerColor=vm.palette[i], disabledContainerColor=vm.palette[i])
                ) { }
            }
        }
    }
}
