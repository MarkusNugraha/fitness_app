package petra.ac.id.proyekpaba.Billy

import android.annotation.SuppressLint
import android.content.Intent
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

class Daily_Calorie_Requirements : AppCompatActivity() {
    data class DailyCalorieData(
        val age: Int = 0,
        val gender: String = "",
        val height: Int = 0,
        val weight: Int = 0,
        val mode: String = "",
        val weightLossCalories: Double = 0.0,
        val date: Timestamp = Timestamp.now(),
        // Add a property to store the document ID
        var documentId: String = ""
    ) {
        // Add a no-argument constructor for Firestore deserialization
        constructor() : this(0, "", 0, 0, "", 0.0, Timestamp.now())
    }

    data class DailyCalorieInfo(
        val dailyCalories: Double,
        val calorieDetails: String,
        val weightLossCalories: Double,
        val weightLossCalorieseasy: Double,
        val weightLossCaloriesextreme: Double
    )

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

                return@withContext DailyCalorieInfo(dailyCalories, calorieDetails, weightLossCalories,weightLossCaloriesEasy,weightLossCaloriesExtreme)
            } else {
                throw JSONException("No 'data' object in the response")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
    private val db = FirebaseFirestore.getInstance()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calorie_requirements)

        var age = "0"
        var gender = "male"
        var height = "0"
        var weight = "0"
        val activityLevel = "level_1"

        val ModeCalorie = findViewById<TextView>(R.id.tv_mode)

        //edit text
        val textcalorie = findViewById<TextView>(R.id.textView200)
        val et_age_calorie = findViewById<EditText>(R.id.ed_age_calorie)
        val et_gender = findViewById<EditText>(R.id.ed_Gender)
        val et_Height = findViewById<EditText>(R.id.et_Height_calorie)
        val et_weight = findViewById<EditText>(R.id.et_Weight_Calorie)



        //button
        val btnplus_age_calorie = findViewById<Button>(R.id.btn_plusAge_calorie)
        val btnmin_age_calorie = findViewById<Button>(R.id.btn_minusAge_calorie)
        val btn_Male_Calorie = findViewById<Button>(R.id.btn_male)
        val btn_Female_Calorie = findViewById<Button>(R.id.btn_female)
        val btnplus_Height_Calorie = findViewById<Button>(R.id.btn_plus_height_Calorie)
        val btnmin_Height_Calorie = findViewById<Button>(R.id.btn_min_Height_Calorie)
        val btnplus_weight_Calorie = findViewById<Button>(R.id.btn_plus_calorie_weight)
        val btnmin_weight_Calorie = findViewById<Button>(R.id.btn_min_Weight_Calorie)
        val btnEasy = findViewById<Button>(R.id.btn_Mild_Weight_loss)
        val btnNormal = findViewById<Button>(R.id.btn_Weight_Loss)
        val btnExtreme = findViewById<Button>(R.id.btn_Extreme_Weight_Loss)
        val btncalculate = findViewById<Button>(R.id.btn_calculate_Calorie)
        val btn_Track = findViewById<Button>(R.id.btn_track)

        btn_Track.setOnClickListener{
            val intent = Intent(this@Daily_Calorie_Requirements, Track_Calorie::class.java)
            startActivity(intent)
        }
        btnplus_age_calorie.setOnClickListener{
            if (et_age_calorie.text.isNotEmpty()) {
                age = et_age_calorie.text.toString()
                val currentAge = age.toInt()
                val newAge = currentAge + 1
                age = newAge.toString()
                et_age_calorie.setText(newAge.toString())
            }else{
                et_age_calorie.setText("1")
            }


        }
        btnmin_age_calorie.setOnClickListener{
            if (et_age_calorie.text.isNotEmpty()) {
                age = et_age_calorie.text.toString()
                val currentAge = age.toInt()
                val newAge = currentAge - 1
                age = newAge.toString()
                et_age_calorie.setText(newAge.toString())
            }else{
                et_age_calorie.setText("1")
            }
        }
        btn_Male_Calorie.setOnClickListener{
            val newMale = "male"
            gender = newMale
            et_gender.setText(newMale)

        }
        btn_Female_Calorie.setOnClickListener{
            val newFemale = "female"
            gender = newFemale
            et_gender.setText(newFemale)

        }
        btnplus_Height_Calorie.setOnClickListener{
            if (et_Height.text.isNotEmpty()) {
                height = et_Height.text.toString()
                val currentHeight = height.toInt()
                val newHeight = currentHeight + 1
                height = newHeight.toString()
                et_Height.setText(newHeight.toString())
            }else{
                et_Height.setText("1")
            }

        }
        btnmin_Height_Calorie.setOnClickListener{
            if (et_Height.text.isNotEmpty()) {
                height = et_Height.text.toString()
                val currentHeight = height.toInt()
                val newHeight = currentHeight - 1
                height = newHeight.toString()
                et_Height.setText(newHeight.toString())
            }else{
                et_Height.setText("1")
            }
        }
        btnplus_weight_Calorie.setOnClickListener{
            if (et_weight.text.isNotEmpty()) {
                weight = et_weight.text.toString()
                val currentweight = weight.toInt()
                val newWeight = currentweight + 1
                height = newWeight.toString()
                et_weight.setText(newWeight.toString())
            }else{
                et_weight.setText("1")
            }
        }

        btnmin_weight_Calorie.setOnClickListener{
            if (et_weight.text.isNotEmpty()) {
                weight = et_weight.text.toString()
                val currentweight = weight.toInt()
                val newWeight = currentweight - 1
                height = newWeight.toString()
                et_weight.setText(newWeight.toString())
            }else{
                et_weight.setText("1")
            }
        }
        btnEasy.setOnClickListener{

        }


//calculate
        var easy = 0
        var normal = 0
        var extreme = 0
        btnEasy.setOnClickListener{
            normal=0
            extreme=0
            easy += 1
            ModeCalorie.setText("Easy")

        }
        btnNormal.setOnClickListener{
            normal +=1
            extreme =0
            easy =0
            ModeCalorie.setText("Normal")
        }
        btnExtreme.setOnClickListener{
            normal =0
            extreme +=1
            easy =0
            ModeCalorie.setText("Extreme")
        }
        btncalculate.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    age = et_age_calorie.text.toString()
                    height = et_Height.text.toString()
                    weight = et_weight.text.toString()
                    val dailyCalorieInfo =
                        makeCalorieApiRequest(age.toInt(), gender, height.toInt(), weight.toInt(), activityLevel)

                    // Determine the selected mode
                    val selectedMode = when {
                        easy == 1 -> "Easy"
                        normal == 1 -> "Normal"
                        extreme == 1 -> "Extreme"
                        else -> "Unknown"
                    }

                    // Save data to Firebase
                    val dailyCalorieData = DailyCalorieData(
                        age.toInt(),
                        gender,
                        height.toInt(),
                        weight.toInt(),
                        selectedMode,
                        when {
                            easy == 1 -> dailyCalorieInfo.weightLossCalorieseasy
                            normal == 1 -> dailyCalorieInfo.weightLossCalories
                            extreme == 1 -> dailyCalorieInfo.weightLossCaloriesextreme
                            else -> 0.0
                        }
                    )

                    // Save to Firebase
                    db.collection("tb_Daily_Calorie").add(dailyCalorieData)
                        .addOnSuccessListener { documentReference ->
                            println("DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document: $e")
                        }

                    // Update UI with calculated calories
                    textcalorie.text = "Calories: ${dailyCalorieData.weightLossCalories}"
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Exception: ${e.message}")
                }
            }
        }

    }
}