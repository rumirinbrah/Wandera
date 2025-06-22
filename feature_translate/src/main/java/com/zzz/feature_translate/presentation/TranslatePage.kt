package com.zzz.feature_translate.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.snackbar.WanderaSnackbar
import com.zzz.core.presentation.events.ObserveAsEvents
import com.zzz.core.presentation.events.UIEvents
import com.zzz.core.presentation.snackbar.showWanderaSnackbar
import com.zzz.core.theme.failureRed
import com.zzz.core.theme.successGreen
import com.zzz.data.translate.model.TranslationModel
import com.zzz.feature_translate.presentation.components.TranslateTabRow
import com.zzz.feature_translate.presentation.tab_download.DownloadModelPage
import com.zzz.feature_translate.presentation.tab_download.components.FirstTimeInstructions
import com.zzz.feature_translate.presentation.tab_translate.TranslateTextPage
import com.zzz.feature_translate.presentation.viewmodel.TranslateAction
import com.zzz.feature_translate.presentation.viewmodel.TranslateState
import com.zzz.feature_translate.presentation.viewmodel.TranslationViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TranslateRoot(
    translationViewModel: TranslationViewModel = koinViewModel()
) {
    val models by translationViewModel.models.collectAsStateWithLifecycle()
    val state by translationViewModel.state.collectAsStateWithLifecycle()

    val events = translationViewModel.events

    TranslateHome(
        state = state,
        models = models,
        events = events,
        onAction = {action->
            translationViewModel.onAction(action)
        }
    )
}

@Composable
private fun TranslateHome(
    state : TranslateState,
    models: List<TranslationModel> ,
    events : Flow<UIEvents>,
    onAction: (TranslateAction) -> Unit ,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val snackbarState = remember {  SnackbarHostState() }

    val pagerState = rememberPagerState {
        2
    }
    var firstTime by remember { mutableStateOf(true) }



    ObserveAsEvents(events) {event->
        when(event){
            is UIEvents.Error -> {
                scope.launch {
                    snackbarState.showWanderaSnackbar(
                        message = event.errorMsg,
                        background = failureRed,
                        onBackground = Color.White
                    )
                }
            }

            is UIEvents.SuccessWithMsg->{
                scope.launch {
                    snackbarState.showWanderaSnackbar(
                        message = event.msg,
                        background = successGreen,
                        onBackground = Color.White
                    )

                }
            }
            else->{}
        }
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
            //TABS
            TranslateTabRow(
                currentTab = pagerState.currentPage ,
                onTabChange = { tab ->
                    scope.launch {
                        pagerState.animateScrollToPage(tab)
                    }
                }
            )
            //PAGER
            HorizontalPager(
                pagerState ,
                modifier = Modifier.fillMaxWidth() ,
            ) { index ->
                when (index) {
                    0 -> {
                        DownloadModelPage(
                            state = state,
                            models,
                            onAction = onAction,
                        )
                    }

                    1 -> {
                        TranslateTextPage(
                            state,
                            models,
                            onAction
                        )
                    }
                }
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
        if(firstTime){
            FirstTimeInstructions(
                onFinish = {firstTime = false},
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }//box
}


