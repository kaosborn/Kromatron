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
    var hiScore by mutableIntStateOf (0); private set
    var loMoves by mutableIntStateOf (-1); private set
    var hint by mutableIntStateOf (-1); private set
    var heartbeat by mutableLongStateOf (0L); private set
    val board get() = grid.data
    val rank get() = grid.rank
    val palette get() = grid.palette
    val moves get() = grid.moveCount
    val boardWidth get() = grid.xSize
    val boardArea get() = grid.gridArea
    val boardGauge get() = (14-palette.size-(boardWidth+2)/4).coerceAtLeast(2).coerceAtMost(5)
    val isBoardMonochrome get() = grid.isConstant
    val root get() = if (grid.isConstant) null else board[0][0]

    init {
        addPoints (grid.monoArea)
    }

    fun resetGame (vals:Settings) {
        if (vals.paletteSize<=baseColors.size)
            if (((vals.boardSize>0 && vals.paletteSize>0) || (vals.boardSize==0 && vals.paletteSize==0))) {
                if (vals.boardSize!=grid.ySize || vals.paletteSize!=palette.size) {
                    hiScore = 0
                    loMoves = -1
                }
                score = 0
                grid = GridGame(
                    colors = baseColors.subList (0, vals.paletteSize),
                    xSize = vals.boardSize,
                    ySize = vals.boardSize)
                addPoints (grid.monoArea)
            }
    }

    fun resetGame() {
        score = 0
        grid.reset()
        addPoints (grid.monoArea)
    }

    fun pushMove (colorIndex:Int) {
        addPoints (grid.flood4 (colorIndex))
    }

    fun popMove() {
        hint = -1
        score -= getPoints (grid.reclaim())
        heartbeat++
    }

    fun calcHint (depth:Int=0) {
        hint = grid.calcHint (depth, ::getPoints)
    }

    fun getPoints (area:Int) = area * (area+1)

    private fun addPoints (expansion:Int) {
        score += getPoints (expansion)
        if (hiScore < score)
            hiScore = score
        if (isBoardMonochrome && (loMoves !in 0..moves))
            loMoves = moves
        hint = -1
        heartbeat++
    }

    companion object {
        private const val PALETTE_KEY = "PALETTE"
        private const val SCORE_KEY = "SCORE"
        private const val MOVES_KEY = "MOVE_STACK"
        private const val HI_SCORE_KEY = "HI_SCORE"
        private const val LO_MOVES_KEY = "LO_MOVES"
        private const val DATA_KEY_PREFIX = "DATA_"
        private const val RANK_KEY_PREFIX = "RANK_"
        private const val HINT_KEY = "HINT"
    }

    fun saveState (prefs:SharedPreferences) {
        prefs.edit {
            putInt (SCORE_KEY, score)
            putInt (HI_SCORE_KEY, hiScore)
            putInt (LO_MOVES_KEY, loMoves)
            putString (PALETTE_KEY, grid.getPaletteLine())
            for (y in 0..<grid.ySize) {
                putString (DATA_KEY_PREFIX+y, grid.getDataLine(y))
                putString (RANK_KEY_PREFIX+y, grid.getRankLine(y))
            }
            remove (DATA_KEY_PREFIX+grid.ySize)
            putString (MOVES_KEY, grid.getMovesLine())
            putInt (HINT_KEY, hint)
        }
    }

    fun loadState (prefs:SharedPreferences) {
        val paletteLine = prefs.getString (PALETTE_KEY, null)
        if (paletteLine!=null) {
            score = prefs.getInt (SCORE_KEY, 0)
            hiScore = prefs.getInt (HI_SCORE_KEY, 0)
            loMoves = prefs.getInt (LO_MOVES_KEY,-1)
            grid = GridGame (paletteLine
                .split (' ')
                .map { it.toInt() }
                .map { Color(it) })
            for (y in 0..Int.MAX_VALUE) {
                val dataLine = prefs.getString (DATA_KEY_PREFIX+y, null)
                val rankLine = prefs.getString (RANK_KEY_PREFIX+y, null)
                if (dataLine==null || rankLine==null)
                    break
                grid.addRow(
                    dataLine.split(' ').map { it.toInt() },
                    rankLine.split(' ').map { it.toInt() })
            }
            grid.addMoves (prefs.getString (MOVES_KEY, ""))
            hint = prefs.getInt (HINT_KEY, -1)
        }
        heartbeat++
    }
}
