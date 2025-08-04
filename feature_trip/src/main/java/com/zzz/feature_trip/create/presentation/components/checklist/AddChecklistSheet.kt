package com.zzz.feature_trip.create.presentation.components.checklist

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.SheetState
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.core.presentation.components.WanderaBottomSheet
import com.zzz.core.presentation.components.WanderaSheetState
import com.zzz.core.presentation.text_field.RoundedTextField
import com.zzz.data.note.model.ChecklistEntity
import com.zzz.feature_trip.create.presentation.components.ChecklistItemViewOnly
import kotlinx.coroutines.launch

/**
 * Sheet for adding/removing items from the checklist.
 * @param checklist List
 * @param onAdd Returns title when item needs to be added
 * @param onDelete To delete an item. Returns id
 * @param onClosed Called when sheet is dismissed
 * @param sheetColor Color of the sheet
 * @author zyzz
 */
@Composable
fun AddChecklistSheet(
    modifier: Modifier = Modifier ,
    state: WanderaSheetState ,
    checklist: List<ChecklistEntity> = emptyList() ,
    onAdd: (title: String) -> Unit ,
    onDelete: (itemId: Long) -> Unit ,
    onClosed: () -> Unit = {} ,
    sheetColor: Color = MaterialTheme.colorScheme.surface ,
    contentColor: Color = MaterialTheme.colorScheme.onBackground.copy(0.8f)
) {
    val scope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val lazyListState = rememberLazyListState()

    var text by remember { mutableStateOf("") }


    LaunchedEffect(state.visible) {
        if (state.visible) {
            text = ""
            scope.launch {
                state.animateTo(SheetState.HALF_EXPANDED)
            }
        }
    }
    LaunchedEffect(checklist) {
        lazyListState.scrollBy(lazyListState.layoutInfo.viewportEndOffset.toFloat())
    }


    WanderaBottomSheet(
        state = state ,
        onSheetClosed = onClosed ,
        modifier = modifier
            .fillMaxSize()
            .background(sheetColor) ,
        sheetColor = sheetColor
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                //.weight(1f)
                //.imePadding()
            ,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Add items to your checklist!" ,
                fontSize = 16.sp ,
                fontWeight = FontWeight.Bold ,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            VerticalSpace(5.dp)
            RoundedTextField(
                value = text ,
                onValueChange = {
                    text = it
                } ,
                placeholder = "E.g. Tripod, Perfume" ,
                imeAction = ImeAction.Done ,
                onDone = {
                    when {
                        text.isBlank() -> {
                            keyboardController?.hide()
                            text = ""
                        }

                        else -> {
                            onAdd(text.trim())
                            text = ""
                        }
                    }
                } ,
                hideKeyboardOnDone = false,
                modifier = Modifier.fillMaxWidth() ,
                background = MaterialTheme.colorScheme.surfaceContainer ,
                singleLine = true
            )

            VerticalSpace(5.dp)

            LazyColumn(
                Modifier
                    .fillMaxWidth() ,
                //    .heightIn(max = 500.dp)
                verticalArrangement = Arrangement.spacedBy(8.dp) ,
                state = lazyListState
            ) {
                items(
                    checklist ,
                    key = { it.id }
                ) { item ->
                    ChecklistItemViewOnly(
                        item ,
                        onDelete = {
                            onDelete(it)
                        } ,
                        modifier = Modifier.animateItem()
                    )
                }
            }

            VerticalSpace(30.dp)
        }
    }

}

