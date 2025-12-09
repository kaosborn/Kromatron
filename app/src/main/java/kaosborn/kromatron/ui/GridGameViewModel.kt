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
    val baseColors = listOf(Crimson,RoyalBlue,LimeGreen,Cyan,Gold,MediumVioletRed,Brown)
    private var grid = GridGame(
        xSize = 5, ySize = 5,
        colors = baseColors.subList(0,5).toTypedArray())
    var score by mutableIntStateOf (0); private set
    var moves by mutableIntStateOf (0); private set
    var hiScore by mutableIntStateOf (0); private set
    var loMoves by mutableStateOf<Int?> (null); private set

    init { addPoints (grid.maxEnum) }

    val palette:List<Color> get() = grid.palette
    val board:List<List<Int>> get() = grid.board
    val xSize get() = grid.xSize
    val ySize get() = grid.ySize
    val isMonochrome:Boolean get() = grid.isConstant

    fun resetGame (vals:Settings) {
        if (vals.paletteSize<=baseColors.size)
            if (((vals.boardSize>0 && vals.paletteSize>0) || (vals.boardSize==0 && vals.paletteSize==0))) {
                grid = GridGame (xSize=vals.boardSize, ySize=vals.boardSize, colors=baseColors.subList(0,vals.paletteSize).toTypedArray())
                score = 0
                moves = 0
                addPoints (grid.maxEnum)
            }
    }

    fun resetGame() {
        grid.reset()
        score = 0
        moves = 0
        addPoints (grid.maxEnum)
    }

    fun pushMove (replacementColor:Int) {
        moves++
        addPoints (grid.flood4(replacementColor))
    }

    private fun addPoints (expansion:Int) {
        score += expansion * (expansion+1)
        if (hiScore < score)
            hiScore = score
        if (isMonochrome && (loMoves==null || loMoves!!>moves))
            loMoves = moves
    }
}
