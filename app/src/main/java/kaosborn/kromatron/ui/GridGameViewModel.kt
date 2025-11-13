package kaosborn.kromatron.ui
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kaosborn.kromatron.GridGame
import kaosborn.kromatron.ui.theme.*

class GridGameViewModel() : ViewModel() {
    private var grid = GridGame(
        xSize = 5, ySize = 5,
        colors = arrayOf(Crimson,RoyalBlue,LimeGreen,Cyan,Gold))
    var moves by mutableIntStateOf (0); private set
    var score by mutableIntStateOf (0); private set
    var hiScore by mutableIntStateOf (0); private set
    var loMoves by mutableStateOf<Int?> (null); private set

    init { addPoints (grid.maxEnum) }

    val palette:List<Color> get() = grid.palette
    val board:List<List<Int>> get() = grid.board
    val xSize get() = grid.xSize
    val isMonochrome:Boolean get() = grid.isConstant

    fun resetGame() {
        grid.reset()
        score = 0
        moves = 0
        addPoints (grid.maxEnum)
    }

    fun pushMove (replacementColor:Int) {
        moves++
        addPoints (grid.flood4 (0,0,replacementColor))
    }

    private fun addPoints (expansion:Int) {
        score += expansion * (expansion+1)
        if (hiScore<score)
            hiScore = score
        if (isMonochrome && (loMoves==null || loMoves!!>moves))
            loMoves = moves
    }
}
