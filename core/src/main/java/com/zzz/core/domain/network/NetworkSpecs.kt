package com.zzz.core.domain.network

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
