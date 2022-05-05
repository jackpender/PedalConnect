package ie.wit.pedalconnect.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PresetModel(
    var title: String = "",
    var volume: Double = 0.0,
    var level: Double = 0.0,
    var tone: Double = 0.0,
    var id: Long = 0,
): Parcelable