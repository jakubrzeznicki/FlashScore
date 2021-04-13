package com.kuba.flashscore.ui.util.networking

import android.app.Application
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultConnectivityManager
@Inject
constructor(
    application: Application
) : ConnectivityManager {
    private val connectionLiveData = ConnectionLiveData(application)

    // observe this in ui
    override val isNetworkAvailable = MutableLiveData<Boolean>(false)

    override fun registerConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.observe(lifecycleOwner, { isConnected ->
            isConnected?.let { isNetworkAvailable.value = it }
        })
    }

    override fun unregisterConnectionObserver(lifecycleOwner: LifecycleOwner){
        connectionLiveData.removeObservers(lifecycleOwner)
    }
}