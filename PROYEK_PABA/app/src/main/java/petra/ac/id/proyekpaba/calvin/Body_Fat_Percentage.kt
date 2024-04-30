package petra.ac.id.proyekpaba.calvin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import petra.ac.id.proyekpaba.Home
import petra.ac.id.proyekpaba.R

class Body_Fat_Percentage : AppCompatActivity() {

    // Untuk menampung hasil response API
    data class BodyFatInfo(
        val navyMethod: Double,
        val category: String,
        val fatMass: Double,
        val leanBodyMass: Double,
        val bmiMethod: Double
    )

    // Untuk request API
    private suspend fun makeApiRequest(
        age: Int,
        gender: String,
        weight: Int,
        height: Int,
        neck: Int,
        waist: Int,
        hip: Int
    ): BodyFatInfo = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val url =
            "https://fitness-calculator.p.rapidapi.com/bodyfat?age=$age&gender=$gender&weight=$weight&height=$height&neck=$neck&waist=$waist&hip=$hip"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", "ebf6499ff9msh042ad040d39fd71p1a58cajsn4ca87c1eff75")
            .addHeader("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body()?.string() ?: "No response"

        // Untuk mengambil hasil response API
        // Parse the JSON response
        val jsonObject = JSONObject(responseBody)
        val dataObject = jsonObject.getJSONObject("data")

        // Extract relevant information
        val navyMethod = dataObject.getDouble("Body Fat (U.S. Navy Method)")
        val category = dataObject.getString("Body Fat Category")
        val fatMass = dataObject.getDouble("Body Fat Mass")
        val leanBodyMass = dataObject.getDouble("Lean Body Mass")
        val bmiMethod = dataObject.getDouble("Body Fat (BMI method)")

        return@withContext BodyFatInfo(navyMethod, category, fatMass, leanBodyMass, bmiMethod)
    }

    private val db = FirebaseFirestore.getInstance()


    /*
    private suspend fun makeApiRequest(): String = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://fitness-calculator.p.rapidapi.com/bodyfat?age=25&gender=male&weight=70&height=178&neck=50&waist=96&hip=92")
            .get()
            .addHeader("X-RapidAPI-Key", "d7d9e0b0d0msh4ca54b57a1aaaadp117c7cjsnc24d5d92480b")
            .addHeader("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
            .build()

        val response = client.newCall(request).execute()
        return@withContext response.body()?.string() ?: "No response"
    }
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_fat_percentage)

        /*// Launch a coroutine to perform the API request in the background
        GlobalScope.launch(Dispatchers.Main) {
            val apiResponse = makeApiRequest()
            println(apiResponse)
        }*/

        val edt_age = findViewById<EditText>(R.id.ed_age)
        val edt_weight = findViewById<EditText>(R.id.ed_weight)
        val edt_height = findViewById<EditText>(R.id.ed_height)
        val edt_neck = findViewById<EditText>(R.id.ed_neck)
        val edt_waist = findViewById<EditText>(R.id.ed_waist)
        val edt_hip = findViewById<EditText>(R.id.ed_hip)

        lateinit var genderValue: String

        val rdb_male = findViewById<RadioButton>(R.id.rb_male)
        val rdb_female = findViewById<RadioButton>(R.id.rb_female)

        val btn_plusAge = findViewById<Button>(R.id.btn_plusAge)
        val btn_minusAge = findViewById<Button>(R.id.btn_minusAge)
        val btn_plusWeight = findViewById<Button>(R.id.btn_plusWeight)
        val btn_minusWeight = findViewById<Button>(R.id.btn_minusWeight)
        val btn_plusHeight = findViewById<Button>(R.id.btn_plusHeight)
        val btn_minusHeight = findViewById<Button>(R.id.btn_minusHeight)
        val btn_plusNeck = findViewById<Button>(R.id.btn_plusNeck)
        val btn_minusNeck = findViewById<Button>(R.id.btn_minusNeck)
        val btn_plusWaist = findViewById<Button>(R.id.btn_plusWaist)
        val btn_minusWaist = findViewById<Button>(R.id.btn_minusWaist)
        val btn_plusHip = findViewById<Button>(R.id.btn_plusHip)
        val btn_minusHip = findViewById<Button>(R.id.btn_minusHip)

