package com.zzz.feature_trip.create.presentation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.zzz.core.presentation.buttons.ElevatedIconTextButton
import com.zzz.core.presentation.buttons.IconTextButton
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.dialogs.DateRangePickerDialog
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.theme.WanderaTheme
import com.zzz.feature_trip.R
import com.zzz.feature_trip.create.presentation.components.IndicatorCard
import com.zzz.feature_trip.create.presentation.components.ItineraryItem
import com.zzz.feature_trip.create.presentation.states.CreateAction
import com.zzz.feature_trip.create.presentation.states.DayState
import com.zzz.feature_trip.create.presentation.states.TripState
import com.zzz.feature_trip.create.util.toFormattedDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateRoot(
    onNavToAddDay : ()->Unit,
    createViewModel: CreateViewModel = koinViewModel() ,
    modifier: Modifier = Modifier
) {

    val tripState by createViewModel.tripState.collectAsStateWithLifecycle()
    val dayState by createViewModel.dayState.collectAsStateWithLifecycle()

    CreateTripPage(
        tripState,
        dayState,
        onAction = {action->
            createViewModel.onAction(action)
        },
        onNavToAddDay = onNavToAddDay
    )
}

@Composable
private fun CreateTripPage(
    tripState : TripState,
    dayState : DayState,
    onAction :(CreateAction)->Unit,
    onNavToAddDay : ()->Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp) ,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Create new adventure!" ,
            fontSize = 18.sp ,
            fontWeight = FontWeight.Bold ,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        //title
        VerticalSpace()
        Text(
            "Title" ,
            fontSize = 16.sp ,
            fontWeight = FontWeight.Bold ,
        )
        RoundedTextField(
            value = tripState.tripTitle ,
            placeholder = "Give your trip a title!" ,
            onValueChange = {value->
                onAction(CreateAction.OnTripTitleChange(value))
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
            if(tripState.startDate!=null && tripState.endDate!=null){
                Row {
                    Text(tripState.startDate.toFormattedDate()+" - ")
                    Text(tripState.endDate.toFormattedDate("dd MMM yyyy"))
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

                    onAction(CreateAction.OnDateSelect(start,end))
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
        AnimatedVisibility(tripState.days.isEmpty()){
            IndicatorCard("Added itinerary will appear here")
        }
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tripState.days.onEach { dayWithTodos->
                ItineraryItem(
                    dayWithTodos,
                    onClick = {id->
                        onAction(CreateAction.FetchDayById(id))
                        onNavToAddDay()
                    }
                )
            }
        }
//        LazyColumn(
//            Modifier.fillMaxWidth()
//                .heightIn(0.dp,400.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//        ) {
//            items(tripState.days){dayWithTodos->
//                ItineraryItem(dayWithTodos)
//            }
//        }

        VerticalSpace(5.dp)
        //docs
        Column {
            Text(
                "Add tickets, documents, ID, etc." ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )
            Text(
                text = "(these files are not uploaded anywhere & are stored on your own device)" ,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //image
            ElevatedIconTextButton(
                icon = com.zzz.core.R.drawable.upload_image ,
                text = "Image" ,
                onClick = {

                }
            )
            //pdf
            ElevatedIconTextButton(
                icon = com.zzz.core.R.drawable.pdf ,
                text = "PDF" ,
                onClick = {

                }
            )
        }
        if(tripState.days.isEmpty()){
            IndicatorCard("Added documents will appear here")
        }

        //translate
        VerticalSpace(5.dp)
        Column {
            Text(
                "Want to download a translator?" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontWeight = FontWeight.Bold ,
                            fontSize = 15.sp
                        )
                    ) {
                        append("No internet?")
                    }
                    withStyle(
                        SpanStyle(
                            fontSize = 14.sp
                        )
                    ) {
                        append(
                            "I got you covered!\nGrab your offline translation model and impress " +
                                    "the locals!( or at least...confuse them slightly less )"
                        )
                    }
                }
            )
        }
        VerticalSpace(10.dp)
        IconTextButton(
            icon = com.zzz.core.R.drawable.arrow_45 ,
            text = "Choose language" ,
            onClick = {

            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        VerticalSpace()
        //save
        NormalButton(
            title = "Save" ,
            onClick = {

            },
            background = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )


    }
}

@Preview
@Composable
private fun HomePrev() {
    WanderaTheme { 
        CreateRoot(
            onNavToAddDay = {}
        )
    }
}
