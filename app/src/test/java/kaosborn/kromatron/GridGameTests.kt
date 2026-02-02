package kaosborn.kromatron
import androidx.compose.ui.graphics.Color
import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GridGameTests {
    companion object {
        val COLORS = listOf (Color.Red, Color.Blue, Color.Green, Color.Cyan, Color.Magenta, Color.Yellow)
        fun getPoints (area:Int) = area * (area+1)
    }

    @Test
    fun emptyNoColors() {
        val colors0:List<Color> = emptyList()
        val gg = GridGame (colors0, 0, 0)
        assertEquals (0, gg.xSize)
        assertEquals (0, gg.ySize)
        assertEquals ("", gg.toString())
        assertTrue (gg.calcHint (1, ::getPoints) < 0)
    }

    @Test
    fun emptyHasColors() {
        val colors2:List<Color> = listOf (Color.Red, Color.Blue)
        val gg = GridGame (colors2, 0, 0)
        assertEquals (0, gg.xSize)
        assertEquals (0, gg.ySize)
        assertEquals ("", gg.toString())
        assertTrue (gg.isConstant)
        assertTrue (gg.calcHint (1, ::getPoints) < 0)
    }

    @Test
    fun noWidth() {
        val colors2 = listOf (Color.Red, Color.Blue)
        val exception = assertFailsWith<IllegalArgumentException> {
            val gg = GridGame (colors2, 0, 3)
        }
        assertEquals ("Illegal value", exception.message)
    }

    @Test
    fun noHeight() {
        val colors2 = listOf (Color.Red, Color.Blue)
        val gg = GridGame (colors2, 2, 0)
        assertEquals ("", gg.toString())
        assertTrue (gg.isConstant)
        assertTrue (gg.calcHint (1, ::getPoints) < 0)
    }

    @Test
    fun grid2by3NoColor() {
        val colors0:List<Color> = emptyList()
        val expected1 = "0 0\n0 0\n0 0"

        val gg = GridGame (colors0, 2, 3)
        assertEquals (2, gg.xSize)
        assertEquals (3, gg.ySize)
        assertFalse (gg.isConstant)
        assertEquals (0, gg.calcHint (1, ::getPoints))
        assertEquals (expected1, gg.toString())
    }

    @Test
    fun grid1by2() {
        val colors1 = listOf (Color.Red)
        val expected1 = "0\n0"

        val gg = GridGame (colors1, 1, 2)
        assertEquals (1, gg.xSize)
        assertEquals (2, gg.ySize)
        assertEquals (2, gg.monoArea)
        assertEquals (-1, gg.calcHint (0, ::getPoints))
        assertEquals (expected1, gg.toString())
    }

    @Test
    fun grid2by1() {
        val colors1 = listOf (Color.Red)
        val expected1 = "0 0"

        val gg = GridGame (colors1, 2, 1)
        assertEquals (2, gg.xSize)
        assertEquals (1, gg.ySize)
        assertEquals (2, gg.monoArea)
        assertEquals (expected1, gg.toString())
    }

    @Test
    fun grid2by3() {
        val colors2 = listOf (Color.Red)
        val expected1 = "0 0\n0 0\n0 0"

        val gg = GridGame (colors2, 2, 3)
        assertEquals (2, gg.xSize)
        assertEquals (3, gg.ySize)
        assertEquals (expected1, gg.toString())
        assertTrue (gg.isConstant)
    }

    @Test
    fun gridByValues() {
        val colors2 = listOf (Color.Red, Color.Blue)
        val source:List<List<Int>> = listOf(
            listOf ( 1,0 ),
            listOf ( 1,1 ),
            listOf ( 1,0 ))
        val expected1 = "1 0\n1 1\n1 0"

        val gg = GridGame (colors2, source)
        assertEquals (expected1, gg.toString())
        assertEquals (4, gg.monoArea)
    }

    @Test
    fun floodEmptyNoColors() {
        val colors0:List<Color> = emptyList()
        val source:List<List<Int>> = emptyList()

        val gg = GridGame (colors0, source)
        assertEquals ("", gg.toString())

        gg.flood4 (0)
        assertEquals ("", gg.toString())

        gg.reclaim()
        assertEquals ("", gg.toString())
    }

    @Test
    fun floodEmptyHasColors() {
        val colors2 = listOf (Color.Red, Color.Blue)
        val source:List<List<Int>> = emptyList()

        val gg = GridGame (colors2, source)
        assertEquals ("", gg.toString())
        
        gg.flood4 (0)
        assertTrue (gg.isConstant)
    }

    @Test
    fun flood0() {
        val colors2 = listOf (Color.Red, Color.Blue)
        val source:List<List<Int>> = listOf (emptyList())
        val exception = assertFailsWith<IllegalArgumentException> {
            val gg = GridGame (colors2, source)
        }
        assertEquals ("Illegal value", exception.message)
    }

    @Test
    fun flood1() {
        val colors2 = listOf (Color.Red, Color.Blue)
        val source:List<List<Int>> = listOf (listOf (1))

        val gg = GridGame (colors2, source)
        assertEquals ("1", gg.toString())
        assertEquals(1, gg.monoArea)
        assertTrue (gg.isConstant)

        gg.flood4 (0)
        assertEquals ("0", gg.toString())
        assertEquals (1, gg.monoArea)
    }

    @Test
    fun flood2() {
        val colors3 = listOf (Color.Red, Color.Blue, Color.Green)
        val source = listOf(
            listOf ( 1,0,0,1,1 ),
            listOf ( 1,1,1,0,1 ),
            listOf ( 1,0,1,1,1 ),
            listOf ( 2,1,1,0,2 ))
        val sourceCopy = listOf(
            listOf ( 1,0,0,1,1 ),
            listOf ( 1,1,1,0,1 ),
            listOf ( 1,0,1,1,1 ),
            listOf ( 2,1,1,0,2 ))

        val expected1 = "2 0 0 2 2\n2 2 2 0 2\n2 0 2 2 2\n2 2 2 0 2"
        val expected2 = "1 0 0 1 1\n1 1 1 0 1\n1 0 1 1 1\n1 1 1 0 1"
        val expected3 = "0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0\n0 0 0 0 0"

        val gg = GridGame (colors3, source)
        assertTrue (gg.isEqual (sourceCopy))
        assertEquals (13, gg.monoArea)

        gg.flood4 (2)
        assertEquals (expected1, gg.toString())
        assertEquals (15, gg.monoArea)

        gg.flood4 (1)
        assertEquals (expected2, gg.toString())
        assertEquals (15, gg.monoArea)
        assertFalse (gg.isConstant)

        gg.flood4 (0)
        assertEquals (expected3, gg.toString())
        assertEquals (20, gg.monoArea)
        assertTrue (gg.isConstant)

        gg.reclaim()
        assertEquals (expected2, gg.toString())
        assertFalse (gg.isConstant)

        gg.reclaim()
        assertEquals (expected1, gg.toString())

        gg.reclaim()
        assertTrue (gg.isEqual(sourceCopy))

        gg.reclaim()
        assertTrue (gg.isEqual(sourceCopy))
    }

    @Test
    fun hints1() {
        val gg = GridGame (COLORS.subList(0,4), listOf(listOf( 1,2,3 )))
        assertEquals (2, gg.calcHint (0, ::getPoints))
        assertEquals (2, gg.calcHint (1, ::getPoints))
        assertEquals (2, gg.calcHint (2, ::getPoints))
    }

    @Test
    fun hints2() {
        val source1 = listOf(
            listOf ( 0,1,1 ),
            listOf ( 2,2,2 ))
        val gg1 = GridGame (COLORS.subList(0,3), source1)
        assertEquals (2, gg1.calcHint (0, ::getPoints))
        assertEquals (2, gg1.calcHint (1, ::getPoints))
        assertEquals (2, gg1.calcHint (2, ::getPoints))

        val source2 = listOf(
            listOf ( 0,2,2 ),
            listOf ( 1,1,1 ))
        val gg2 = GridGame (COLORS.subList(0,3), source2)
        assertEquals (1, gg2.calcHint (0, ::getPoints))
        assertEquals (1, gg2.calcHint (1, ::getPoints))
        assertEquals (1, gg2.calcHint (2, ::getPoints))
    }

    @Test
    fun hints3() {
        val source = listOf(
            listOf ( 0,1,3 ),
            listOf ( 0,2,2 ),
            listOf ( 3,3,0 ))
        val gg = GridGame (COLORS.subList(0,4), source)
        assertEquals (3, gg.calcHint (0, ::getPoints))
        assertEquals (2, gg.calcHint (1, ::getPoints))
    }

    @Test
    fun hints4() {
        val source = listOf(
            listOf ( 0,1,2,3 ),
            listOf ( 0,3,3,0 ))
        val gg = GridGame (COLORS.subList(0,4), source)
        assertEquals (3, gg.calcHint (0, ::getPoints))
        assertEquals (3, gg.calcHint (1, ::getPoints))
        assertEquals (1, gg.calcHint (2, ::getPoints))
        assertEquals (1, gg.calcHint (3, ::getPoints))
    }

    @Test
    fun hints5() {
        val source = listOf(
            listOf ( 0,1,2,3 ),
            listOf ( 0,3,3,5 ),
            listOf ( 3,3,4,0 ))
        val gg = GridGame (COLORS.subList(0,6), source)
        assertEquals (3, gg.calcHint (0, ::getPoints))
        assertEquals (3, gg.calcHint (1, ::getPoints))
        assertEquals (1, gg.calcHint (2, ::getPoints))
        assertEquals (1, gg.calcHint (3, ::getPoints))
        assertEquals (1, gg.calcHint (4, ::getPoints))
        assertEquals ("0 1 2 3\n0 3 3 5\n3 3 4 0", gg.toString())
    }
}
