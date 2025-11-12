package kaosborn.kromatron
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color

class GridGame() {
    private val _palette = mutableStateListOf<Color>()
    private val _board = mutableStateListOf<MutableList<Int>>()
    private val enums:MutableList<IntArray> = mutableListOf()

    var xSize = 0; private set
    var ySize = 0; private set
    var maxEnum = 0; private set
    private var area = 0

    val palette:List<Color> get() = _palette
    val board:List<List<Int>> get() = _board
    val isConstant get() = maxEnum==area

    constructor (xSize:Int, ySize:Int, colors:Array<Color>) : this() {
        this._palette.addAll (colors)
        this.xSize = xSize
        this.ySize = ySize
        this.area = xSize*ySize
        for (y in 0..<ySize) {
            this._board.add ((List(xSize) { (0..<_palette.size).random() }).toMutableList())
            this.enums.add (IntArray(xSize))
        }
        if (area>0)
            crawl4 (0,0, this._board[0][0])
    }

    fun reset() {
        for (y in _board.indices)
            for (x in _board[y].indices) {
                _board[y][x] = (0..<_palette.size).random()
                enums[y][x] = 0
            }
        maxEnum = 0
        if (area>0)
            crawl4 (0,0, _board[0][0])
    }

    fun flood4 (x:Int, y:Int, replacementColor:Int): Int {
        if (x<0 || y<0 || y>=_board.size || x>=_board[y].size)
            return -1
        val fillSize = maxEnum
        flood4R (x,y, _board[y][x], replacementColor)
        return maxEnum - fillSize
    }

    private fun flood4R (x:Int, y:Int, oldColor:Int, newColor:Int) {
        when (_board[y][x]) {
            newColor ->
                crawl4 (x,y,newColor)
            oldColor -> {
                _board[y][x] = newColor
                if (x>0)
                    flood4R (x-1,y,oldColor,newColor)
                if (y>0 && x<_board[y-1].size)
                    flood4R (x,y-1,oldColor,newColor)
                if (y+1<_board.size && x<_board[y+1].size)
                    flood4R (x,y+1,oldColor,newColor)
                if (x+1<_board[y].size)
                    flood4R (x+1,y,oldColor,newColor)
            }
        }
    }

    private fun crawl4 (x:Int, y:Int, color:Int) {
        if (enums[y][x]!=0)
            return
        maxEnum++
        enums[y][x] = maxEnum
        if (x>0 && _board[y][x-1]==color)
            crawl4 (x-1,y,color)
        if (y>0 && x<_board[y-1].size && _board[y-1][x]==color)
            crawl4 (x,y-1,color)
        if (y+1<_board.size && x<_board[y+1].size && _board[y+1][x]==color)
            crawl4 (x,y+1,color)
        if (x+1<_board[y].size && _board[y][x+1]==color)
            crawl4 (x+1,y,color)
    }

    fun isEqual (rval:MutableList<IntArray>): Boolean {
        if (rval.size!=_board.size)
            return false
        for (y in _board.indices) {
            val dataRow = _board[y]
            if (dataRow.size!=rval[y].size)
                return false
            for (x in dataRow.indices)
                if (dataRow[x]!=rval[y][x])
                    return false
        }
        return true
    }

    override fun toString(): String {
        var result = ""
        var y = 0
        val ySizeActual = _board.size
        while (true) {
            val xSizeActual = _board[y].size
            var x = 0
            while (true) {
                result += _board[y][x].toString()
                x++
                if (x>=xSizeActual)
                    break
                result += " "
            }
            y++
            if (y>=ySizeActual)
                break
            result += "\n"
        }
        return result
    }
}
