package petra.ac.id.proyekpaba.Billy

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import petra.ac.id.proyekpaba.R
import java.util.*

class Edit_Daily_Calorie : AppCompatActivity() {
    data class DailyCalorieData(
        val age: Int = 0,
        val gender: String = "",
        val height: Int = 0,
        val weight: Int = 0,
        val mode: String = "",
        val weightLossCalories: Double = 0.0,
        val date: Timestamp = Timestamp.now(),
        var documentId: String = ""
    ) {
        constructor() : this(0, "", 0, 0, "", 0.0, Timestamp.now())
    }

    data class DailyCalorieInfo(
        val dailyCalories: Double,
        val calorieDetails: String,
        val weightLossCalories: Double,
        val weightLossCalorieseasy: Double,
        val weightLossCaloriesextreme: Double
    )

    private val db = FirebaseFirestore.getInstance()
    private lateinit var documentId: String

    private lateinit var et_age_calorie: EditText
    private lateinit var et_gender: EditText
    private lateinit var et_Height: EditText
    private lateinit var et_weight: EditText
    private lateinit var ModeCalorie: TextView
    private lateinit var textcalorie: TextView
    private lateinit var btncalculate: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calorie_requirements)

        et_age_calorie = findViewById(R.id.ed_Edit_age_calorie)
        et_gender = findViewById(R.id.ed_Edit_Gender)
        et_Height = findViewById(R.id.et_Edit_Height_calorie)
        et_weight = findViewById(R.id.et_Edit_Weight_Calorie)
        ModeCalorie = findViewById(R.id.tv_Edit_mode)
        textcalorie = findViewById(R.id.tv_editCalories)
        btncalculate = findViewById(R.id.btn_calculate_Calorie_Edit)

        documentId = intent.getStringExtra("DOCUMENT_ID") ?: ""

        et_age_calorie.setText(intent.getIntExtra("AGE", 0).toString())
        et_gender.setText(intent.getStringExtra("GENDER") ?: "male")
        et_Height.setText(intent.getIntExtra("HEIGHT", 0).toString())
        et_weight.setText(intent.getIntExtra("WEIGHT", 0).toString())

        btncalculate.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val dailyCalorieInfo = makeCalorieApiRequest(
                        et_age_calorie.text.toString().toInt(),
                        et_gender.text.toString(),
                        et_Height.text.toString().toInt(),
                        et_weight.text.toString().toInt(),
                        "level_1"
                    )

                    textcalorie.text = dailyCalorieInfo.dailyCalories.toString()
                    ModeCalorie.text = "Mode: ${dailyCalorieInfo.calorieDetails}"

                    when (ModeCalorie.text) {
                        "Mode: Weight loss" -> textcalorie.text = dailyCalorieInfo.weightLossCalories.toString()
                        "Mode: Mild weight loss" -> textcalorie.text = dailyCalorieInfo.weightLossCalorieseasy.toString()
                        "Mode: Extreme weight loss" -> textcalorie.text = dailyCalorieInfo.weightLossCaloriesextreme.toString()
                    }

                    val updatedData = DailyCalorieData(
                        et_age_calorie.text.toString().toInt(),
                        et_gender.text.toString(),
                        et_Height.text.toString().toInt(),
                        et_weight.text.toString().toInt(),
                        "level_1",
                        dailyCalorieInfo.weightLossCalories,
                        Timestamp.now()
                    )
                    updatedData.documentId = documentId

                    db.collection("tb_Daily_Calorie").document(documentId).set(updatedData)
                        .addOnSuccessListener {
                            // Handle success
                        }
                        .addOnFailureListener { e ->
                            // Handle failure
                        }

                } catch (e: Exception) {
                    textcalorie.text = "Error calculating calories"
                    ModeCalorie.text = "Mode: N/A"
                }
            }
        }
    }

    private suspend fun makeCalorieApiRequest(
        age: Int,
        gender: String,
        height: Int,
        weight: Int,
        activityLevel: String
    ): DailyCalorieInfo = withContext(Dispatchers.IO) {
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

            println("Raw JSON response: $responseBody")

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

                return@withContext DailyCalorieInfo(
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
}
