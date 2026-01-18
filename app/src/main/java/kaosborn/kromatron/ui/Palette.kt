package kaosborn.kromatron.ui
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Palette (vm:GridGameViewModel, palette:List<Color>, selection:Int?) {
    var width:Dp = (getAppWidth() - 4.dp) / palette.size
    if (width > 60.dp)
        width = 60.dp

    Row (verticalAlignment=Alignment.CenterVertically) {
        for (i in 0..<palette.size) {
            Box(
                modifier = Modifier.height (60.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { vm.pushMove (i) },
                    modifier = Modifier
                        .height (if (selection==null || selection==i) 30.dp else 60.dp)
                        .width (width),
                    enabled = selection!=null && selection!=i,
                    colors = ButtonDefaults.buttonColors (containerColor=palette[i], disabledContainerColor=palette[i])
                ) { }
            }
        }
    }
}
