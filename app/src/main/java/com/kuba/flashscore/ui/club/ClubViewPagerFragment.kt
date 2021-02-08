package com.kuba.flashscore.ui.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.kuba.flashscore.R
import com.kuba.flashscore.databinding.FragmentClubViewPagerBinding
import timber.log.Timber

class ClubViewPagerFragment : Fragment(R.layout.fragment_club_view_pager) {

    private var _binding: FragmentClubViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentClubViewPagerBinding.inflate(inflater, container, false)
        val view = binding.root

        val teamId = ClubViewPagerFragmentArgs.fromBundle(requireArguments()).teamId
        val clubFragmentList = arrayListOf<Fragment>(
            PlayersFragment(teamId!!)
        )

        val clubViewPagerAdapter = ClubViewPagerAdapter(
            clubFragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPagerClub.adapter = clubViewPagerAdapter

        TabLayoutMediator(
            binding.tabLayoutClub, binding.viewPagerClub
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Sklad"
                }
            }
        }.attach()

        return view
    }

}