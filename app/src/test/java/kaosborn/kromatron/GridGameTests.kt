package kaosborn.kromatron
import androidx.compose.ui.graphics.Color
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GridGameTests {
    @Test
    fun emptyNoColors() {
        val colors0:Array<Color> = emptyArray()
        val gg = GridGame (0,0,colors0)
        assertEquals (0, gg.xSize)
        assertEquals (0, gg.ySize)
        assertEquals ("", gg.toString())
    }

    @Test
    fun emptyHasColors() {
        val colors2:Array<Color> = arrayOf(Color.Red,Color.Blue)
        val gg = GridGame (0,0,colors2)
        assertEquals (0, gg.xSize)
        assertEquals (0, gg.ySize)
        assertEquals ("", gg.toString())
        assertTrue (gg.isConstant)
    }

    @Test
    fun noWidth() {
        val colors2 = arrayOf(Color.Red,Color.Blue)
        val exception = assertFailsWith<IllegalArgumentException> {
            val gg = GridGame (0,3,colors2)
        }
        assertEquals ("Illegal value", exception.message)
    }

    @Test
    fun noHeight() {
        val colors2 = arrayOf(Color.Red,Color.Blue)
        val gg = GridGame (2,0,colors2)
        assertEquals ("", gg.toString())
    }

    @Test
    fun grid2by3NoColor() {
        val colors0:Array<Color> = emptyArray()
        val expected1 = "0 0\n0 0\n0 0"

        val gg = GridGame (2,3,colors0)
        assertEquals (2, gg.xSize)
        assertEquals (3, gg.ySize)
        assertEquals (expected1, gg.toString())
        assertFalse (gg.isConstant)
    }

    @Test
    fun grid1by2() {
        val colors1 = arrayOf(Color.Red)
        val expected1 = "0\n0"

        val gg = GridGame(1,2,colors1)
        assertEquals (1, gg.xSize)
        assertEquals (2, gg.ySize)
        assertEquals (2, gg.maxRank)
        assertEquals (expected1, gg.toString())
    }

    @Test
    fun grid2by1() {
        val colors1 = arrayOf(Color.Red)
        val expected1 = "0 0"

        val gg = GridGame(2,1,colors1)
        assertEquals (2, gg.xSize)
        assertEquals (1, gg.ySize)
        assertEquals (2, gg.maxRank)
        assertEquals (expected1, gg.toString())
    }

    @Test
    fun grid2by3() {
        val colors2 = arrayOf(Color.Red)
        val expected1 = "0 0\n0 0\n0 0"

        val gg = GridGame(2,3,colors2)
        assertEquals (2, gg.xSize)
        assertEquals (3, gg.ySize)
        assertEquals (expected1, gg.toString())
        assertTrue (gg.isConstant)
    }

    @Test
    fun gridByValues() {
        val colors2 = arrayOf(Color.Red,Color.Blue)
        val source = arrayOf (
            intArrayOf ( 1,0 ),
            intArrayOf ( 1,1 ),
            intArrayOf ( 1,0 ))
        val expected1 = "1 0\n1 1\n1 0"

        val gg = GridGame (source, colors2)
        assertEquals (expected1, gg.toString())
        assertEquals (4, gg.maxRank)
    }

    @Test
    fun floodEmptyNoColors() {
        val colors0:Array<Color> = emptyArray()
        val source:Array<IntArray> = emptyArray()

        val gg = GridGame (source, colors0)
        assertEquals ("", gg.toString())

        gg.flood4 (0)
        assertEquals ("", gg.toString())
    }

    @Test
    fun floodEmptyHasColors() {
        val colors2 = arrayOf(Color.Red,Color.Blue)
        val source:Array<IntArray> = emptyArray()

        val gg = GridGame (source, colors2)
        assertEquals ("", gg.toString())
        gg.flood4 (0)
        assertTrue (gg.isConstant)
    }

    @Test
    fun flood0() {
        val colors2 = arrayOf(Color.Red,Color.Blue)
        val source = arrayOf(intArrayOf())
        val exception = assertFailsWith<IllegalArgumentException> {
            val gg = GridGame (source, colors2)
        }
        assertEquals ("Illegal value", exception.message)
    }

    @Test
    fun flood1() {
        val colors2 = arrayOf(Color.Red,Color.Blue)
        val source = arrayOf (intArrayOf (1))

        val gg = GridGame (source, colors2)
        assertEquals ("1", gg.toString())
        assertEquals(1, gg.maxRank)
        assertTrue (gg.isConstant)

        gg.flood4 (0)
        assertEquals ("0", gg.toString())
        assertEquals (1, gg.maxRank)
    }

    @Test
    fun flood2() {
        val colors3 = arrayOf(Color.Red,Color.Blue,Color.Green)
        val source = arrayOf (
            intArrayOf ( 1,0,0,1,1 ),
            intArrayOf ( 1,1,1,0,1 ),
            intArrayOf ( 1,0,1,1,1 ),
            intArrayOf ( 2,1,1,0,2 ))
        val expected1 = "2 0 0 2 2\n2 2 2 0 2\n2 0 2 2 2\n2 2 2 0 2"
        val expected2 = "1 0 0 1 1\n1 1 1 0 1\n1 0 1 1 1\n1 1 1 0 1"
        val expected3 = "0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0"

        val gg = GridGame (source, colors3)
        assertTrue (gg.isEqual(source))
        assertEquals (13, gg.maxRank)

        gg.flood4 (2)
        assertEquals (expected1, gg.toString())
        assertEquals (15, gg.maxRank)

        gg.flood4 (1)
        assertEquals (expected2, gg.toString())
        assertEquals (15, gg.maxRank)
        assertFalse (gg.isConstant)

        gg.flood4 (0)
        assertEquals (expected3, gg.toString())
        assertEquals (20, gg.maxRank)
        assertTrue (gg.isConstant)
    }
}
