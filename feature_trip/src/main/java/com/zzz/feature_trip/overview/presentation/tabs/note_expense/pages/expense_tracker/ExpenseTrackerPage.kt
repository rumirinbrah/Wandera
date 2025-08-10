package com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker

import android.content.res.Configuration
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.buttons.CircularIconButton
import com.zzz.core.presentation.buttons.NormalButton
import com.zzz.core.presentation.components.SheetState
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.components.WanderaBottomSheet
import com.zzz.core.presentation.components.WanderaSheetState
import com.zzz.core.presentation.events.ObserveAsEvents
import com.zzz.core.presentation.events.UIEvents
import com.zzz.core.presentation.headers.VerticalDateHeader
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.core.theme.successGreen
import com.zzz.feature_trip.R
import com.zzz.feature_trip.overview.domain.ExpenseEntityUI
import com.zzz.feature_trip.overview.domain.ExpenseType
import com.zzz.feature_trip.overview.domain.expenseTypes
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.components.CurrencySelectorPage
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel.ExpenseActions
import com.zzz.feature_trip.overview.presentation.tabs.note_expense.pages.expense_tracker.viewmodel.ExpenseTrackerViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate

/**
 * Contains all the user expenses, sub total, add new expense button
 * @param expenses All the expenses for trip
 * @param groupedExpenses Expenses grouped by date
 * @param onExpenseItemClick Launch sheet to fetch expense details
 * @param launchAddExpenseSheet Launch bottom sheet
 * @param interactionsEnabled To control UI interactions
 * @author zyzz
 */
@Composable
fun ExpensePage(
    expenses: List<ExpenseEntityUI> ,
    groupedExpenses: Map<LocalDate , List<ExpenseEntityUI>> ,
    totalExpense: Int? = null ,
    onExpenseItemClick: (itemId: Long) -> Unit ,
    launchAddExpenseSheet: () -> Unit ,
    shareExpenses: () -> Unit ,
    interactionsEnabled: Boolean = true ,
    viewOnly: Boolean = false ,
    modifier: Modifier = Modifier
) {

    Box(
        Modifier.fillMaxSize()
    ) {
        Column(
            modifier.fillMaxWidth() ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            //header
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //action buttons
                if (!viewOnly) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically ,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularIconButton(
                            icon = com.zzz.core.R.drawable.add ,
                            contentDescription = "add new expense" ,
                            onClick = {
                                launchAddExpenseSheet()
                            } ,
                            buttonSize = 40.dp ,
                            background = MaterialTheme.colorScheme.surfaceContainer ,
                            onBackground = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text("Add new" , fontWeight = FontWeight.Medium)
                    }
                }
                //share
                CircularIconButton(
                    icon = com.zzz.core.R.drawable.share ,
                    contentDescription = "share expense" ,
                    onClick = {
                        shareExpenses()
                    } ,
                    buttonSize = 40.dp ,
                    background = MaterialTheme.colorScheme.surfaceContainer ,
                    onBackground = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            VerticalSpace(5.dp)


            //--- TOTAL ---
            totalExpense?.let {
                Text(
                    "Total $it" ,
                    fontSize = 20.sp ,
                    fontWeight = FontWeight.Bold ,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp) ,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                /*
                items(
                    expenses,
                    key = {it.id}
                ) {item->
                    ExpenseEntityItem(
                        item,
                        modifier = Modifier.animateItem()
                    )

                }
                 */
                groupedExpenses.onEach { (date , list) ->
                    item {
                        VerticalDateHeader(date = date)
                    }
                    items(
                        list ,
                        key = { it.id }
                    ) { item ->
                        ExpenseEntityItem(
                            item ,
                            onClick = onExpenseItemClick ,
                            modifier = Modifier.animateItem() ,
                            clickEnabled = interactionsEnabled
                        )
                    }
                }

            }

        }

    }

}

/**
 * Bottom sheet for adding, updating expenses.
 * @param tripId To be used while saving an expense
 * @param itemId To fetch expense details
 * @param sheetState To control bottom sheet
 * @param onClosed Callback when sheet is closed
 * @author zyzz
 */
