package com.sunrise.app.ui.main

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.sunrise.app.core.setWeatherIcon
import com.sunrise.app.databinding.ForecastItemLayoutBinding
import com.sunrise.app.domain.model.ListItem

class ForecastAdapter(var list: List<ListItem>?) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ForecastItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ForecastViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(list?.get(position))
    }

    override fun getItemCount() = list?.size ?: 0

    class ForecastViewHolder(val binding: ForecastItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(listItem: ListItem?) {

            listItem?.let {
                setWeatherIcon(binding.currentWeatherIcon, listItem.weather?.firstOrNull()?.icon)
                binding.currentWeatherDay.text = listItem.getDay()
                binding.currentWeatherDescription.text = listItem.weather?.firstOrNull()?.getDescriptionText()
                binding.currentWeatherTemperature.text = listItem.main?.getTempString()
            }
        }
    }
}