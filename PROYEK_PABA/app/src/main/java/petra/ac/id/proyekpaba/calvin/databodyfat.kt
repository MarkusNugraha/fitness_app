package petra.ac.id.proyekpaba.calvin

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class databodyfat(
    var age: String,
    var gender: String,
    var weight: String,
    var height: String,
    var neck: String,
    var waist: String,
    var hip: String,
    var navyMethod: String,
    var category: String,
    var fatMass: String,
    var leanBodyMass: String,
    var bmiMethod: String

): Parcelable
