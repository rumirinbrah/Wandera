package com.zzz.feature_trip.create.presentation

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.ConfirmActionDialog
import com.zzz.core.presentation.dialogs.OptionSelectorDialog
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.trip.model.TodoLocation
import com.zzz.feature_trip.create.presentation.components.IndicatorCard
import com.zzz.feature_trip.create.presentation.components.TodoLocationItem
import com.zzz.feature_trip.create.presentation.components.UploadImageComponent
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import org.koin.androidx.compose.koinViewModel

/**
 * @author zyzz
 */
@Composable
fun AddDayRoot(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit ,
    createViewModel: CreateViewModel = koinViewModel()
) {
    val dayState by createViewModel.dayState.collectAsStateWithLifecycle()
    val todos by createViewModel.todos.collectAsStateWithLifecycle()

    AddDayPage(
        modifier,
        dayState = dayState ,
        todos = todos ,
        navigateUp = navigateUp,
        onAction = { action ->
            when (action) {
                CreateAction.DayActions.OnUpdateDay -> {
                    createViewModel.onAction(action)
                    navigateUp()
                }

                else -> {
                    createViewModel.onAction(action)
                }
            }
        }
    )
}

/**
 * @author zyzz
 */
@Composable
private fun AddDayPage(
    modifier: Modifier = Modifier,
    dayState: DayState ,
    todos: List<TodoLocation> ,
    navigateUp: () -> Unit ,
    onAction: (CreateAction) -> Unit ,
) {
    var backHandlerDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if(!dayState.isUpdating){
            onAction(CreateAction.DayActions.CreateDaySession)
        }
    }
    BackHandler {
        if (dayState.isUpdating) {
            navigateUp()
        } else {
            if (!backHandlerDialog) {
                backHandlerDialog = true
            }
        }

    }
    Column(
        modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp) ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (!dayState.isUpdating) {

            //new
            Row(
                Modifier.fillMaxWidth() ,
                horizontalArrangement = Arrangement.SpaceBetween ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NormalButton(
                    title = "Cancel" ,
                    onClick = {
                        backHandlerDialog = true
                    } ,
                    contentDescription = "cancel" ,
                    background = MaterialTheme.colorScheme.errorContainer ,
                    onBackground = MaterialTheme.colorScheme.onErrorContainer ,
                    verticalPadding = 4.dp ,
                )
                NormalButton(
                    title = "Save" ,
                    onClick = {
                        onAction(CreateAction.DayActions.OnUpdateDay)
                    } ,
                    contentDescription = "save" ,
                    verticalPadding = 4.dp
                )
            }
        } else {
            //update
            Row(
                Modifier.fillMaxWidth() ,
                horizontalArrangement = Arrangement.SpaceBetween ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularIconButton(
                    modifier = Modifier
                        .padding(4.dp) ,
                    icon = com.zzz.core.R.drawable.arrow_back ,
                    contentDescription = "Go back" ,
                    background = Color.DarkGray.copy(0.5f) ,
                    onBackground = Color.White,
                    onClick = {
                        onAction(CreateAction.DayActions.ClearDayState)
                        navigateUp()
                    }
                )
                NormalButton(
                    title = "Update" ,
                    onClick = {
                        onAction(CreateAction.DayActions.OnUpdateDay)
                    } ,
                    contentDescription = "update day" ,
                    verticalPadding = 4.dp
                )
            }
        }

        VerticalSpace(5.dp)
        Text(
            if (dayState.isUpdating) {
                dayState.dayTitle
            } else {
                "NEW itinerary item"
            } ,
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
            onValueChange = { value ->
                onAction(CreateAction.DayActions.OnDayTitleChange(value))
            } ,
            imeAction = ImeAction.Done ,
            singleLine = true ,
            //enabled = !dayState.isUpdating,
            modifier = Modifier.fillMaxWidth() ,
        )

        //image
        VerticalSpace(5.dp)
        UploadImageComponent(
            image = dayState.image,
            dayTitle = dayState.dayTitle,
            onImagePick = {uri->
                onAction(CreateAction.DayActions.OnPickImage(uri))
            },
            //isViewOnly = dayState.isUpdating
        )

        //toddooos
        VerticalSpace(5.dp)
        Row(
            Modifier.fillMaxWidth() ,
            horizontalArrangement = Arrangement.SpaceBetween ,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Places to visit/TODOs" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )

            CircularIconButton(
                icon = com.zzz.core.R.drawable.add ,
                contentDescription = "Add a TODO" ,
                onClick = {
                    onAction(CreateAction.DayActions.OnDialogVisibilityChange(true))
                } ,
                iconSize = 30.dp
            )

        }
        AnimatedVisibility(todos.isEmpty()) {
            IndicatorCard(
                text = buildAnnotatedString {
                    append("You can add everything about your plans for the day here. For ex, ")
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold
                        )
                    ){
                        append("'Visiting local market', 'Sky tree'")
                    }
                },
                background = MaterialTheme.colorScheme.surfaceContainer,
                onBackground = MaterialTheme.colorScheme.onSurfaceVariant,
                contentAlpha = 1f
            )
        }
        //"You can add everything about all your plans for the day here. For ex, 'Visiting local market' 'Sky tree'"
        LazyColumn(
            Modifier.fillMaxWidth() ,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            items(
                items = todos ,
                key = {
                    it.id
                }
            ) { todo ->
                TodoLocationItem(
                    todo ,
                    modifier = Modifier.animateItem() ,
                    onDeleteTodo = {
                        onAction(CreateAction.DayActions.OnDeleteTodoLocation(it.id))
                    }
                )
            }
        }

        //add dialog
        if (dayState.dialogVisible) {
            OptionSelectorDialog(
                title = "Enter title" ,
                textFieldPlaceholder = "Title" ,
                onDone = { title , isTodo ->
                    if(todoNameValid(title)){
                        onAction(CreateAction.DayActions.OnAddTodoLocation(title , isTodo))
                    }else{
                        Toast.makeText(context , "Hey are you sure you don't wanna give it a name?" , Toast.LENGTH_SHORT).show()
                    }
                } ,
                onDismiss = {
                    onAction(CreateAction.DayActions.OnDialogVisibilityChange(false))
                }
            )
        }
        //Back handler dialog
        if (backHandlerDialog) {
            ConfirmActionDialog(
                title = "Are you sure you want to go back? All the changes will be discarded" ,
                actionText = "Discard" ,
                onConfirm = {
                    backHandlerDialog = false
                    onAction(CreateAction.DayActions.OnDiscardCreation)
                    navigateUp()
                } ,
                onCancel = {
                    backHandlerDialog = false
                }
            )
        }


    }

}
private fun todoNameValid(str : String):Boolean{
    return str.isNotEmpty()
}
@Preview
@Composable
private fun AddDayPrev() {
    WanderaTheme {
        AddDayPage(
            Modifier,
            DayState() ,
            emptyList() ,
            navigateUp = {}
        ) { }
    }
}