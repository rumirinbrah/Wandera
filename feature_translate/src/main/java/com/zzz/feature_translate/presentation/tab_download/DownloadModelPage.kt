package com.zzz.feature_translate.presentation.tab_download

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.R
import com.zzz.feature_translate.presentation.tab_download.components.TranslateModelItem

@Composable
internal fun DownloadModelPage(
    models : List<TranslationModel>,
    modifier: Modifier = Modifier
) {


    Column(
        modifier.fillMaxSize()
            .padding(16.dp)
    ){

        Image(
            painter = painterResource(R.drawable.downloaded_image) ,
            contentDescription = "Illustration of a man looking at files",
            contentScale = ContentScale.Fit,
            modifier = Modifier.width(200.dp)
                .aspectRatio(1f)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            "You can download over 50 translation models & easily use them without network!",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
        VerticalSpace()
        Text(
            "Browse models",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        LazyColumn(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                models,
                key = {it.name}
            ){model->
                TranslateModelItem(
                    model = model,
                    onLongClick = {},
                    modifier = Modifier.animateItem()
                )
            }
        }
    }
}