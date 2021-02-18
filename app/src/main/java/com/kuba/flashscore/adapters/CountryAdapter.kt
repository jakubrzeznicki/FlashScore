package com.kuba.flashscore.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.kuba.flashscore.databinding.CountryItemBinding
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.ui.country.CountryFragmentDirections
import javax.inject.Inject

class CountryAdapter @Inject constructor(private val glide: RequestManager) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(val binding: CountryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<CountryEntity>() {
        override fun areItemsTheSame(oldItem: CountryEntity, newItem: CountryEntity): Boolean {
            return oldItem.countryId == newItem.countryId
        }

        override fun areContentsTheSame(oldItem: CountryEntity, newItem: CountryEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var country: List<CountryEntity>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = CountryItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.binding.apply {
            val country = country[position]
            Glide.with(holder.itemView).load(country.countryLogo).into(imageViewCountryFlag)
            textViewCountryName.text = country.countryName

            holder.itemView.setOnClickListener {
                val action =
                    CountryFragmentDirections.actionCountryFragmentToLeagueFragment(
                        country
                    )
                it.findNavController().navigate(action)
            }
        }
    }


    override fun getItemCount(): Int = country.size


}
