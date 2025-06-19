package com.zzz.feature_translate.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.VerticalSpace
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.R
import com.zzz.feature_translate.presentation.components.TranslateTabRow
import com.zzz.feature_translate.presentation.tab_download.DownloadModelPage
import com.zzz.feature_translate.presentation.viewmodel.TranslationViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TranslateRoot(
    translationViewModel: TranslationViewModel = koinViewModel()
) {
    val models by translationViewModel.models.collectAsStateWithLifecycle()

    TranslateHome(
        models
    )
}

@Composable
private fun TranslateHome(
    models : List<TranslationModel>,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val pagerState = rememberPagerState {
        2
    }



    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            Modifier
                .fillMaxSize()
        ) {
            TranslateTabRow(
                currentTab = pagerState.currentPage,
                onTabChange = {tab->
                    scope.launch {
                        pagerState.animateScrollToPage(tab)
                    }
                }
            )

            HorizontalPager(
                pagerState ,
                modifier = Modifier.fillMaxWidth(),
            ) {index->
                when(index){
                    0->{
                        DownloadModelPage(models)
                    }
                    1->{
                        Column(Modifier.fillMaxSize()){
                            Text("Translate")
                        }
                    }
                }
            }
        }
    }
}