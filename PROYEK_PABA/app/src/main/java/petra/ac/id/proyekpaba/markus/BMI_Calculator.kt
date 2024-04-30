package petra.ac.id.proyekpaba.markus

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import java.text.DateFormat
import java.util.Calendar

class BMI_Calculator: AppCompatActivity() {

    // Untuk menampung hasil response API
    data class BmiInfo(val score: Double, val health: String, val healthyBmiRange: String)

    // Untuk request API
    private suspend fun makeApiRequest(age: Int, weight: Int, height: Int): BmiInfo =
        withContext(Dispatchers.IO) {
            val client = OkHttpClient()

            val url =
                "https://fitness-calculator.p.rapidapi.com/bmi?age=$age&weight=$weight&height=$height"

            val request = Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "41bb703e3emsh9a6d561323de3a8p1ab405jsnc05899f3c241")  // Ini API Key Markus
                .addHeader("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body()?.string() ?: "No response"

            // Untuk mengambil hasil response API
            // Parse the JSON response
            val jsonObject = JSONObject(responseBody)
            val dataObject = jsonObject.getJSONObject("data")

            // Extract relevant information
            val bmiScore = dataObject.getDouble("bmi")
            val health = dataObject.getString("health")
            val healthyBmiRange = dataObject.getString("healthy_bmi_range")

            return@withContext BmiInfo(bmiScore, health, healthyBmiRange)
        }

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_calculator)

        val edt_age = findViewById<EditText>(R.id.ed_age)
        val edt_weight = findViewById<EditText>(R.id.ed_weight)
        val edt_height = findViewById<EditText>(R.id.ed_height)

        val btnPlusAge = findViewById<Button>(R.id.btn_plusAge)
        val btnMinusAge = findViewById<Button>(R.id.btn_minusAge)
        val btnPlusWeight = findViewById<Button>(R.id.btn_plusWeight)
        val btnMinusWeight = findViewById<Button>(R.id.btn_minusWeight)
        val btnPlusHeight = findViewById<Button>(R.id.btn_plusHeight)
        val btnMinusHeight = findViewById<Button>(R.id.btn_minusHeight)

        val _tvScoreBMI = findViewById<TextView>(R.id.score_BMI)
        val _tvDetailBMI = findViewById<TextView>(R.id.detail_penjelasan)

        val btnLihatHistory = findViewById<Button>(R.id.btn_lihatHistory)
        val btnBackToHome = findViewById<Button>(R.id.btn_backToHome)

        // Age
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
        }

        // Weight
        var weightValue: Int
        btnPlusWeight.setOnClickListener {
            if(edt_weight.text.isNotEmpty()) {
                weightValue = Integer.parseInt(edt_weight.text.toString())
                edt_weight.setText("${weightValue + 1}")
            } else {
                edt_weight.setText("1")
            }
        }
        btnMinusWeight.setOnClickListener {
            if(edt_weight.text.isNotEmpty()) {
                weightValue = Integer.parseInt(edt_weight.text.toString())
                if (weightValue > 0) {
                    edt_weight.setText("${weightValue - 1}")
                }
            } else {
                edt_weight.setText("0")
            }
        }

        // Height
        var heightValue: Int
        btnPlusHeight.setOnClickListener {
            if(edt_height.text.isNotEmpty()) {
                heightValue = Integer.parseInt(edt_height.text.toString())
                edt_height.setText("${heightValue + 1}")
            } else {
                edt_height.setText("1")
            }
        }
        btnMinusHeight.setOnClickListener {
            if(edt_height.text.isNotEmpty()) {
                heightValue = Integer.parseInt(edt_height.text.toString())
                if (heightValue > 0) {
                    edt_height.setText("${heightValue - 1}")
                }
            } else {
                edt_height.setText("0")
            }
        }


        val btnCalculate = findViewById<Button>(R.id.btn_calculate)
        btnCalculate.setOnClickListener {
            if(edt_age.text.isNotEmpty() && edt_weight.text.isNotEmpty() && edt_height.text.isNotEmpty()) {

                val age = edt_age.text.toString().toInt()
                val weight = edt_weight.text.toString().toInt()
                val height = edt_height.text.toString().toInt()

                // Memanggil API
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        val bmiInfo = makeApiRequest(age, weight, height)
                        _tvScoreBMI.text = "${bmiInfo.score}"

                        // Untuk warna score BMI
                        if(bmiInfo.score < 18.5) {
                            _tvScoreBMI.setTextColor(Color.parseColor("#2BB9F6"))
                        } else if(bmiInfo.score < 25) {
                            _tvScoreBMI.setTextColor(Color.parseColor("#1BBB4D"))
                        } else if(bmiInfo.score < 30) {
                            _tvScoreBMI.setTextColor(Color.parseColor("#EAA81B"))
                        } else if(bmiInfo.score < 35) {
                            _tvScoreBMI.setTextColor(Color.parseColor("#FA5239"))
                        } else if(bmiInfo.score < 40) {
                            _tvScoreBMI.setTextColor(Color.parseColor("#F82C14"))
                        } else if(bmiInfo.score >= 40) {
                            _tvScoreBMI.setTextColor(Color.parseColor("#F82C14"))
                        }
                        _tvDetailBMI.text = "Health: \n${bmiInfo.health}\n\nHealthy BMI Range: \n${bmiInfo.healthyBmiRange}"


                        // Simpan hasil kedalam database dengan format dataClass BMI_Result

                        // Menggunakan shared preferences untuk mendapatkan nama
                        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                        val userName = sharedPreferences.getString("userName", "")

                        // Mendapatkan date
                        val calendar = Calendar.getInstance()
                        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime())

                        val dataBaru = BMI_Result(
                            userName.toString(),
                            age.toString(),
                            weight.toString(),
                            height.toString(),
                            bmiInfo.score.toString(),
                            bmiInfo.health,
                            currentDate
                        )

                        // Simpan ke database
                        db.collection("tbBMI_Result")
                            .add(dataBaru)

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@BMI_Calculator,
                            "Terjadi kesalahan saat mengambil informasi BMI",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else {
                Toast.makeText(
                    this,
                    "Data Tidak Boleh Kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnLihatHistory.setOnClickListener {
            val intent = Intent(this@BMI_Calculator, BMI_History::class.java)
            startActivity(intent)
        }

        btnBackToHome.setOnClickListener {
            val intent = Intent(this@BMI_Calculator, Home::class.java)
            startActivity(intent)
        }


    }

//    private suspend fun makeApiRequest(): String = withContext(Dispatchers.IO) {
//        val client = OkHttpClient()
//
//        val request = Request.Builder()
//            .url("https://fitness-calculator.p.rapidapi.com/bmi?age=20&weight=94&height=175")
//            .get()
//            .addHeader("X-RapidAPI-Key", "d7d9e0b0d0msh4ca54b57a1aaaadp117c7cjsnc24d5d92480b")
//            .addHeader("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
//            .build()
//
//        val response = client.newCall(request).execute()
//        return@withContext response.body()?.string() ?: "No response"
//    }

}