package kaosborn.kromatron.ui
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*

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
                vals = Settings (vm.boardWidth,vm.palette.size,vm.baseColors.size),
                onConfirm = { v -> showSettings.value = false; vm.resetGame (v) },
                onDismiss = { showSettings.value = false })

        Scoreboard (score=vm.score, moves=vm.moves, hiScore=vm.hiScore, loMoves=vm.loMoves, isGameOver=vm.isBoardMonochrome, onReset = { vm.resetGame() }, onUndo = { vm.popMove() } )
        Gameboard (vm)
        Palette (vm, vm.palette, vm.root)
    }
}
