package com.zzz.wandera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zzz.core.theme.WanderaTheme
import com.zzz.feature_trip.share.presentation.ImportTripRoot
import com.zzz.wandera.presentation.nav.Navigation
import com.zzz.wandera.ui.ThemeViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
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
                useSystemTheme = false,
                darkThemePref = themeState.isDarkMode
            ) {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    val a = innerPadding
//                    Box(
//                        Modifier
//                            .fillMaxSize()
//                            //.statusBarsPadding()
////                            .padding(innerPadding)
//                        ,
//                    ){
//                        ImportTripRoot()
//                    }
//                }
                Navigation(
                    themeState,
                    toggleDarkMode = {darkMode->
                        themeViewModel.setDarkMode(darkMode)
                    },
                    tripJsonUri = jsonUri
                )
            }
        }
    }
}
fun ComponentActivity.checkJsonIntent(onJsonReceived : (Uri)->Unit){
    if(intent!=null){
        when(intent.action){
            Intent.ACTION_SEND->{
                val jsonUri = intent.getParcelableExtra(Intent.EXTRA_STREAM) as? Uri
                jsonUri?.let {
                    onJsonReceived(jsonUri)
                }
            }
            else->Unit
        }
    }else{
        return
    }
}