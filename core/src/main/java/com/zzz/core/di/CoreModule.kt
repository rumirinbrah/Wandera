package com.zzz.core.di

import com.zzz.core.domain.network.NetworkObserver
import com.zzz.core.platform.network.AndroidNetworkObserver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val coreModule = module {
    single<NetworkObserver> {
        AndroidNetworkObserver(androidContext())
    }
}


