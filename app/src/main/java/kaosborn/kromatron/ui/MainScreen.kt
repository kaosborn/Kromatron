package kaosborn.kromatron.ui
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign

@Composable
fun getAppWidth(): Dp {
    val windowWidth = LocalWindowInfo.current.containerSize.width
    with<Density,Unit>(receiver = LocalDensity.current) {
        return windowWidth.toDp()
    }
}

@Composable
fun MainScreen (vm:GridGameViewModel, showSettings:MutableState<Boolean>) {
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

    Column(
        modifier = Modifier.padding (top=8.dp),
        verticalArrangement = Arrangement.spacedBy (8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (showSettings.value)
            SettingsDialog(
                vals = Settings (vm.boardWidth,vm.palette.size,vm.baseColors.size),
                onConfirm = { v -> showSettings.value = false; vm.resetGame (v) },
                onDismiss = { showSettings.value = false })

        Scoreboard (score=vm.score, moves=vm.moves, hiScore=vm.hiScore, loMoves=vm.loMoves, isGameOver=vm.isBoardMonochrome, onReset = { vm.resetGame() }, onUndo = { vm.popMove() } )

        Column (modifier=Modifier, verticalArrangement=Arrangement.spacedBy(4.dp)) {
            vm.board.forEach { row ->
                Row (modifier=Modifier, horizontalArrangement=Arrangement.spacedBy(space)) {
                    row.forEach { colorIx ->
                        Text(
                            text = " ",
                            modifier = Modifier
                                .background (vm.palette[colorIx])
                                .size (sizePerCell),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        Palette (vm, vm.palette, vm.root)
    }
}
