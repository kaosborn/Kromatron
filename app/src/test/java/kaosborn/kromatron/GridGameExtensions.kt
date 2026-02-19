package kaosborn.kromatron
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kaosborn.kromatron.ui.theme.Gold4
import kaosborn.kromatron.ui.theme.Gray

fun Color.toHex6UString() = with (toArgb()) {
    String.format ("%02X%02X%02X", (this shr 16) and 0xFF, (this shr 8) and 0xFF, this and 0xFF)
}

private fun genIconVectorHeader() =
    "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
    "\n<vector" +
    "\n    xmlns:android=\"http://schemas.android.com/apk/res/android\"" +
    "\n    android:width=\"108dp\"" +
    "\n    android:height=\"108dp\"" +
    "\n    android:viewportWidth=\"108\"" +
    "\n    android:viewportHeight=\"108\">"

fun GridGame.genIconVectorBackground() = with (IconVectorValues (xSize,ySize)) {
    genIconVectorHeader() +
    "\n    <path" +
    "\n        android:fillColor=\"#${Gold4.toHex6UString()}\"" +
    "\n        android:pathData=\"M${genBackgroundPathData()}\" />" +
    "\n    <path" +
    "\n        android:fillColor=\"#${Gray.toHex6UString()}\"" +
    "\n        android:pathData=\"M${genForegroundPathData()}\" />" +
    "\n</vector>"
}

fun GridGame.genIconVectorForeground() = with (IconVectorValues (xSize,ySize)) {
    fun genSpanPath (spanStart:Int, spanStop:Int, y:Int, spanColorIndex:Int) =
        "\n    <path" +
        "\n        android:pathData=\"M${genSpanPathData(spanStart,spanStop,y)}\"" +
        "\n        android:strokeWidth=\"$tileHeight\"" +
        "\n        android:strokeColor=\"#${palette[spanColorIndex].toHex6UString()}\" />"
    var paths = ""
    for ((y,r) in data.withIndex()) {
        var spanColorIndex = -1
        var spanStart = -1
        for ((x,tileColorIndex) in r.withIndex())
            if (spanColorIndex!=tileColorIndex) {
                if (spanStart>=0)
                    paths += genSpanPath (spanStart, x, y, spanColorIndex)
                spanColorIndex = tileColorIndex
                spanStart = x
            }
        paths += genSpanPath (spanStart, r.size, y, spanColorIndex)
    }
    "${genIconVectorHeader()}$paths\n</vector>"
}
