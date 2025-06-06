package com.zzz.feature_trip.create.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.DialogWithTextField
import com.zzz.core.presentation.dialogs.OptionSelectorDialog
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.model.TodoLocation
import com.zzz.feature_trip.R
import com.zzz.feature_trip.create.presentation.components.TodoLocationItem
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddDayRoot(
    createViewModel: CreateViewModel = koinViewModel()
) {
    val dayState by createViewModel.dayState.collectAsStateWithLifecycle()
    val tripState by createViewModel.tripState.collectAsStateWithLifecycle()

    AddDayPage(
        dayState = dayState,
        dayNo = tripState.days.size+1,
        onAction = {action->
            createViewModel.onAction(action)
        }
    )
}

@Composable
private fun AddDayPage(
    dayState : DayState,
    dayNo : Int = 0,
    onAction :(CreateAction) ->Unit,
) {
    var backHandlerDialog  by remember { mutableStateOf(false) }

    BackHandler {

    }

    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp) ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            NormalButton(
                title = "Cancel" ,
                onClick = {

                },
                contentDescription = "cancel",
                background = MaterialTheme.colorScheme.errorContainer,
                onBackground = MaterialTheme.colorScheme.onErrorContainer,
                verticalPadding = 4.dp,
            )
            NormalButton(
                title = "Save" ,
                onClick = {

                },
                contentDescription = "save",
                verticalPadding = 4.dp
            )
        }

        VerticalSpace(5.dp)
        Text(
            "DAY $dayNo" ,
            fontSize = 18.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        //title
        VerticalSpace()
        Text(
            "Location" ,
            fontSize = 16.sp ,
            fontWeight = FontWeight.Bold ,
        )
        RoundedTextField(
            value = dayState.dayTitle ,
            placeholder = "Where are you headed?" ,
            onValueChange = {value->
                onAction(CreateAction.OnDayTitleChange(value))
            } ,
            imeAction = ImeAction.Done,
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        //toddooos
        VerticalSpace(5.dp)
        Row (
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            Text(
                "Places to visit/TODOs" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )

            CircularIconButton(
                icon = com.zzz.core.R.drawable.add ,
                contentDescription = "Add a TODO" ,
                onClick = {
                    onAction(CreateAction.OnDialogVisibilityChange(true))
                },
                iconSize = 30.dp
            )
        }
        LazyColumn(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            items(
                items = dayState.todoLocations
            ){todo->
                TodoLocationItem(
                    todo,
                    modifier = Modifier,
                    onDeleteTodo = {
                        onAction(CreateAction.OnDeleteTodoLocation(it))
                    }
                )
            }
        }

        //add dialog
        if(dayState.dialogVisible){
            OptionSelectorDialog(
                title = "Enter title" ,
                textFieldPlaceholder = "Title" ,
                onDone = {title,isTodo->
                    onAction(CreateAction.OnAddTodoLocation(title,isTodo))
                } ,
                onDismiss = {
                    onAction(CreateAction.OnDialogVisibilityChange(false))
                }
            )
        }


    }

}

@Preview
@Composable
private fun AddDayPrev() {
    WanderaTheme {
        AddDayPage(DayState(),1) { }
    }
}