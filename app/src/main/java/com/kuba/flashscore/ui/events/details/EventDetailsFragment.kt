package com.kuba.flashscore.ui.events.details

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kuba.flashscore.R
import com.kuba.flashscore.adapters.EventDetailAdapter
import com.kuba.flashscore.databinding.FragmentEventDetailsBinding
import com.kuba.flashscore.local.models.incident.INCIDENTTYPE
import com.kuba.flashscore.local.models.incident.Incident
import com.kuba.flashscore.local.models.incident.IncidentHeader
import com.kuba.flashscore.local.models.incident.IncidentItem
import com.kuba.flashscore.network.models.events.EventDto
import com.kuba.flashscore.other.Constants.CARD_YELLOW
import com.kuba.flashscore.other.Constants.INCIDENT_HEADER_2_HALF
import com.kuba.flashscore.other.Constants.INCIDENT_HEADER_OVERTIME
import com.kuba.flashscore.other.Constants.MATCH_STATUS_FINISHED
import com.kuba.flashscore.other.Constants.RESULT_NULL_TO_NULL
import com.kuba.flashscore.other.Constants.SUBSTITUTION_DIVIDER


class EventDetailsFragment(private val event: EventDto) :
    Fragment(R.layout.fragment_event_details) {

    private var _binding: FragmentEventDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventDetailAdapter: EventDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEventDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        setInformationAboutRefereeAndStadium()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (event.matchStatus == MATCH_STATUS_FINISHED) {
            binding.constraintLayoutEventDetailsInformationAboutMatch.visibility = View.VISIBLE
            setInformationAboutFirstHalf()
            setupRecyclerView()
        } else {
            binding.constraintLayoutEventDetailsMessageBeforeMatch.visibility = View.VISIBLE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun setInformationAboutFirstHalf() {
        binding.apply {
            textViewInformationAboutFirstHalfResult.text =
                "${event.matchHometeamHalftimeScore} - ${event.matchAwayteamHalftimeScore}"
        }
    }

    private fun setInformationAboutRefereeAndStadium() {
        binding.apply {
            textViewRefereeName.text = event.matchReferee
            textViewStadiumName.text = event.matchStadium
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewEventDetailFirstHour.apply {
            eventDetailAdapter = EventDetailAdapter(requireContext())
            eventDetailAdapter.incidents = addHeadersToIncidentItemList()
            adapter = eventDetailAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }


    private fun addHeadersToIncidentItemList(): List<Incident> {
        val incidentItemList = sortAllIncidentItemListByTimeListByTime()
        val secondHalfHeader =
            incidentItemList.indexOfFirst { it.time.length == 2 && it.time.toInt() >= 46 }
        val overtimeHeader =
            incidentItemList.indexOfFirst { it.time.length == 2 && it.time.toInt() >= 91 }

        val incidentList = mutableListOf<Incident>()
        for (index in incidentItemList.indices) {
            if (index == secondHalfHeader) {
                incidentList.add(
                    IncidentHeader(
                        INCIDENT_HEADER_2_HALF,
                        incidentItemList[index].score ?: RESULT_NULL_TO_NULL
                    )
                )
            }
            if (index == overtimeHeader) {
                incidentList.add(
                    IncidentHeader(
                        INCIDENT_HEADER_OVERTIME,
                        incidentItemList[index].score ?: RESULT_NULL_TO_NULL
                    )
                )
            }
            incidentList.add(incidentItemList[index])
        }
        return incidentList
    }

    private fun sortAllIncidentItemListByTimeListByTime(): List<IncidentItem> {
        val incidentList = mutableListOf<IncidentItem>()
        incidentList.apply {
            addAll(getCardsFromEventDtoToIncidentList())
            addAll(getGoalscorersFromEventDtoToIncidentList())
            addAll(getSubstitutionsFromEventDtoToIncidentItemList())
            sortBy { it.time }
        }
        return incidentList
    }

    private fun getGoalscorersFromEventDtoToIncidentList(): List<IncidentItem> {
        val goalscorersList = mutableListOf<IncidentItem>()
        goalscorersList.addAll(event.goalscorer.map { goal ->
            IncidentItem(
                if (goal.homeScorerId.isNotEmpty()) goal.homeScorer else goal.awayScorer,
                if (goal.homeScorerId.isNotEmpty()) goal.homeAssist else goal.awayAssist,
                goal.time,
                INCIDENTTYPE.GOAL,
                goal.homeScorerId.isNotEmpty(),
                goal.score
            )
        })
        return goalscorersList
    }


    private fun getCardsFromEventDtoToIncidentList(): List<IncidentItem> {
        val cardsList = mutableListOf<IncidentItem>()
        cardsList.addAll(event.cards.map { card ->
            IncidentItem(
                if (card.homeFault.isNotEmpty()) card.homeFault else card.awayFault,
                "",
                card.time,
                if (card.card == CARD_YELLOW) INCIDENTTYPE.YELLOW_CARD else INCIDENTTYPE.RED_CARD,
                card.homeFault.isNotEmpty()
            )
        })
        return cardsList
    }

    private fun getSubstitutionsFromEventDtoToIncidentItemList(): List<IncidentItem> {
        val substitutionsList = mutableListOf<IncidentItem>()
        substitutionsList.apply {
            addAll(event.substitutions.home.map { sub ->
                val subPlayer = sub.substitution.split(SUBSTITUTION_DIVIDER)
                IncidentItem(
                    subPlayer[1].trim(),
                    subPlayer[0].trim(),
                    sub.time,
                    INCIDENTTYPE.SUBSTITUTION,
                    true
                )
            })
            addAll(event.substitutions.away.map { sub ->
                val subPlayer = sub.substitution.split(SUBSTITUTION_DIVIDER)
                IncidentItem(
                    subPlayer[1].trim(),
                    subPlayer[0].trim(),
                    sub.time,
                    INCIDENTTYPE.SUBSTITUTION,
                    false
                )
            })
        }
        return substitutionsList
    }
}