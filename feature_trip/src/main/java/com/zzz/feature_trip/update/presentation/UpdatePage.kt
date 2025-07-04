package com.zzz.feature_trip.update.presentation

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.buttons.IconTextButton
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.ConfirmActionDialog
import com.zzz.core.presentation.dialogs.DateRangePickerDialog
import com.zzz.core.presentation.dialogs.LoadingDialog
import com.zzz.core.presentation.events.ObserveAsEvents
import com.zzz.core.presentation.events.UIEvents
import com.zzz.core.presentation.modifiers.customShadow
import com.zzz.core.presentation.snackbar.WanderaSnackbar
import com.zzz.core.presentation.snackbar.showWanderaSnackbar
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.feature_trip.create.presentation.components.DocumentCard
import com.zzz.feature_trip.create.presentation.components.IndicatorCard
import com.zzz.feature_trip.create.presentation.components.ItineraryItem
import com.zzz.feature_trip.create.presentation.components.UploadDocumentComponent
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.util.toFormattedDate
import com.zzz.feature_trip.update.presentation.viewmodel.UpdateTripState
import com.zzz.feature_trip.update.presentation.viewmodel.UpdateTripViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

/**
 * This is simply a copy of the CreatePage with a few logical changes in the view model
 */
@Composable
fun UpdateRoot(
    modifier: Modifier = Modifier ,
    onNavToAddDay : ()->Unit ,
    onEditDay :()->Unit ,
    navigateUp: () -> Unit ,
    updateTripViewModel: UpdateTripViewModel = koinViewModel() ,
) {

    val state by updateTripViewModel.state.collectAsStateWithLifecycle()



    val uiEvents = updateTripViewModel.events

    UpdateTripPage(
        modifier,
        state = state,
        events = uiEvents,
        onAction = {action->
            updateTripViewModel.onAction(action)
        },
        onNavToAddDay = onNavToAddDay,
        onEditDay = onEditDay,
        navigateUp = navigateUp
    )
}

@Composable
private fun UpdateTripPage(
    modifier: Modifier = Modifier ,
    state : UpdateTripState ,
    events : Flow<UIEvents> ,
    onAction :(CreateAction)->Unit ,
    onNavToAddDay : ()->Unit ,
    onEditDay : ()->Unit ,
    navigateUp : ()->Unit
) {

    val snackbarState = remember {  SnackbarHostState() }

    val scope = rememberCoroutineScope()

    var showDatePicker by remember { mutableStateOf(false) }
    var showConfirmDiscardDialog by remember { mutableStateOf(false) }



    ObserveAsEvents(events) {event->
        when(event){
            is UIEvents.Error -> {
                scope.launch {
                    snackbarState.showWanderaSnackbar(
                        message = event.errorMsg,
                    )

                }
            }
            UIEvents.Success -> {
                //sent when saved to db!
                navigateUp()
            }
            else->{}
        }
    }
    BackHandler {
        showConfirmDiscardDialog = true
    }


    Box(modifier.fillMaxSize()){
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp) ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box (
                Modifier.fillMaxWidth()
            ){
                CircularIconButton(
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.CenterStart),
                    icon = com.zzz.core.R.drawable.arrow_back ,
                    contentDescription = "Go back" ,
                    background = Color.DarkGray.copy(0.3f) ,
                    onBackground = Color.White,
                    onClick = {
                        showConfirmDiscardDialog = true
                    }
                )
                Text(
                    "Update trip details" ,
                    fontSize = 18.sp ,
                    fontWeight = FontWeight.Bold ,
                    modifier = Modifier.align(Alignment.Center)
                )
            }


            //title
            VerticalSpace()
            Text(
                "Title" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )
            RoundedTextField(
                value = state.tripTitle ,
                placeholder = "Give your trip a title!" ,
                onValueChange = {value->
                    onAction(CreateAction.TripActions.OnTripTitleChange(value))
                } ,
                imeAction = ImeAction.Done,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpace(5.dp)
            //date
            Text(
                "Start & End Date" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                CircularIconButton(
                    icon = com.zzz.core.R.drawable.calendar ,
                    contentDescription = "Open calendar to pick dates" ,
                    onClick = {
                        //launch date dialog
                        showDatePicker = true

                    } ,
                    iconSize = 30.dp
                )
                if(state.startDate!=null && state.endDate!=null){
                    Row {
                        Text(state.startDate.toFormattedDate()+" - ")
                        Text(state.endDate.toFormattedDate("dd MMM yyyy"))
                    }
                }else{
                    Text("None selected")
                }
            }
            //put the dialog here
            if (showDatePicker) {
                DateRangePickerDialog(
                    onSelect = { start , end ->
                        Log.d("date" , "Selected date start $start ; end $end ")
                        showDatePicker = false

                        onAction(CreateAction.TripActions.OnDateSelect(start,end))
                    } ,
                    onDismiss = {
                        showDatePicker = false
                    }
                )
            }

            //Itinerary
            VerticalSpace(5.dp)
            Row(
                modifier = Modifier.fillMaxWidth() ,
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Itinerary" ,
                    fontSize = 16.sp ,
                    fontWeight = FontWeight.Bold ,
                )
                IconTextButton(
                    icon = com.zzz.core.R.drawable.add ,
                    text = "Add Day" ,
                    onClick = onNavToAddDay
                )
            }
            AnimatedVisibility(state.days.isEmpty()){
                IndicatorCard("Added itinerary will appear here")
            }
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.days.onEach {day->
                    ItineraryItem(
                        day,
                        onClick = {id->
                            onAction(CreateAction.DayActions.FetchDayById(id))
                            onEditDay()
                        },
                        onDelete = {id->
                            println("id is $id")
                            onAction(CreateAction.DayActions.OnDeleteDay(id))
                        }
                    )
                }

            }


            //docs
            VerticalSpace(5.dp)

            UploadDocumentComponent(
                onAddDoc = { uri,name->
                    onAction(CreateAction.TripActions.OnDocumentUpload(uri,name))
                }
            )
            if(state.userDocs.isEmpty()){
                IndicatorCard("Added documents will appear here")
            }
            LazyColumn(
                Modifier.fillMaxWidth()
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = state.userDocs,
                    key = {
                        it.id
                    }
                ){document->
                    DocumentCard(
                        document = document,
                        onDelete = {docId->
                            onAction(CreateAction.TripActions.DeleteDocument(docId))
                        },
                        modifier = Modifier.animateItem(

                        )
                    )
                }
            }
            VerticalSpace()
            //save
            NormalButton(
                title = "Update" ,
                onClick = {
                    onAction(CreateAction.OnSave)
                    //navigateUp()
                },
                background = MaterialTheme.colorScheme.onBackground,
                onBackground = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .customShadow(
                        color = MaterialTheme.colorScheme.onBackground,
                        alpha = 0.2f,
                        offsetY = 10f
                    )
                    .fillMaxWidth()
            )


        }
        when{
            state.saving->{
                LoadingDialog(modifier = Modifier.align(Alignment.Center))
            }
            showConfirmDiscardDialog ->{
                ConfirmActionDialog(
                    title = "Are you sure you want to go back? All created data will be lost" ,
                    actionText = "Discard" ,
                    onConfirm = {
                        onAction(CreateAction.OnDiscardTripCreation)
                        navigateUp()
                        showConfirmDiscardDialog = false
                    } ,
                    onCancel = {
                        showConfirmDiscardDialog = false
                    }
                )
            }
        }

        SnackbarHost(
            hostState = snackbarState,
            modifier = Modifier.align(Alignment.BottomCenter)
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ){
            WanderaSnackbar(
                it
            )
        }

    }


}