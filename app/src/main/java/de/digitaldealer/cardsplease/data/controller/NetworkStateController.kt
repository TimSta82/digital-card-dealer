@file:OptIn(FlowPreview::class)

package de.digitaldealer.cardsplease.data.controller

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.net.NetworkRequest
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.core.component.KoinComponent

/**
 * This class monitors the currently connected network(s). When we connect to a WIFI network, the Cellular network is lost, therefore
 * [_activeNetworks] will mostly contain just the active network.
 * E.g: Connected to Cellular: 1 network available. Enabling Wifi: Second network is added (onAvailable), but immediately first is dropped (onLost).
 * E.g: Connected to Wifi: 1 network available. Disabling Wifi: network is dropped (onLost), but immediately cellular is found (onAvailable)
 *
 * To avoid [_activeNetworks] being empty and triggering a false positive alert, we debounce the flow for one second, this way the immediate changes
 * in network availability do not trigger alerts.
 */
class NetworkStateController(val context: Context) : KoinComponent {

    private val _activeNetworksSyncMap = mutableMapOf<Int, ActiveNetwork>()
    private val _activeNetworks = MutableStateFlow<Set<ActiveNetwork>>(setOf())
    val activeNetworks = _activeNetworks.debounce(1000).distinctUntilChanged()

    val isConnectedToInternet: Boolean
        get() = _activeNetworksSyncMap.any { activeNetwork -> activeNetwork.value.hasInternetAccess }

    fun register() {
        runCatching {
            getConnectivityManager()?.registerNetworkCallback(NetworkRequest.Builder().build(), NetworkStateChangedCallback())
        }.getOrElse(Throwable::printStackTrace)
    }

    private inner class NetworkStateChangedCallback : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            getConnectivityManager()?.let { connectivityManager ->
                connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
                    updateActiveNetworks(network.hashCode(), networkCapabilities)
                }
            }
        }

        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) = updateActiveNetworks(network.hashCode(), networkCapabilities)

        override fun onLost(network: Network) {
            _activeNetworksSyncMap.remove(network.hashCode())
            _activeNetworks.value = _activeNetworksSyncMap.values.toSet()
        }

        private fun updateActiveNetworks(networkHashCode: Int, networkCapabilities: NetworkCapabilities) {
            val isWiFi = networkCapabilities.hasTransport(TRANSPORT_WIFI)
            val isCellular = networkCapabilities.hasTransport(TRANSPORT_CELLULAR)
            val hasInternetAccess = networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET)
            val activeNetwork = ActiveNetwork(hasInternetAccess, isWiFi, isCellular)

            _activeNetworksSyncMap[networkHashCode] = activeNetwork
            _activeNetworks.value = _activeNetworksSyncMap.values.toSet()
        }
    }

    private fun getConnectivityManager(): ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

    data class ActiveNetwork(
        val hasInternetAccess: Boolean = false,
        val isWiFi: Boolean = false,
        val isCellular: Boolean = false
    )
}
