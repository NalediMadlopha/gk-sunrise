package domain.model

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

private const val DELIMITER = "."
private const val DEGREE_SYMBOL = "°"

@Parcelize
@JsonClass(generateAdapter = true)
data class Main(

    @Json(name = "temp")
    val temp: Double?,

    @Json(name = "temp_min")
    var tempMin: Double?,

    @Json(name = "grnd_level")
    val grndLevel: Double?,

    @Json(name = "temp_kf")
    val tempKf: Double?,

    @Json(name = "humidity")
    val humidity: Int?,

    @Json(name = "pressure")
    val pressure: Double?,

    @Json(name = "sea_level")
    val seaLevel: Double?,

    @Json(name = "temp_max")
    var tempMax: Double?
) : Parcelable {

    fun getTempString(): String {
        return temp.toString().substringBefore(DELIMITER) + DEGREE_SYMBOL
    }

    fun getHumidityString(): String {
        return humidity.toString() + "°"
    }

    fun getTempMinString(): String {
        return tempMin.toString().substringBefore(DELIMITER) + DEGREE_SYMBOL
    }

    fun getTempMaxString(): String {
        return tempMax.toString().substringBefore(DELIMITER) + DEGREE_SYMBOL
    }
}