@Composable
internal fun AddExpenseSheet(
    tripId: Long? = null ,
    itemId: Long? = null ,
    sheetState: WanderaSheetState ,
    onClosed: () -> Unit = {} ,
    errorToast: (errorMsg: String) -> Unit = {} ,
    modifier: Modifier = Modifier ,
    sheetBackground: Color = MaterialTheme.colorScheme.surface ,
    onSheetBackground: Color = MaterialTheme.colorScheme.onSurface.copy(0.8f) ,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val flowRowItems = remember { expenseTypes }

    val expenseViewModel = koinViewModel<ExpenseTrackerViewModel>()
    val state by expenseViewModel.state.collectAsStateWithLifecycle()
    val events = expenseViewModel.events

    var selector by remember { mutableStateOf(false) }

    //----------- CLEAN UP VM -----------
    DisposableEffect(Unit) {
        onDispose {
            onClosed()
            expenseViewModel.onAction(ExpenseActions.Discard)
        }
    }

    //----------- ONE TIME EVENTS -----------
    ObserveAsEvents(events) { event ->
        when (event) {
            is UIEvents.Error -> {
                Toast.makeText(context , event.errorMsg , Toast.LENGTH_SHORT).show()
                //errorToast(event.errorMsg)
            }

            UIEvents.Success -> {
                scope.launch {
                    sheetState.dismiss()
                }
            }

            else -> Unit
        }
    }
    LaunchedEffect(sheetState.visible) {
        if (sheetState.visible) {
            scope.launch {
                sheetState.animateTo(SheetState.HALF_EXPANDED)
            }
        }
    }
    LaunchedEffect(itemId) {
        itemId?.let {
            expenseViewModel.onAction(ExpenseActions.FetchExpenseData(itemId))
        }
    }

    BackHandler {
        //onClosed()
        when {
            selector -> {
                selector = false
            }

            else -> {
                scope.launch {
                    sheetState.animateTo(SheetState.CLOSED)
                }
            }
        }

    }


    WanderaBottomSheet(
        sheetState ,
        //onSheetClosed = {} ,
        dismissTopContainer = true ,
        modifier = modifier
            .fillMaxSize()
            .background(sheetBackground)
        //.padding(16.dp),
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) ,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                VerticalSpace(5.dp)
                if (state.loading) {
                    CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
                }
                //price
                Row(
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        Modifier
                            .clickable(
                            ) {
                                selector = true
                            }
                    ) {
                        Text(
                            text = state.currencySymbol ?: "$" ,
                            fontSize = 25.sp ,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    RoundedTextField(
                        value = state.amount ,
                        onValueChange = {
                            expenseViewModel.onAction(ExpenseActions.OnAmountChange(it))
                        } ,
                        placeholder = "Amount/money spent" ,
                        background = MaterialTheme.colorScheme.surfaceContainer ,
                        onBackground = MaterialTheme.colorScheme.onSurfaceVariant ,
                        keyboardType = KeyboardType.Number ,
                        textStyle = TextStyle(
                            fontSize = 20.sp ,
                            fontWeight = FontWeight.Bold
                        ) ,
                        singleLine = true ,
                        modifier = Modifier.weight(1f) ,
                    )

                }


                //EXPENSE TYPE
                VerticalSpace(10.dp)
                Text(
                    "What did you spend on?" ,
                    fontWeight = FontWeight.Bold ,
                    fontSize = 16.sp ,
                    color = onSheetBackground
                )
                /*
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp) ,
                ) {
                    flowRowItems.onEach { type ->
                        ExpenseTypeFlowRowItem(
                            item = type ,
                            selected = type.shortTitle == state.expenseType ,
                            onClick = {
                                expenseViewModel.onAction(ExpenseActions.OnExpenseTypeChange(type.shortTitle))
                            }
                        )
                    }
                }

                 */
                ExpenseTypesFlowRow(
                    modifier = Modifier.fillMaxWidth() ,
                    items = flowRowItems ,
                    currentTypeShortTitle = state.expenseType ,
                    onClick = { shortTitle ->
                        expenseViewModel.onAction(ExpenseActions.OnExpenseTypeChange(shortTitle))
                    }
                )

                //titlew
                VerticalSpace(10.dp)
                RoundedTextField(
                    value = state.title ?: "" ,
                    onValueChange = {
                        expenseViewModel.onAction(ExpenseActions.OnTitleChange(it))
                    } ,
                    placeholder = "Title (Optional)" ,
                    background = MaterialTheme.colorScheme.surfaceContainer ,
                    onBackground = MaterialTheme.colorScheme.onSurfaceVariant ,
                    singleLine = true ,
                    modifier = Modifier.fillMaxWidth()
                )

                //split
                VerticalSpace(10.dp)
                RoundedTextField(
                    value = state.splitInto ?: "" ,
                    onValueChange = {
                        expenseViewModel.onAction(ExpenseActions.OnSplitIntoChange(it))
                    } ,
                    placeholder = "Split Into (Optional)" ,
                    background = MaterialTheme.colorScheme.surfaceContainer ,
                    onBackground = MaterialTheme.colorScheme.onSurfaceVariant ,
                    keyboardType = KeyboardType.Number ,
                    singleLine = true ,
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold ,
                        fontSize = 20.sp ,
                    ) ,
                    modifier = Modifier
                )

                //save
                VerticalSpace(30.dp)
                when {
                    state.saving -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(25.dp)
                                .align(Alignment.CenterHorizontally) ,
                            strokeWidth = 5.dp ,
                        )
                    }

                    state.updating -> {
                        NormalButton(
                            title = "Update" ,
                            onClick = {
                                tripId?.let {
                                    expenseViewModel.onAction(ExpenseActions.Update(it))
                                }
                            } ,
                            shape = MaterialTheme.shapes.small ,
                            background = successGreen ,
                            onBackground = Color.White ,
                            modifier = Modifier.align(Alignment.CenterHorizontally) ,
                            animationSpec = tween(200)
                        )
                        NormalButton(
                            title = "Delete" ,
                            onClick = {
                                tripId?.let {
                                    expenseViewModel.onAction(ExpenseActions.DeleteExpense)
                                }
                            } ,
                            enabled = !state.loading ,
                            shape = MaterialTheme.shapes.small ,
                            background = MaterialTheme.colorScheme.surfaceContainer ,
                            onBackground = MaterialTheme.colorScheme.onBackground ,
                            modifier = Modifier.align(Alignment.CenterHorizontally) ,
                        )
                    }

                    else -> {
                        NormalButton(
                            title = "Save" ,
                            onClick = {
                                tripId?.let {
                                    expenseViewModel.onAction(ExpenseActions.Save(it))
                                }
                            } ,
                            shape = MaterialTheme.shapes.small ,
                            background = successGreen ,
                            onBackground = Color.White ,
                            modifier = Modifier.align(Alignment.CenterHorizontally) ,
                            animationSpec = tween(200)
                        )
                    }
                }
            }
            if (selector) {
                CurrencySelectorPage(
                    onClick = { symbol , code ->
                        selector = false
                        expenseViewModel.onAction(ExpenseActions.OnCurrencyChange(symbol , code))
                    }
                )
            }

        }

    }
}

