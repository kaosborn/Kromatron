package kaosborn.kromatron
import org.junit.Test
import kotlin.test.assertTrue
import kaosborn.kromatron.ui.theme.*

class GridGameGenerators {
    companion object {
        val COLORS = listOf (Crimson, RoyalBlue, LimeGreen, Cyan, Gold, MediumVioletRed, Brown)
        val ICON_DATA = listOf(
            listOf ( 3,0,0,3,4 ),
            listOf ( 4,1,0,3,1 ),
            listOf ( 1,1,3,3,1 ),
            listOf ( 2,1,1,4,4 ),
            listOf ( 2,1,4,4,0 ))
    }

    @Test
    fun genAppIconForeground() {
        val gg = GridGame (COLORS.subList(0,5), ICON_DATA)
        val xml = gg.genIconVectorForeground()
        assertTrue (xml.endsWith("</vector>"))
    }

    @Test
    fun genAppIconBackground() {
        val gg = GridGame (COLORS.subList(0,5), ICON_DATA)
        val xml = gg.genIconVectorBackground()
        assertTrue (xml.endsWith("</vector>"))
    }
}
