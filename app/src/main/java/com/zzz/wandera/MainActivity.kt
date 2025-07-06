package com.zzz.wandera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.toast.WanderaToast
import com.zzz.core.presentation.toast.rememberWanderaToastState
import com.zzz.wandera.nav.Navigation
import com.zzz.core.theme.WanderaTheme
import com.zzz.core.theme.blueToastSweep
import com.zzz.core.theme.greenToastSweep
import com.zzz.core.theme.redToastSweep
import com.zzz.feature_translate.presentation.TranslateRoot
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
//                        Button(
//                            onClick = {
//                                toastState.showToast("Note saved!", blueToastSweep)
//                            },
//                            modifier = Modifier.align(Alignment.Center)
//                        ) {
//                            Text("Show toast")
//                        }
//                        WanderaToast(
//                            state = toastState,
//                            Modifier.align(Alignment.BottomCenter),
//                        )
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
