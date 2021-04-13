package com.sunrise.app.utils.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.sunrise.app.domain.model.ListItem
import javax.inject.Inject

class ForecastMapper @Inject constructor() : Mapper<List<ListItem>, List<ListItem>> {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapFrom(type: List<ListItem>): List<ListItem> {
        val days = arrayListOf<String>()
        val mappedArray = arrayListOf<ListItem>()

        type.forEachIndexed { _, listItem ->
            if (days.contains(listItem.dtTxt?.substringBefore(" ")).not()) // Add day to days
                listItem.dtTxt?.substringBefore(" ")?.let { days.add(it) }
        }

        days.forEach { day ->
            val array = type.filter { it.dtTxt?.substringBefore(" ").equals(day) }

            val minTemp = array.minByOrNull { it.main?.tempMin ?: 0.0 }?.main?.tempMin
            val maxTemp = array.maxByOrNull { it.main?.tempMax ?: 0.0 }?.main?.tempMax

            array.forEach {
                it.main?.tempMin = minTemp
                it.main?.tempMax = maxTemp
                mappedArray.add(it)
            }
        }

        return mappedArray
            .filter { it.dtTxt?.substringAfter(" ")?.substringBefore(":")?.toInt()!! >= 12 }
            .distinctBy { it.getDay() }
            .toList()
    }
}