/**
 * Expense types flow row for the user to select the type of expense.
 * This component dynamically calculates item width depending upon the max num of items in the flow row
 * @param horizontalSpacing Spacing between items
 * @param maxItemsEachRow Max num of items in a single row of flowRow
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpenseTypesFlowRow(
    modifier: Modifier = Modifier ,
    items: List<ExpenseType> ,
    currentTypeShortTitle: String ,
    onClick: (shortTitle: String) -> Unit ,
    enabled: Boolean = true ,
    horizontalSpacing: Dp = 8.dp ,
    maxItemsEachRow: Int = 4 ,
) {
    val density = LocalDensity.current

    val flowRowItemWidth = remember {
        mutableStateOf(50.dp)
    }

    FlowRow(
        modifier
            .onGloballyPositioned {
                val totalWidth = with(density) {
                    it.size.width.toDp()
                }
                val extraSpace = horizontalSpacing * maxItemsEachRow
                val itemWidthDp = (totalWidth - extraSpace) / maxItemsEachRow
                flowRowItemWidth.value = itemWidthDp
            } ,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing) ,
        maxItemsInEachRow = maxItemsEachRow
    ) {
        items.onEach { type ->
            ExpenseTypeFlowRowItem(
                item = type ,
                selected = type.shortTitle == currentTypeShortTitle ,
                onClick = {
                    //expenseViewModel.onAction(ExpenseActions.OnExpenseTypeChange(type.shortTitle))
                    if (enabled) {
                        onClick(type.shortTitle)
                    }
                } ,
                modifier = Modifier.width(flowRowItemWidth.value)
            )
        }
    }
}

/**
 * An item to represent the Type of expense (Flight, food, etc)
 */
