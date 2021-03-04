package com.kuba.flashscore.other

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

object ViewModelUtils {

     fun <A, B, C> zipTripleLiveData(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>
    ): LiveData<Triple<A, B, C>> {
        return MediatorLiveData<Triple<A, B, C>>().apply {
            var lastA: A? = null
            var lastB: B? = null
            var lastC: C? = null

            fun update() {
                val localLastA = lastA
                val localLastB = lastB
                val localLastC = lastC
                if (localLastA != null && localLastB != null && localLastC != null)
                    this.value = Triple(localLastA, localLastB, localLastC)
            }

            addSource(a) {
                lastA = it
                update()
            }
            addSource(b) {
                lastB = it
                update()
            }
            addSource(c) {
                lastC = it
                update()
            }
        }
    }

    fun <A, B> zipPairLiveData(
        a: LiveData<A>,
        b: LiveData<B>
    ): LiveData<Pair<A, B>> {
        return MediatorLiveData<Pair<A, B>>().apply {
            var lastA: A? = null
            var lastB: B? = null

            fun update() {
                val localLastA = lastA
                val localLastB = lastB
                if (localLastA != null && localLastB != null)
                    this.value = Pair(localLastA, localLastB)
            }

            addSource(a) {
                lastA = it
                update()
            }
            addSource(b) {
                lastB = it
                update()
            }
        }
    }
}