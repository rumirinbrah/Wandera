package com.zzz.wandera

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.presentation.components.TestLazyColumn
import com.zzz.core.theme.WanderaTheme
import com.zzz.feature_trip.overview.presentation.components.MarkedAsDoneRoot
import com.zzz.wandera.presentation.nav.Navigation
import com.zzz.wandera.ui.ThemeViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel = koinViewModel<ThemeViewModel>()
            val themeState by themeViewModel.themeState.collectAsStateWithLifecycle()
            var jsonUri = remember<Uri?> { null }

            checkJsonIntent {
                println("The uri is not null")
                jsonUri = it
            }

            WanderaTheme(
                useSystemTheme = false ,
                darkThemePref = themeState.isDarkMode
            ) {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val a = innerPadding
//                    Box(
//                        Modifier
//                            .fillMaxSize()
//                            //.statusBarsPadding()
//                            .padding(innerPadding)
//                        ,
//                    ){
//                        TestLazyColumn()
//                    }
//                }
                Navigation(
                    themeState ,
                    toggleDarkMode = { darkMode ->
                        themeViewModel.setDarkMode(darkMode)
                    } ,
                    tripJsonUri = jsonUri
                )
            }
        }
    }
}

fun ComponentActivity.checkJsonIntent(onJsonReceived: (Uri) -> Unit) {
    try {
        if (intent != null && intent.action == Intent.ACTION_SEND) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val jsonUri =
                    intent.getParcelableExtra(Intent.EXTRA_STREAM , Uri::class.java)
                jsonUri?.let {
                    onJsonReceived(jsonUri)
                }
            } else {
                val jsonUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
                jsonUri?.let {
                    onJsonReceived(jsonUri)
                }
            }
        } else {
            return
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return
    }

}