        val score_navymethod = findViewById<TextView>(R.id.score_navyMethod)
        val score_category = findViewById<TextView>(R.id.score_Category)
        val score_fatmass = findViewById<TextView>(R.id.score_fatMass)
        val score_leanbodymass = findViewById<TextView>(R.id.score_leanBodyMass)
        val score_bmimethod = findViewById<TextView>(R.id.score_bmiMethod)

        val btn_back = findViewById<Button>(R.id.btn_backhome)
        val btn_bodyfathistory = findViewById<Button>(R.id.btn_bodyfathistory)


        /*// Age
        var ageValue: Int
        btnPlusAge.setOnClickListener {
            if(edt_age.text.isNotEmpty()) {
                ageValue = Integer.parseInt(edt_age.text.toString())
                edt_age.setText("${ageValue + 1}")
            } else {
                edt_age.setText("1")
            }
        }
        btnMinusAge.setOnClickListener {
            if(edt_age.text.isNotEmpty()) {
                ageValue = Integer.parseInt(edt_age.text.toString())
                if (ageValue > 0) {
                    edt_age.setText("${ageValue - 1}")
                }
            } else {
                edt_age.setText("0")
            }
        }*/

        // Age
        var ageValue: Int
        btn_plusAge.setOnClickListener {
            if(edt_age.text.isNotEmpty()) {
                ageValue = Integer.parseInt(edt_age.text.toString())
                edt_age.setText("${ageValue + 1}")
            } else {
                edt_age.setText("1")
            }
        }
        btn_minusAge.setOnClickListener {
            if(edt_age.text.isNotEmpty()) {
                ageValue = Integer.parseInt(edt_age.text.toString())
                if (ageValue > 0) {
                    edt_age.setText("${ageValue - 1}")
                }
            } else {
                edt_age.setText("0")
            }
        }

        //gender
//        late init var genderValue: String
        rdb_male.setOnClickListener {
            genderValue = "male"
        }
        rdb_female.setOnClickListener {
            genderValue = "female"
        }

        //weight
        var weightValue: Int
        btn_plusWeight.setOnClickListener {
            if(edt_weight.text.isNotEmpty()) {
                weightValue = Integer.parseInt(edt_weight.text.toString())
                edt_weight.setText("${weightValue + 1}")
            } else {
                edt_weight.setText("1")
            }
        }
        btn_minusWeight.setOnClickListener {
            if(edt_weight.text.isNotEmpty()) {
                weightValue = Integer.parseInt(edt_weight.text.toString())
                if (weightValue > 0) {
                    edt_weight.setText("${weightValue - 1}")
                }
            } else {
                edt_weight.setText("0")
            }
        }

        //height
        var heightValue: Int
        btn_plusHeight.setOnClickListener {
            if(edt_height.text.isNotEmpty()) {
                heightValue = Integer.parseInt(edt_height.text.toString())
                edt_height.setText("${heightValue + 1}")
            } else {
                edt_height.setText("1")
            }
        }
        btn_minusHeight.setOnClickListener {
            if(edt_height.text.isNotEmpty()) {
                heightValue = Integer.parseInt(edt_height.text.toString())
                if (heightValue > 0) {
                    edt_height.setText("${heightValue - 1}")
                }
            } else {
                edt_height.setText("0")
            }
        }

        //neck
        var neckValue: Int
        btn_plusNeck.setOnClickListener {
            if(edt_neck.text.isNotEmpty()) {
                neckValue = Integer.parseInt(edt_neck.text.toString())
                edt_neck.setText("${neckValue + 1}")
            } else {
                edt_neck.setText("1")
            }
        }
        btn_minusNeck.setOnClickListener {
            if(edt_neck.text.isNotEmpty()) {
                neckValue = Integer.parseInt(edt_neck.text.toString())
                if (neckValue > 0) {
                    edt_neck.setText("${neckValue - 1}")
                }
            } else {
                edt_neck.setText("0")
            }
        }

