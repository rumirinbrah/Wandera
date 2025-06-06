package com.zzz.feature_trip.create.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.zzz.data.trip.model.TodoLocation
import com.zzz.feature_trip.R

/**
 * Represents a single TodoLocation Item in Lazy view
 */
@Composable
fun TodoLocationItem(
    todo: TodoLocation,
    onDeleteTodo : (TodoLocation) ->Unit,
    modifier: Modifier = Modifier
) {

    Row (
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(
                    if(todo.isTodo){
                        com.zzz.core.R.drawable.todo_tick
                    }else{
                        com.zzz.core.R.drawable.location
                    }
                ) ,
                contentDescription = todo.title
            )
            Text(todo.title)
        }
        IconButton(
            onClick = {
                onDeleteTodo(todo)
            }
        ) {
            Icon(
                painter = painterResource(com.zzz.core.R.drawable.delete) ,
                contentDescription = "delete ${todo.title}",
            )
        }
    }


}