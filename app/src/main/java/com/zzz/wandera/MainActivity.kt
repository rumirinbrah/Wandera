package com.zzz.wandera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.toast.rememberWanderaToastState
import com.zzz.core.theme.WanderaTheme
import com.zzz.data.note.model.ChecklistEntity
import com.zzz.feature_trip.overview.presentation.components.ChecklistItem
import com.zzz.wandera.nav.Navigation
import com.zzz.wandera.ui.ThemeViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.themeState.collectAsStateWithLifecycle()
            val toastState = rememberWanderaToastState()

            WanderaTheme(
                useSystemTheme = false,
                darkThemePref = themeState.isDarkMode
            ) {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val a = innerPadding
//                    Box(
//                        Modifier.fillMaxSize()
//                            .padding(innerPadding),
//                    ){
//
//                        ChecklistItem(
//                            item = ChecklistEntity(
//                                isChecked = true,
//                                title = "Checklist"
//                            ),
//                            onCheck = {_,_->
//
//                            },
//                            onDelete = {},
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//
//                    }
//                }
                Navigation(
                    themeState,
                    toggleDarkMode = {darkMode->
                        themeViewModel.setDarkMode(darkMode)
                    }
                )
            }
        }
    }
}
