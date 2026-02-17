package kaosborn.kromatron.ui
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun Gameboard (vm:GridGameViewModel) {
    val boardWindowWidth = getAppWidth() - 4.dp
    var space = 0.dp
    var sizePerCell:Dp = boardWindowWidth / vm.boardWidth
    if (sizePerCell>=20.dp)
        if (sizePerCell<60.dp) {
            space = 2.dp
            sizePerCell = (boardWindowWidth - space * vm.boardWidth) / vm.boardWidth
        }
        else {
            space = 4.dp
            sizePerCell = 56.dp
        }

    val tick = if (vm.boardArea<50) 50L else if (vm.boardArea<200) 10L else 3L
    Column (modifier=Modifier, verticalArrangement=Arrangement.spacedBy(space)) {
        vm.board.forEachIndexed { y, row ->
            Row (modifier=Modifier, horizontalArrangement=Arrangement.spacedBy(space)) {
                row.forEachIndexed { x, colorIx ->
                    val rank = vm.rank[y][x]
                    val color = remember { Animatable (Color.Gray) }
                    LaunchedEffect (vm.heartbeat) {
                        delay (rank*tick)
                        color.animateTo(
                            vm.palette[colorIx],
                            animationSpec = tween (200))
                    }
                    Text(
                        text = " ",
                        modifier = Modifier
                            .background (color.value)
                            .size (sizePerCell)
                            .wrapContentHeight (align=Alignment.CenterVertically),
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}
