package kaosborn.kromatron.ui
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog (vals:Settings, onConfirm:(Settings) -> Unit, onDismiss:() -> Unit) {
    val sizeOptions = listOf("0","1","2","3","4","5","6","12","14","20")

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text ("Settings") },
        text = {
            Column {
                var expanded1 by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded1,
                    onExpandedChange = { expanded1 = ! expanded1 }
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        label = { Text ("Board size") },
                        value = vals.boardSize.toString(),
                        onValueChange = { },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded=expanded1) }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded1,
                        onDismissRequest = { expanded1 = false }
                    ) {
                        sizeOptions.forEach { option ->
                            DropdownMenuItem(
                                onClick = {
                                    vals.boardSize = option.toInt()
                                    expanded1 = false
                                },
                                text = { Text(option) }
                            )
                        }
                    }
                }

                var expanded2 by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded2,
                    onExpandedChange = { expanded2 = ! expanded2 },
                ) {
                    OutlinedTextField(
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        label = { Text ("Palette size") },
                        value = vals.paletteSize.toString(),
                        onValueChange = { },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded=expanded2) }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded2,
                        //containerColor = MenuDefaults.groupStandardContainerColor,
                        onDismissRequest = { expanded2 = false }
                    ) {
                        for (i in 1..vals.basePaletteSize) {
                            DropdownMenuItem(
                                onClick = {
                                    vals.paletteSize = i
                                    expanded2 = false
                                },
                                text = { Text(i.toString()) }
                            )
                        }
                    }
                }

            }
        },
        confirmButton = {
            Button(onClick = { onConfirm (vals) }) {
                Text("Apply")
            }
        }
    )
}
