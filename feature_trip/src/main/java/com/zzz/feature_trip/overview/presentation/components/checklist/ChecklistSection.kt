package com.zzz.feature_trip.overview.presentation.components.checklist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zzz.core.presentation.components.LineProgressBar
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.data.note.model.ChecklistEntity

@Composable
internal fun ChecklistSection(
    modifier: Modifier = Modifier,
    checklist : List<ChecklistEntity>,
    onCheck : (id : Long , checked : Boolean)->Unit,
    onDelete : (id : Long) ->Unit,
    checklistProgress : Float = 1f,
    collapsed : Boolean = true,
    onCollapse : ()->Unit,
    trapeziumChecklist : Boolean = true,
) {
    Column(
        modifier
    ) {

        ChecklistHeader(
            collapsed = collapsed ,
            onCollapse = {
                onCollapse()
            }
        )

        VerticalSpace(8.dp)

        AnimatedVisibility(!collapsed) {

            when{
                checklist.isEmpty()->{
                    Text(
                        "Seems like you haven't created a checklist..." ,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                    )
                }
                else->{
                    Column (
                        Modifier.fillMaxWidth()
                    ) {
                        LineProgressBar(
                            modifier = Modifier.fillMaxWidth(),
                            progress = checklistProgress,
                        )
                        VerticalSpace(10.dp)

                        LazyColumn(
                            Modifier
                                .fillMaxWidth()
                                .heightIn(max = 400.dp) ,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = checklist ,
                                key = { it.id }
                            ) { item ->
                                ChecklistItemRoot(
                                    item = item ,
                                    onCheck = { itemId , checked ->
                                        onCheck(itemId,checked)
                                    } ,
                                    onDelete = { itemId ->
                                        onDelete(itemId)
                                    } ,
                                    modifier = Modifier.animateItem() ,
                                    trapeziumChecklist = trapeziumChecklist
                                )

                            }
                        }
                    }

                }
            }


        }


    }
}