package com.kuba.flashscore.ui.util.networking
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


/**
 * Send a ping to googles primary DNS.
 * If successful, that means we have internet.
 */
object DoesNetworkHaveInternet {

    // Make sure to execute this on a background thread.
    fun execute(): Boolean {
        return try {
            //Timber.d("JUREK NETWORK PINGING google.")
            val socket = Socket()
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            //Timber.d("JUREK NETWORK PINGING success.")
            true
        } catch (e: IOException) {
          //  Timber.d("JUREK NETWORK No internet connection. ${e}")
            false
        }
    }
}