package kaosborn.kromatron.ui
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kaosborn.kromatron.GridGame
import kaosborn.kromatron.ui.theme.*

class GridGameViewModel() : ViewModel() {
    val baseColors = listOf(Crimson,RoyalBlue,LimeGreen,Cyan,Gold,MediumVioletRed,Brown)
    private var grid = GridGame (5, 5, baseColors.subList(0,5))
    var score by mutableIntStateOf (0); private set
    var moves by mutableIntStateOf (0); private set
    var hiScore by mutableIntStateOf (0); private set
    var loMoves by mutableIntStateOf (-1); private set
    var heartbeat by mutableLongStateOf (0L); private set

    init { addPoints (grid.maxRank) }

    val palette:List<Color> get() = grid.palette
    val board:List<List<Int>> get() = grid.data
    val rank:List<List<Int>> get() = grid.rank
    val xSize get() = grid.xSize
    val area get() = grid.area
    val fillSize get() = grid.fillSize
    val isMonochrome get() = grid.isConstant
    val root:Int? get() = if (grid.isConstant) null else board[0][0]

    fun resetGame (vals:Settings) {
        if (vals.paletteSize<=baseColors.size)
            if (((vals.boardSize>0 && vals.paletteSize>0) || (vals.boardSize==0 && vals.paletteSize==0))) {
                grid = GridGame (xSize=vals.boardSize, ySize=vals.boardSize, colors=baseColors.subList(0,vals.paletteSize))
                score = 0
                moves = 0
                addPoints (grid.maxRank)
            }
    }

    fun resetGame() {
        grid.reset()
        score = 0
        moves = 0
        addPoints (grid.maxRank)
    }

    fun pushMove (colorIndex:Int) {
        moves++
        addPoints (grid.flood4(colorIndex))
    }

    private fun addPoints (expansion:Int) {
        score += expansion * (expansion+1)
        if (hiScore < score)
            hiScore = score
        if (isMonochrome && (loMoves !in 0..moves))
            loMoves = moves
        heartbeat++
    }
}
