package com.zzz.feature_trip.day_details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.CheckboxCircular
import com.zzz.core.presentation.modifiers.customShadow
import com.zzz.core.theme.successGreen
import com.zzz.data.trip.model.TodoLocation

/**
 * TodoLocation lazy col item for DayDetails page
 * @param isViewOnly Can be used to hide actions
 * @param onCheck Called when TodoLoc is marked as done!
 */
@Composable
fun DayDetailsTodoItem(
    todo: TodoLocation ,
    onCheck : (itemId : Long , checked : Boolean)->Unit,
    modifier: Modifier = Modifier,
    isViewOnly : Boolean = false ,
    contentColor : Color = MaterialTheme.colorScheme.onBackground
) {

    val textColor = remember(todo.isDone) {
        when{
            isViewOnly -> contentColor
            todo.isDone-> contentColor.copy(0.7f)
            else-> contentColor

        }
    }
    Row (
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Row (
            Modifier.weight(1f)
                .padding(end = 4.dp),
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
            Text(
                todo.title ,
                fontSize = 18.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = textColor,
                modifier = Modifier
                    .padding(horizontal = 5.dp)
            )
        }
        if(!isViewOnly){
            CheckboxCircular(
                checked = todo.isDone ,
                onCheck = {
                    onCheck(todo.id, it)
                },
                onBackground = successGreen ,
                background = MaterialTheme.colorScheme.surfaceContainer,
                modifier = Modifier.customShadow(
                    shadowRadius = 5f,
                    color = MaterialTheme.colorScheme.onBackground,
                    alpha = 0.1f
                )
            )

        }

    }
}