package com.zzz.core.domain.network

/**
 * Gives info about network connectivity.
 *
 * See supported network types [NetworkType]
 */
data class NetworkSpecs(
    val connected: Boolean = false ,
    val type: NetworkType = NetworkType.UNKNOWN
)

enum class NetworkType {
    UNKNOWN ,
    WIFI ,
    MOBILE_DATA,
    ETHERNET
}
