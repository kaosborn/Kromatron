package kaosborn.kromatron
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

class GridGame() {
    private val _data = mutableStateListOf<MutableList<Int>>()
    private val _rank = mutableStateListOf<MutableList<Int>>()
    private val _palette = mutableStateListOf<Color>()
    private val _moveStack = mutableStateListOf<MoveNode>()
    var gridArea = 0; private set
    var monoArea = 0; private set
    var fillArea = 0; private set
    var xSize = 0; private set
    val ySize get() = _data.size
    val data get() = _data
    val rank get() = _rank
    val palette get() = _palette
    val moveCount get() = _moveStack.size
    val isConstant get() = monoArea==gridArea
    fun getDataLine (y:Int) = _data[y].joinToString(" ")
    fun getRankLine (y:Int) = _rank[y].joinToString(" ")
    fun getPaletteLine() = _palette.map { it.toArgb() }.joinToString(" ")
    fun getMovesLine() = _moveStack.joinToString(" ") { "${it.colorIndex} ${it.fillArea}" }

    constructor (colors:List<Color>, boardValues:List<List<Int>>): this() {
        if (boardValues.isNotEmpty()) {
            for (r in boardValues) {
                if (r.isEmpty() || r.any { it<0 || (it>0 && it>=colors.size) })
                    throw IllegalArgumentException ("Illegal value")
                _data.add (r.toMutableList())
                _rank.add (MutableList(r.size) { 0 })
                this.gridArea += r.size
                if (this.xSize < r.size)
                    this.xSize = r.size
            }
            _palette.addAll (colors)
            expand4()
        }
    }

    constructor (colors:List<Color>, xSize:Int, ySize:Int): this() {
        if (xSize<0 || ySize<0 || (xSize==0 && ySize>0))
            throw IllegalArgumentException ("Illegal value")
        _palette.addAll (colors)
        for (y in 0..<ySize) {
            _data.add (MutableList(xSize) { 0 })
            _rank.add (MutableList(xSize) { 0 })
        }
        this.xSize = xSize
        this.gridArea = xSize*ySize
        reset()
    }

    constructor (colors:List<Color>): this() {
        _palette.addAll (colors)
    }

    fun addRow (dataRow:List<Int>, rankRow:List<Int>) {
        _data.add (dataRow.toMutableList())
        _rank.add (rankRow.toMutableList())
        gridArea += dataRow.size
        if (xSize < dataRow.size)
            xSize = dataRow.size
        val max = rankRow.max()
        if (monoArea < max)
            monoArea = max
    }

    fun addMoves (moveHistoryLine:String?) {
        if (! moveHistoryLine.isNullOrEmpty())
            _moveStack.addAll (moveHistoryLine
                .split (' ')
                .map { it.toInt() }
                .chunked (2)
                .map { c -> MoveNode (c[0], c[1]) })
    }

    fun reset() {
        if (ySize>0 && _palette.isNotEmpty()) {
            for (r in _data)
                for (x in r.indices)
                    r[x] = (0..<_palette.size).random()
            for (r in _rank)
                for (x in r.indices)
                    r[x] = 0
            _moveStack.clear()
            fillArea = 0
            monoArea = 0
            addSalt()
            expand4()
        }
    }

    fun reclaim(): Int {
        val monoArea0 = monoArea
        if (_moveStack.isNotEmpty()) {
            val colorIndex = _moveStack[_moveStack.lastIndex].colorIndex
            monoArea = _moveStack[_moveStack.lastIndex].fillArea
            for ((y,r) in _rank.withIndex())
                for (x in 0..<r.size)
                    if (r[x] > monoArea)
                        r[x] = 0
                    else if (r[x] > 0)
                        _data[y][x] = colorIndex
            _moveStack.removeAt (_moveStack.lastIndex)
        }
        return monoArea0 - monoArea
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

    fun calcHint (depth:Int, getPointsFromArea:(Int) -> Int): Int {
        fun calcHintR (level:Int): Pair<Int,Int> {
            var topPointT = -1
            var topPoints = 0
            var topIndex = 0
            for (colorIndex in palette.indices)
                if (colorIndex!=_data[0][0]) {
                    val points = getPointsFromArea (flood4 (colorIndex))
                    if (points!=0) {
                        val pointT = if (isConstant || level==0) points else points + calcHintR(level-1).first
                        if (topPointT < pointT || (topPointT == pointT && topPoints <= points)) {
                            topPointT = pointT
                            topPoints = points
                            topIndex = colorIndex
                        }
                    }
                    reclaim()
                }
            return Pair (topPointT, topIndex)
        }
        return if (isConstant) -1 else calcHintR(depth).second
    }

    fun flood4 (colorIndex:Int): Int {
        if (ySize!=0) {
            val targetColorIndex = _data[0][0]
            _moveStack.add (MoveNode (targetColorIndex, monoArea))
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
            fillArea = monoArea
            flood4R (0,0)
        }
        return monoArea - fillArea
    }

    private fun expand4 (x:Int=0, y:Int=0) {
        val colorIndex = _data[y][x]
        fun expand4R (x:Int, y:Int) {
            if (_rank[y][x]==0) {
                monoArea++
                _rank[y][x] = monoArea
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

    fun isEqual (rval:List<List<Int>>): Boolean {
        if (rval.size!=_data.size)
            return false
        for (y in _data.indices)
            if (_data[y]!=rval[y])
                return false
        return true
    }

    override fun toString(): String {
        var result = ""
        _data.forEachIndexed { y, r ->
            if (y>0)
                result += "\n"
            result += r.joinToString (" ")
        }
        return result
    }
}
