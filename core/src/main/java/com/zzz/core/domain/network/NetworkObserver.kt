package com.zzz.core.domain.network

import kotlinx.coroutines.flow.Flow

/**
 * Network observer interface
 */
interface NetworkObserver {

    /**
     * Variable represents whether network is connected. Also provides the type of network.
     * See [NetworkSpecs] for more info.
     */
    val isConnected : Flow<NetworkSpecs>

}


