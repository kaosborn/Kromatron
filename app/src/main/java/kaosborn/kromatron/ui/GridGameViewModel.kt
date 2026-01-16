package kaosborn.kromatron.ui
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import kaosborn.kromatron.GridGame
import kaosborn.kromatron.ui.theme.*

class GridGameViewModel() : ViewModel() {
    val baseColors = listOf(Crimson,RoyalBlue,LimeGreen,Cyan,Gold,MediumVioletRed,Brown)
    private var grid = GridGame (baseColors.subList(0,5), 5, 5)
    var score by mutableIntStateOf (0); private set
    var moves by mutableIntStateOf (0); private set
    var hiScore by mutableIntStateOf (0); private set
    var loMoves by mutableIntStateOf (-1); private set
    var heartbeat by mutableLongStateOf (0L); private set
    val palette:List<Color> get() = grid.palette
    val board:List<List<Int>> get() = grid.data
    val rank:List<List<Int>> get() = grid.rank
    val boardWidth get() = grid.xSize
    val area get() = grid.area
    val fillSize get() = grid.fillSize
    val isMonochrome get() = grid.isConstant
    val root:Int? get() = if (grid.isConstant) null else board[0][0]

    init {
        addPoints (grid.maxRank)
    }

    fun resetGame (vals:Settings) {
        if (vals.paletteSize<=baseColors.size)
            if (((vals.boardSize>0 && vals.paletteSize>0) || (vals.boardSize==0 && vals.paletteSize==0))) {
                if (vals.boardSize!=grid.ySize || vals.paletteSize!=palette.size) {
                    hiScore = 0
                    loMoves = -1
                }
                score = 0
                moves = 0
                grid = GridGame(
                    colors = baseColors.subList (0, vals.paletteSize),
                    xSize = vals.boardSize,
                    ySize = vals.boardSize
                )
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
        addPoints (grid.flood4 (colorIndex))
    }

    private fun addPoints (expansion:Int) {
        score += expansion * (expansion+1)
        if (hiScore < score)
            hiScore = score
        if (isMonochrome && (loMoves !in 0..moves))
            loMoves = moves
        heartbeat++
    }

    companion object {
        private const val PALETTE_KEY = "PALETTE"
        private const val SCORE_KEY = "SCORE"
        private const val MOVES_KEY = "MOVES"
        private const val HI_SCORE_KEY = "HI_SCORE"
        private const val LO_MOVES_KEY = "LO_MOVES"
        private const val DATA_KEY_PREFIX = "DATA_"
        private const val RANK_KEY_PREFIX = "RANK_"
    }

    fun saveState (prefs:SharedPreferences) {
        prefs.edit {
            putInt (SCORE_KEY, score)
            putInt (MOVES_KEY, moves)
            putInt (HI_SCORE_KEY, hiScore)
            putInt (LO_MOVES_KEY, loMoves)
            putString (PALETTE_KEY, grid.getPaletteLine())
            for (y in 0..<grid.ySize) {
                putString (DATA_KEY_PREFIX+y, grid.getDataLine(y))
                putString (RANK_KEY_PREFIX+y, grid.getRankLine(y))
            }
            remove (DATA_KEY_PREFIX+grid.ySize)
        }
    }

    fun loadState (prefs:SharedPreferences) {
        val paletteLine = prefs.getString (PALETTE_KEY, null)
        if (paletteLine!=null) {
            score = prefs.getInt (SCORE_KEY, 0)
            moves = prefs.getInt (MOVES_KEY, 0)
            hiScore = prefs.getInt (HI_SCORE_KEY, 0)
            loMoves = prefs.getInt (LO_MOVES_KEY,-1)
            grid = GridGame (paletteLine
                .split(" ")
                .map { it.toInt() }
                .map { Color(it) })
            for (y in 0..Int.MAX_VALUE) {
                val dataLine = prefs.getString (DATA_KEY_PREFIX+y, null)
                val rankLine = prefs.getString (RANK_KEY_PREFIX+y, null)
                if (dataLine==null || rankLine==null)
                    break
                grid.addRow(
                    dataLine.split(" ").map { it.toInt() },
                    rankLine.split(" ").map { it.toInt() })
            }
        }
        heartbeat++
    }
}
