package petra.ac.id.proyekpaba.Billy


import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class DailyCalorieData(
    val age: Int = 0,
    val gender: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val mode: String = "",
    val weightLossCalories: Double = 0.0,
    val date: Date = Date(),
    var documentId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readDouble(),
        Date(parcel.readLong()),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(age)
        parcel.writeString(gender)
        parcel.writeInt(height)
        parcel.writeInt(weight)
        parcel.writeString(mode)
        parcel.writeDouble(weightLossCalories)
        parcel.writeLong(date.time)
        parcel.writeString(documentId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DailyCalorieData> {
        override fun createFromParcel(parcel: Parcel): DailyCalorieData {
            return DailyCalorieData(parcel)
        }

        override fun newArray(size: Int): Array<DailyCalorieData?> {
            return arrayOfNulls(size)
        }
    }
}
