package com.zzz.core.platform.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.getSystemService
import com.zzz.core.domain.network.NetworkObserver
import com.zzz.core.domain.network.NetworkSpecs
import com.zzz.core.domain.network.NetworkType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class AndroidNetworkObserver(
    context: Context
) : NetworkObserver {

    private val networkManager = context.getSystemService<ConnectivityManager>()
    private var loggingEnabled = false

    override val isConnected: Flow<NetworkSpecs>
        get() = callbackFlow {
            log {
                "Building callback flow"
            }
            val callback = object : NetworkCallback() {

                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

                    networkManager?.getNetworkCapabilities(network)?.let { capability ->

                        trySend(NetworkSpecs(true , capability.getNetworkType()))

                    } ?: run {
                        //capabilities not available -_-
                        trySend(NetworkSpecs(true , NetworkType.UNKNOWN))
                    }

                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    trySend(NetworkSpecs(false))
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(NetworkSpecs(false))
                }

                override fun onCapabilitiesChanged(
                    network: Network ,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network , networkCapabilities)
                    //try send
                    trySend(NetworkSpecs(true,networkCapabilities.getNetworkType()))
                }
            }
            log {
                "Callback registered successfully!"
            }
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                .build()

            networkManager?.requestNetwork(networkRequest , callback)

            awaitClose {
                log {
                    "REMOVING Callback..."
                }
                networkManager?.unregisterNetworkCallback(callback)
            }

        }

    /**
     * Gives network type for the NetworkCapabilities.
     *
     * For supported types, see [NetworkType]
     */
    private fun NetworkCapabilities.getNetworkType(): NetworkType {
        return when {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> NetworkType.WIFI
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> NetworkType.MOBILE_DATA
            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> NetworkType.ETHERNET
            else -> NetworkType.UNKNOWN
        }
    }

    private fun log(msg: () -> String) {
        if (loggingEnabled) {
            Log.d("networkObs" , msg())
        }
    }


}