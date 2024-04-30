package petra.ac.id.proyekpaba

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataDiri(
    var name: String,
    var age: String,
    var gender: String,
    var weight: String,
    var height: String,
    var neck: String,
    var waist: String,
    var hip: String,
): Parcelable
