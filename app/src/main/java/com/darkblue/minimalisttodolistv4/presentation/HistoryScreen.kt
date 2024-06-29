package com.darkblue.minimalisttodolistv4.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.automirrored.outlined.Undo
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.darkblue.minimalisttodolistv4.data.DeletedTask

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(
    viewModel: TaskViewModel,
    onEvent: (TaskEvent) -> Unit
) {
    val deletedTasks by viewModel.deletedTasks.collectAsState()

    BasicAlertDialog(onDismissRequest = {
        onEvent(TaskEvent.HideHistoryDialog)
    }) {
        CustomBox {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .height(400.dp)
            ) {
                Text(
                    text = "History",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .animateContentSize()
                ) {
                    items(deletedTasks) { deletedTask ->
                        HistoryItem(deletedTask, viewModel::onEvent, viewModel)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryItem(
    deletedTask: DeletedTask,
    onEvent: (TaskEvent) -> Unit,
    viewModel: TaskViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 8.dp, top = 8.dp, bottom = 8.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .width(280.dp)
        ) {
            Text(
                text = deletedTask.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = viewModel.formatDueDateWithDateTime(deletedTask.deletedAt)
            )
        }
        Box {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                modifier = Modifier
                    .clickable {
                        expanded = true
                    }
            )
            CustomDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {
                DropDownItem(
                    onRecoverClick = {
                        onEvent(TaskEvent.UndoDeleteTask(deletedTask))
                        expanded = false
                    },
                    onDeleteClick = {
                        onEvent(TaskEvent.DeleteForever(deletedTask))
                        expanded = false
                    })
            }
        }
    }
}


@Composable
fun DropDownItem(modifier: Modifier = Modifier, onRecoverClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 5.dp, bottom = 5.dp)
            .fillMaxHeight()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.Undo,
                contentDescription = "Recover",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.clickable {
                    onRecoverClick()
                }
            )
            Text(
                "Recover",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(30.dp))
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.clickable {
                    onDeleteClick()
                }
            )
            Text(
                "Delete",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}