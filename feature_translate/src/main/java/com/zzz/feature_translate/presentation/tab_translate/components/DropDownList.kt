package com.zzz.feature_translate.presentation.tab_translate.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zzz.data.translate.model.TranslationModel

@Composable
fun DropDownList(
    title: String ,
    titleInitial: String = "" ,
    items: List<TranslationModel> ,
    onClick: (name: String , modelCode: String) -> Unit ,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val rotation = animateFloatAsState(
        targetValue = if(expanded) 180f else 0f,
    )

    var menuWidth by remember {
        mutableStateOf(0.dp)
    }

    val density = LocalDensity.current

    Column(
        modifier
            .heightIn(max = 300.dp) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier
                .clip(MaterialTheme.shapes.large)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .clickable {
                    expanded = true
                }
                .padding(8.dp)
                .onGloballyPositioned {
                    menuWidth = with(density){
                        it.size.width.toDp()
                    }
                } ,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "$titleInitial $title",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown ,
                contentDescription = "Open menu to select source language" ,
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.graphicsLayer {
                    rotationZ = rotation.value
                }
            )

        }
        DropdownMenu(
            expanded ,
            onDismissRequest = { expanded = false } ,
            modifier = Modifier.width(menuWidth)
                .heightIn(max= 300.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            when{
                items.isEmpty()->{
                    Box(
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 4.dp , vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            "You haven't downloaded any",
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else->{
                    items.onEach {model->
                        Box(
                            Modifier.fillMaxWidth()
                                .clickable {
                                    onClick(model.name,model.languageCode)
                                    expanded = false
                                }
                                .padding(horizontal = 4.dp , vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                model.name,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                }
            }

        }

    }
}