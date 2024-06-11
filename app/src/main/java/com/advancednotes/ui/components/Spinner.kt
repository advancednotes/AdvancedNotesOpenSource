package com.advancednotes.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.advancednotes.domain.models.ContextMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Spinner(
    items: List<ContextMenuItem>,
    selectedItemText: String,
    modifier: Modifier = Modifier
) {
    var dropDownExpanded: Boolean by remember { mutableStateOf(false) }

    val shape = MaterialTheme.shapes.medium
    val dropdownShapes = MaterialTheme.shapes.copy(extraSmall = MaterialTheme.shapes.medium)

    MaterialTheme(
        shapes = dropdownShapes
    ) {
        ExposedDropdownMenuBox(
            expanded = dropDownExpanded,
            onExpandedChange = { dropDownExpanded = !dropDownExpanded },
            modifier = modifier
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = shape
                    ),
                value = selectedItemText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (dropDownExpanded) {
                            Icons.Filled.KeyboardArrowUp
                        } else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                },
                shape = shape,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = dropDownExpanded,
                onDismissRequest = {
                    dropDownExpanded = false
                }
            ) {
                items.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.text,
                                style = MaterialTheme.typography.bodySmall
                            )
                        },
                        onClick = {
                            it.onClick()
                            dropDownExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}