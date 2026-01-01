package kaosborn.kromatron
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class GridGame() {
    private val _palette = mutableStateListOf<Color>()
    private val _data = mutableStateListOf<MutableList<Int>>()
    private val _rank = mutableStateListOf<MutableList<Int>>()
    var area = 0; private set
    var xSize = 0; private set
    var maxRank = 0; private set
    var fillSize = 0; private set
    val palette:List<Color> get() = _palette
    val data:List<List<Int>> get() = _data
    val rank:List<List<Int>> get() = _rank
    val ySize get() = _data.size
    val isConstant get() = maxRank==area
    fun getPaletteLine() = _palette.map { it.toArgb() }.joinToString(" ")
    fun getDataLine(y:Int) = _data[y].joinToString(" ")
    fun getRankLine(y:Int) = _rank[y].joinToString(" ")

    constructor (boardValues:Array<IntArray>, colors:List<Color>): this() {
        if (boardValues.isNotEmpty()) {
            for (r in boardValues) {
                if (r.isEmpty() || r.any { it<0 || (it>0 && it>=colors.size) })
                    throw IllegalArgumentException ("Illegal value")
                _data.add (r.toMutableList())
                _rank.add (MutableList(r.size) { 0 })
                this.area += r.size
                if (this.xSize < r.size)
                    this.xSize = r.size
            }
            _palette.addAll (colors)
            expand4()
        }
    }

    constructor (xSize:Int, ySize:Int, colors:List<Color>): this() {
        if (xSize<0 || ySize<0 || (xSize==0 && ySize>0))
            throw IllegalArgumentException ("Illegal value")
        _palette.addAll (colors)
        for (y in 0..<ySize) {
            _data.add (MutableList(xSize) { 0 })
            _rank.add (MutableList(xSize) { 0 })
        }
        this.xSize = xSize
        this.area = xSize*ySize
        reset()
    }

    constructor (colors:List<Color>): this() {
        _palette.addAll (colors)
    }

    fun addRow (dataRow:List<Int>, rankRow:List<Int>) {
        _data.add (dataRow.toMutableList())
        _rank.add (rankRow.toMutableList())
        area += dataRow.size
        if (xSize < dataRow.size)
            xSize = dataRow.size
        val max = rankRow.max()
        if (maxRank < max)
            maxRank = max
    }

    fun reset() {
        if (ySize>0 && _palette.isNotEmpty()) {
            for (y in _data.indices)
                for (x in _data[y].indices) {
                    _data[y][x] = (0..<_palette.size).random()
                    _rank[y][x] = 0
                }
            fillSize = 0
            maxRank = 0
            addSalt()
            expand4()
        }
    }

    private fun addSalt() {
        if (ySize>2 && xSize>2)
            for (k in 1..(xSize-2)*(ySize-2)) {
                val x = (1..xSize-2).random()
                val y = (1..ySize-2).random()
                if ((0..1).random()==0) {
                    if (_data[y-1][x]==_data[y+1][x])
                        _data[y][x] = _data[y-1][x]
                }
                else if (_data[y][x-1]==_data[y][x+1])
                    _data[y][x] = _data[y][x-1]
            }
    }

    fun flood4 (colorIndex:Int): Int {
        if (ySize!=0) {
            val targetColorIndex = _data[0][0]
            fun flood4R (x:Int, y:Int) {
                when (_data[y][x]) {
                    colorIndex ->
                        expand4 (x,y)
                    targetColorIndex -> {
                        _data[y][x] = colorIndex
                        if (x>0)
                            flood4R (x-1,y)
                        if (y>0 && x<_data[y-1].size)
                            flood4R (x,y-1)
                        if (y+1<_data.size && x<_data[y+1].size)
                            flood4R (x,y+1)
                        if (x+1<_data[y].size)
                            flood4R (x+1,y)
                    }
                }
            }
            fillSize = maxRank
            flood4R (0,0)
        }
        return maxRank - fillSize
    }

    private fun expand4 (x:Int=0, y:Int=0) {
        val colorIndex = _data[y][x]
        fun expand4R (x:Int, y:Int) {
            if (_rank[y][x]==0) {
                maxRank++
                _rank[y][x] = maxRank
                if (x>0 && _data[y][x-1]==colorIndex)
                    expand4R (x-1,y)
                if (y>0 && x<_data[y-1].size && _data[y-1][x]==colorIndex)
                    expand4R (x,y-1)
                if (y+1<_data.size && x<_data[y+1].size && _data[y+1][x]==colorIndex)
                    expand4R (x,y+1)
                if (x+1<_data[y].size && _data[y][x+1]==colorIndex)
                    expand4R (x+1,y)
            }
        }
        expand4R (x,y)
    }

    fun isEqual (rval:Array<IntArray>): Boolean {
        if (rval.size!=_data.size)
            return false
        for (y in _data.indices) {
            if (_data[y].size!=rval[y].size)
                return false
            for (x in _data[y].indices)
                if (_data[y][x]!=rval[y][x])
                    return false
        }
        return true
    }

    override fun toString(): String {
        var result = ""
        for (y in 0..<_data.size) {
            if (y>0)
                result += "\n"
            for (x in 0..<_data[y].size) {
                if (x>0)
                    result += " "
                result += _data[y][x].toString()
            }
        }
        return result
    }
}
