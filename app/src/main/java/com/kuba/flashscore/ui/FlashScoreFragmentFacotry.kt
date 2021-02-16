package com.kuba.flashscore.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.kuba.flashscore.adapters.CountryAdapter
import com.kuba.flashscore.ui.country.CountryFragment
import javax.inject.Inject

class FlashScoreFragmentFacotry @Inject constructor(
    private val countryAdapter: CountryAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            CountryFragment::class.java.name -> CountryFragment(countryAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}