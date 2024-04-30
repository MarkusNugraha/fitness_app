package petra.ac.id.proyekpaba.markus

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BMI_Result(
    val namaAkun: String,
    val age: String,
    val weight: String,
    val height: String,
    val scoreBMI: String,
    val statusBMI: String,
    val date: String
): Parcelable


