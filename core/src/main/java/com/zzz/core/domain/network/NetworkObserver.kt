package com.zzz.core.domain.network

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {

    val isConnected : Flow<NetworkSpecs>

}


