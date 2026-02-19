package kaosborn.kromatron

data class IconVectorValues (val xSize:Int, val ySize:Int) {
    val tileWidth = 60/xSize
    val tileHeight = 60/ySize
    val boardWidth = tileWidth*xSize
    val boardHeight = tileHeight*ySize
    val leftBorder = 54-boardWidth/2
    val topBorder = 54-boardHeight/2
    fun genBackgroundPathData() = "0,0h108v108h-108z"
    fun genForegroundPathData() = "${leftBorder-2},${topBorder-2}h${boardWidth+4}v${boardHeight+4}h${-(boardWidth+4)}z"
    fun genSpanPathData (spanStart:Int, spanStop:Int, y:Int) =
        "${leftBorder+spanStart*tileWidth},${topBorder+y*tileHeight+tileHeight/2}" +
        "h${(spanStop-spanStart)*tileWidth}"
}