@Composable
private fun ExpenseTypeFlowRowItem(
    item: ExpenseType ,
    onClick: () -> Unit ,
    selected: Boolean = true ,
    modifier: Modifier = Modifier ,
    borderColor: Color = MaterialTheme.colorScheme.onBackground ,
    contentColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.8f) ,
) {
    Column(
        modifier
            .drawBehind {
                if (selected) {
                    drawRoundRect(
                        borderColor ,
                        style = Stroke(5f) ,
                        cornerRadius = CornerRadius(x = 25f , y = 25f) ,
                        size = Size(size.width , size.height)
                    )
                }
            }
            .clickable {
                onClick()
            }
            //.widthIn(50.dp , 80.dp)
            .aspectRatio(1f)
        //.padding(4.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally ,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(item.icon) ,
            contentDescription = "" ,
            modifier = Modifier.size(30.dp) ,
            tint = contentColor
        )
        VerticalSpace(4.dp)
        Text(
            item.title ,
            style = MaterialTheme.typography.bodySmall ,
            textAlign = TextAlign.Center ,
            color = contentColor
        )
    }
}

/**
 * Represents an expense in LazyColumn of Expenses Page
 */
@Composable
internal fun ExpenseEntityItem(
    item: ExpenseEntityUI ,
    onClick: (itemId: Long) -> Unit = {} ,
    modifier: Modifier = Modifier ,
    clickEnabled: Boolean = true ,
    background: Color = MaterialTheme.colorScheme.background ,
    onBackground: Color = MaterialTheme.colorScheme.onSurfaceVariant ,
) {
    Row(
        modifier
            .clip(MaterialTheme.shapes.large)
            .fillMaxWidth()
            .background(background)
            .clickable(
                enabled = clickEnabled ,
                onClick = {
                    onClick(item.id)
                }
            )
            .padding(8.dp) ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column {
            //------- ICON --------
            Box(
                Modifier
                    .background(Color(item.iconBackground))
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(item.icon) ,
                    contentDescription = item.title ?: "Expense item" ,
                    tint = Color.White//onBackground.copy(0.8f)
                )
            }
        }

        //------- TITLE ,SPLIT --------
        Column(
            Modifier
                .weight(1f)
                .padding(4.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                item.title ?: "Untitled" ,
                color = onBackground ,
                textAlign = TextAlign.Center ,
                maxLines = 3
            )
            item.splitInto?.let {
                Text(
                    "Split into $it" ,
                    fontSize = 13.sp ,
                    color = onBackground
                )
            }
        }

        //------- MONEY --------
        Column(
            Modifier.widthIn(min = 70.dp , max = 60.dp) ,
            horizontalAlignment = Alignment.CenterHorizontally ,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.annotatedAmount(
                    amountColor = MaterialTheme.colorScheme.error ,
                    symbolColor = MaterialTheme.colorScheme.onBackground ,
                ) ,
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                color = MaterialTheme.colorScheme.error
            )
            item.splitInto?.let {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Medium ,
                                fontSize = 13.sp ,
                                color = onBackground.copy(0.8f)
                            )
                        ) {
                            val perHead = (item.amount / it)
                            append("$perHead each")
                        }
                    } ,
                    textAlign = TextAlign.Center ,
                    lineHeight = 15.sp
                )
            }
        }

    }
}

/**
 * Formats the expense amount and its symbol
 */
private fun ExpenseEntityUI.annotatedAmount(
    amountColor: Color ,
    symbolColor: Color
): AnnotatedString {

    return buildAnnotatedString {
        pushStyle(
            SpanStyle(
                fontSize = 20.sp ,
                fontWeight = FontWeight.Bold ,
                color = symbolColor
            )
        )
        append(amount.toString())
        append(" $currencySymbol")
        /*
        withStyle(
            style = SpanStyle(
                color = amountColor
            )
        ){
            append(amount.toString())
        }
        withStyle(
            style = SpanStyle(
                color = symbolColor
            )
        ){
            append(" $currencySymbol")
        }

         */


    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SheetPrev() {
    MaterialTheme {
        ExpenseEntityItem(
            item = ExpenseEntityUI(
                id = 2 ,
                amount = 5570 ,
                title = "Breakfast" ,
                icon = R.drawable.fastfood ,
                splitInto = 10 ,
                iconBackground = Color.Green.toArgb() ,
                timestamp = 2L
            )
        )
    }
}