package com.advancednotes.ui.components.top_app_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.advancednotes.R
import com.advancednotes.ui.components.MyDropdownMenu
import com.advancednotes.ui.components.buttons.MyIconButton
import com.advancednotes.ui.components.texts.MyText
import com.advancednotes.ui.components.texts.MyTextAnimated
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    drawerState: DrawerState,
    drawerScope: CoroutineScope,
    myTopAppBarState: MyTopAppBarState
) {
    val scrollBehavior: TopAppBarScrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior()

    var dropDownExpanded: Boolean by rememberSaveable { mutableStateOf(false) }

    val shadowColors = listOf(
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
    )

    Column {
        TopAppBar(
            title = {
                MyTextAnimated(
                    text = myTopAppBarState.label,
                    textStyle = MaterialTheme.typography.titleMedium
                )
            },
            navigationIcon = {
                if (myTopAppBarState.navigationBack) {
                    MyIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = {
                            myTopAppBarState.navigationBackAction?.invoke()
                        },
                        contentDescription = stringResource(id = R.string.cd_back)
                    )
                } else {
                    MyIconButton(
                        icon = Icons.Default.Menu,
                        onClick = {
                            drawerScope.launch { drawerState.open() }
                        },
                        contentDescription = stringResource(id = R.string.cd_open_menu)
                    )
                }
            },
            actions = {
                myTopAppBarState.actions.forEach { item ->
                    MyIconButton(
                        icon = item.icon,
                        onClick = {
                            item.onClick()
                        },
                        contentDescription = item.text
                    )
                }

                if (myTopAppBarState.menuActions.isNotEmpty()) {
                    Box {
                        MyIconButton(
                            icon = Icons.Default.MoreVert,
                            onClick = {
                                dropDownExpanded = true
                            }
                        )

                        MyDropdownMenu(
                            expanded = dropDownExpanded,
                            onDismissRequest = {
                                dropDownExpanded = false
                            }
                        ) {
                            myTopAppBarState.menuActions.forEach { item ->
                                DropdownMenuItem(
                                    text = {
                                        MyText(
                                            text = item.text,
                                            textStyle = MaterialTheme.typography.bodySmall
                                        )
                                    },
                                    onClick = {
                                        item.onClick()
                                        dropDownExpanded = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = item.icon,
                                            contentDescription = null,
                                            modifier = Modifier.size(ButtonDefaults.IconSize),
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            },
            scrollBehavior = scrollBehavior,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    brush = Brush.linearGradient(colors = shadowColors)
                )
        )
    }
}