        //waist
        var waistValue: Int
        btn_plusWaist.setOnClickListener {
            if(edt_waist.text.isNotEmpty()) {
                waistValue = Integer.parseInt(edt_waist.text.toString())
                edt_waist.setText("${waistValue + 1}")
            } else {
                edt_waist.setText("1")
            }
        }
        btn_minusWaist.setOnClickListener {
            if(edt_waist.text.isNotEmpty()) {
                waistValue = Integer.parseInt(edt_waist.text.toString())
                if (waistValue > 0) {
                    edt_waist.setText("${waistValue - 1}")
                }
            } else {
                edt_waist.setText("0")
            }
        }

        //hip
        var hipValue: Int
        btn_plusHip.setOnClickListener {
            if(edt_hip.text.isNotEmpty()) {
                hipValue = Integer.parseInt(edt_hip.text.toString())
                edt_hip.setText("${hipValue + 1}")
            } else {
                edt_hip.setText("1")
            }
        }
        btn_minusHip.setOnClickListener {
            if(edt_hip.text.isNotEmpty()) {
                hipValue = Integer.parseInt(edt_hip.text.toString())
                if (hipValue > 0) {
                    edt_hip.setText("${hipValue - 1}")
                }
            } else {
                edt_hip.setText("0")
            }
        }


        val btncalculate = findViewById<Button>(R.id.btn_calculate)
        btncalculate.setOnClickListener {
            if(edt_age.text.isNotEmpty() && edt_weight.text.isNotEmpty()
                && genderValue.isNotEmpty() && edt_height.text.isNotEmpty() && edt_neck.text.isNotEmpty()
                && edt_waist.text.isNotEmpty() && edt_hip.text.isNotEmpty()) {
                val age = edt_age.text.toString().toInt()
                val weight = edt_weight.text.toString().toInt()
                val height = edt_height.text.toString().toInt()
                val neck = edt_neck.text.toString().toInt()
                val waist = edt_waist.text.toString().toInt()
                val hip = edt_hip.text.toString().toInt()

                //memanggil API
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val bodyFatInfo = makeApiRequest(age, genderValue, weight,
                            height, neck, waist, hip)

                        score_navymethod.text = "Body Fat (U.S. Navy Method): ${bodyFatInfo.navyMethod}"
                        score_category.text = "Body Fat Category: ${bodyFatInfo.category}"
                        score_fatmass.text = "Body Fat Mass: ${bodyFatInfo.fatMass}"
                        score_leanbodymass.text = "Lean Body Mass: ${bodyFatInfo.leanBodyMass}"
                        score_bmimethod.text = "Body Fat (BMI method): ${bodyFatInfo.bmiMethod}"

                        //Simpan ke database
                        val databaru = databodyfat(
                            age.toString(),
                            genderValue,
                            weight.toString(),
                            height.toString(),
                            neck.toString(),
                            waist.toString(),
                            hip.toString(),
                            bodyFatInfo.navyMethod.toString(),
                            bodyFatInfo.category,
                            bodyFatInfo.fatMass.toString(),
                            bodyFatInfo.leanBodyMass.toString(),
                            bodyFatInfo.bmiMethod.toString()
                        )
                        db.collection("tb_bodyfat")
                            .add(databaru)

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@Body_Fat_Percentage,
                            "Terjadi kesalahan saat mengambil informasi body fat",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else {
                Toast.makeText(
                    this,
                    "Data Tidak Boleh Kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btn_back.setOnClickListener {
            val intent = Intent(this@Body_Fat_Percentage, Home::class.java)
            startActivity(intent)
        }

        btn_bodyfathistory.setOnClickListener {
            val intent = Intent(this@Body_Fat_Percentage, bodyfathistory::class.java)
            startActivity(intent)
        }

        // Contoh penggunaan
        /*GlobalScope.launch(Dispatchers.Main) {
            val bodyFatInfo = makeApiRequest(25, "male", 70, 178, 50, 96, 92)
            println("Navy Method Body Fat: ${bodyFatInfo.navyMethod}")
            println("Body Fat Category: ${bodyFatInfo.category}")
            println("Body Fat Mass: ${bodyFatInfo.fatMass}")
            println("Lean Body Mass: ${bodyFatInfo.leanBodyMass}")
            println("BMI Method Body Fat: ${bodyFatInfo.bmiMethod}")
        }
*/
    }
}