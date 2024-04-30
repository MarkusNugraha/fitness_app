package petra.ac.id.proyekpaba

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import petra.ac.id.proyekpaba.Billy.Daily_Calorie_Requirements
import petra.ac.id.proyekpaba.calvin.Body_Fat_Percentage
import petra.ac.id.proyekpaba.markus.BMI_Calculator
import java.text.DateFormat
import java.util.Calendar

class Home : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    // API Request For BMI Calculator
    data class BmiInfo(val score: Double, val health: String, val healthyBmiRange: String)
    private suspend fun makeApiRequest(age: Int, weight: Int, height: Int): Home.BmiInfo =
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

    // API Request For BodyFat
    private suspend fun makeApiRequest(
        age: Int,
        gender: String,
        weight: Int,
        height: Int,
        neck: Int,
        waist: Int,
        hip: Int
    ): Body_Fat_Percentage.BodyFatInfo = withContext(Dispatchers.IO) {
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

        return@withContext Body_Fat_Percentage.BodyFatInfo(
            navyMethod,
            category,
            fatMass,
            leanBodyMass,
            bmiMethod
        )
    }

    // API Request For Daily Calorie Requirement
    private suspend fun makeCalorieApiRequest(
        age: Int,
        gender: String,
        height: Int,
        weight: Int,
        activityLevel: String
    ): Daily_Calorie_Requirements.DailyCalorieInfo = withContext(Dispatchers.IO) {
        val client = OkHttpClient()

        val url =
            "https://fitness-calculator.p.rapidapi.com/dailycalorie?age=$age&gender=$gender&height=$height&weight=$weight&activitylevel=$activityLevel"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("X-RapidAPI-Key", "d7d9e0b0d0msh4ca54b57a1aaaadp117c7cjsnc24d5d92480b")
            .addHeader("X-RapidAPI-Host", "fitness-calculator.p.rapidapi.com")
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body()?.string() ?: "No response"

            // Log the raw JSON response
            println("Raw JSON response: $responseBody")

            // Parse the JSON response
            val jsonObject = JSONObject(responseBody)
            val dataObject = jsonObject.optJSONObject("data")
            if (dataObject != null) {
                val dailyCalories = dataObject.optDouble("daily_calories", 0.0)
                val calorieDetails = dataObject.optString("calorie_details", "")
                val weightLossCalories = dataObject
                    .optJSONObject("goals")
                    ?.optJSONObject("Weight loss")
                    ?.optDouble("calory", 0.0) ?: 0.0
                val weightLossCaloriesEasy = dataObject
                    .optJSONObject("goals")
                    ?.optJSONObject("Mild weight loss")
                    ?.optDouble("calory", 0.0) ?: 0.0
                val weightLossCaloriesExtreme = dataObject
                    .optJSONObject("goals")
                    ?.optJSONObject("Extreme weight loss")
                    ?.optDouble("calory", 0.0) ?: 0.0

                return@withContext Daily_Calorie_Requirements.DailyCalorieInfo(
                    dailyCalories,
                    calorieDetails,
                    weightLossCalories,
                    weightLossCaloriesEasy,
                    weightLossCaloriesExtreme
                )
            } else {
                throw JSONException("No 'data' object in the response")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Untuk tanggal
        val calendar = Calendar.getInstance()
        val currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.time)

        val date = findViewById<TextView>(R.id.tanggal)
        date.text = currentDate

        val name = findViewById<TextView>(R.id.name)
        // Menggunakan shared preferences
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString("userName", "")
        name.text = "Hi, $userName"


        if(userName != null) {
            db.collection("tbUserData")
                .whereEqualTo("name", userName)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        for (document in result) {
                            val hasil = DataDiri(
                                document.data["name"].toString(),
                                document.data["age"].toString(),
                                document.data["gender"].toString(),
                                document.data["weight"].toString(),
                                document.data["height"].toString(),
                                document.data["neck"].toString(),
                                document.data["waist"].toString(),
                                document.data["hip"].toString()
                            )

                            // Untuk BMI CALCULATOR
                            val tvAge = findViewById<TextView>(R.id.tv_Age)
                            val tvWeight = findViewById<TextView>(R.id.tv_Weight)
                            val tvHeight = findViewById<TextView>(R.id.tv_Height)
                            val tvScore = findViewById<TextView>(R.id.tv_scoreBMI)
                            val tvStatus = findViewById<TextView>(R.id.tv_statusBMI)

                            GlobalScope.launch(Dispatchers.Main) {
                                tvAge.text = "${hasil.age}"
                                tvWeight.text = "${hasil.weight}"
                                tvHeight.text = "${hasil.height}"

                                try {
                                    val bmiInfo = makeApiRequest(
                                        hasil.age.toInt(),
                                        hasil.weight.toInt(),
                                        hasil.height.toInt()
                                    )
                                    tvScore.text = "${bmiInfo.score}"

                                    // Untuk warna score BMI
                                    if (bmiInfo.score < 18.5) {
                                        tvScore.setTextColor(Color.parseColor("#2BB9F6"))
                                    } else if (bmiInfo.score < 25) {
                                        tvScore.setTextColor(Color.parseColor("#1BBB4D"))
                                    } else if (bmiInfo.score < 30) {
                                        tvScore.setTextColor(Color.parseColor("#EAA81B"))
                                    } else if (bmiInfo.score < 35) {
                                        tvScore.setTextColor(Color.parseColor("#FA5239"))
                                    } else if (bmiInfo.score < 40) {
                                        tvScore.setTextColor(Color.parseColor("#F82C14"))
                                    } else if (bmiInfo.score >= 40) {
                                        tvScore.setTextColor(Color.parseColor("#F82C14"))
                                    }
                                    tvStatus.text = "${bmiInfo.health}"


                                    // Simpan hasil kedalam database dengan format dataClass BMI_Result

                                    // Menggunakan shared preferences untuk mendapatkan nama
                                    val sharedPreferences =
                                        getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                                    val userName = sharedPreferences.getString("userName", "")

                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@Home,
                                        "Terjadi kesalahan saat mengambil informasi BMI",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            // Untuk Body Fat Calculator
                            val score_navymethod = findViewById<TextView>(R.id.score_navyMethod)
                            val score_category = findViewById<TextView>(R.id.score_Category)
                            val score_fatmass = findViewById<TextView>(R.id.score_fatMass)
                            val score_leanbodymass = findViewById<TextView>(R.id.score_leanBodyMass)
                            val score_bmimethod = findViewById<TextView>(R.id.score_bmiMethod)

                            GlobalScope.launch(Dispatchers.Main) {
                                try {

                                    val bodyFatInfo =
                                        makeApiRequest(
                                            hasil.age.toInt(),
                                            hasil.gender,
                                            hasil.weight.toInt(),
                                            hasil.height.toInt(),
                                            hasil.neck.toInt(),
                                            hasil.waist.toInt(),
                                            hasil.hip.toInt()
                                        )
                                    score_navymethod.text =
                                        "Body Fat (U.S. Navy Method): ${bodyFatInfo.navyMethod}"
                                    score_category.text =
                                        "Body Fat Category: ${bodyFatInfo.category}"
                                    score_fatmass.text = "Body Fat Mass: ${bodyFatInfo.fatMass}"
                                    score_leanbodymass.text =
                                        "Lean Body Mass: ${bodyFatInfo.leanBodyMass}"
                                    score_bmimethod.text =
                                        "Body Fat (BMI method): ${bodyFatInfo.bmiMethod}"
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@Home,
                                        "Terjadi kesalahan saat mengambil informasi body fat",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            // Untuk Daily Calorie Requirement
                            val tv_easyResult = findViewById<TextView>(R.id.easyResult)
                            val tv_normalResult = findViewById<TextView>(R.id.normalResult)
                            val tv_extremeResult = findViewById<TextView>(R.id.extremeResult)

                            GlobalScope.launch(Dispatchers.Main) {
                                try {
                                    val age = hasil.age.toInt()
                                    val height = hasil.height.toInt()
                                    val weight = hasil.weight.toInt()
                                    val gender = hasil.gender

                                    val easyDailyCalorieInfo =
                                        makeCalorieApiRequest(age, gender, height, weight, "level_1").weightLossCalorieseasy

                                    val normalDailyCalorieInfo =
                                        makeCalorieApiRequest(age, gender, height, weight, "level_1").weightLossCalories

                                    val extremeDailyCalorieInfo =
                                        makeCalorieApiRequest(age, gender, height, weight, "level_1").weightLossCaloriesextreme

                                    // Update UI with calculated calories
                                    tv_easyResult.text = "Easy: ${easyDailyCalorieInfo}"
                                    tv_normalResult.text = "Normal: ${normalDailyCalorieInfo}"
                                    tv_extremeResult.text = "Extreme: ${extremeDailyCalorieInfo}"

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    println("Exception: ${e.message}")
                                }
                            }
                        }
                    }
                }

        }

        // Ambil data terakhir dari tbBMI_Result berdasarkan nama
//        val userResultRef = db.collection("tbBMI_Result")
//            .whereEqualTo("namaAkun", userName)
//            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
//            .limit(1)
//
//        userResultRef.get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val result = task.result
//                    if (result != null && !result.isEmpty) {
//                        val lastResult = result.documents[0]
//                        // Tampilkan data pada TextView
//                        tvAge.text = "${lastResult.getString("age")}"
//                        tvWeight.text = "${lastResult.getString("weight")}"
//                        tvHeight.text = "${lastResult.getString("height")}"
//                        tvScore.text = "${lastResult.getString("scoreBMI")}"
//                        tvStatus.text = "${lastResult.getString("statusBMI")}"
//                    } else {
//                        tvAge.text = "Nan"
//                        tvWeight.text = "Nan"
//                        tvHeight.text = "Nan"
//                        tvScore.text = "Nan"
//                        tvStatus.text = "Nan"
//                    }
//                }
//            }


        val btnTestBMI = findViewById<Button>(R.id.btn_TestBMI)
        btnTestBMI.setOnClickListener {
            val intent = Intent(this@Home, BMI_Calculator::class.java)
            startActivity(intent)
        }

        val btnSetting = findViewById<ImageView>(R.id.btn_setting)
        btnSetting.setOnClickListener {
            val intent = Intent(this@Home, Setting::class.java)
            startActivity(intent)
        }

        val btn_bodyfat = findViewById<Button>(R.id.btnbodyfat)
        btn_bodyfat.setOnClickListener {
            val intent = Intent(this@Home, Body_Fat_Percentage::class.java)
            startActivity(intent)
        }

        val _btnDailyCalorie = findViewById<Button>(R.id.btn_daily_calorie)
        _btnDailyCalorie.setOnClickListener {
            val intent = Intent(this@Home, Daily_Calorie_Requirements::class.java)
            startActivity(intent)
        }

        val _btnDataDiri = findViewById<Button>(R.id.btn_dataDiri)
        _btnDataDiri.setOnClickListener {
            val intent = Intent(this@Home, FormDataDiri::class.java)
            startActivity(intent)
        }
    }
}
