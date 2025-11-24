package kaosborn.kromatron
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color

class GridGame() {
    private val _palette = mutableStateListOf<Color>()
    private val _board = mutableStateListOf<MutableList<Int>>()
    private val _enums:MutableList<IntArray> = mutableListOf()
    private var area = 0
    var xSize = 0; private set
    var ySize = 0; private set
    var maxEnum = 0; private set
    val palette:List<Color> get() = _palette
    val board:List<List<Int>> get() = _board
    val isConstant get() = maxEnum==area

    constructor (boardValues:Array<IntArray>, colors:Array<Color>) : this() {
        if (boardValues.isNotEmpty()) {
            for (r in boardValues) {
                if (r.isEmpty() || r.any { it<0 || (it>0 && it>=colors.size) })
                    throw IllegalArgumentException ("Illegal value")
                _board.add (r.toMutableList())
                _enums.add (IntArray(r.size))
                this.area += r.size
                if (this.xSize < r.size)
                    this.xSize = r.size
            }
            this.ySize = boardValues.size
            _palette.addAll (colors)
            crawl4()
        }
    }

    constructor (xSize:Int, ySize:Int, colors:Array<Color>) : this() {
        if (xSize<0 || ySize<0 || (xSize==0 && ySize>0))
            throw IllegalArgumentException ("Illegal value")
        _palette.addAll (colors)
        for (y in 0..<ySize) {
            _board.add (MutableList(xSize) { 0 })
            _enums.add (IntArray(xSize))
        }
        this.xSize = xSize
        this.ySize = ySize
        this.area = xSize*ySize
        reset()
    }

    fun reset() {
        if (ySize>0 && _palette.isNotEmpty()) {
            for (y in _board.indices)
                for (x in _board[y].indices) {
                    _board[y][x] = (0..<_palette.size).random()
                    _enums[y][x] = 0
                }
            maxEnum = 0
            crawl4()
        }
    }

    fun flood4 (newColor:Int): Int {
        var fillSize = 0
        if (ySize != 0) {
            val oldColor = _board[0][0]
            fun flood4R (x:Int, y:Int) {
                when (_board[y][x]) {
                    newColor ->
                        crawl4 (x,y)
                    oldColor -> {
                        _board[y][x] = newColor
                        if (x>0)
                            flood4R (x-1,y)
                        if (y>0 && x<_board[y-1].size)
                            flood4R (x,y-1)
                        if (y+1<_board.size && x<_board[y+1].size)
                            flood4R (x,y+1)
                        if (x+1<_board[y].size)
                            flood4R (x+1,y)
                    }
                }
            }
            fillSize = maxEnum
            flood4R (0,0)
        }
        return maxEnum - fillSize
    }

    private fun crawl4 (x:Int=0, y:Int=0) {
        val color = _board[y][x]
        fun crawl4R (x:Int, y:Int) {
            if (_enums[y][x]==0) {
                maxEnum++
                _enums[y][x] = maxEnum
                if (x>0 && _board[y][x-1]==color)
                    crawl4R (x-1,y)
                if (y>0 && x<_board[y-1].size && _board[y-1][x]==color)
                    crawl4R (x,y-1)
                if (y+1<_board.size && x<_board[y+1].size && _board[y+1][x]==color)
                    crawl4R (x,y+1)
                if (x+1<_board[y].size && _board[y][x+1]==color)
                    crawl4R (x+1,y)
            }
        }
        crawl4R (x,y)
    }

    fun isEqual (rval:Array<IntArray>): Boolean {
        if (rval.size!=_board.size)
            return false
        for (y in _board.indices) {
            if (_board[y].size!=rval[y].size)
                return false
            for (x in _board[y].indices)
                if (_board[y][x]!=rval[y][x])
                    return false
        }
        return true
    }

    override fun toString(): String {
        var result = ""
        for (y in 0..<_board.size) {
            if (y>0)
                result += "\n"
            for (x in 0..<_board[y].size) {
                if (x>0)
                    result += " "
                result += _board[y][x].toString()
            }
        }
        return result
    }
}
