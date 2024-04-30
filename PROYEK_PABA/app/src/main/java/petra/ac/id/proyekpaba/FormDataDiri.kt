package petra.ac.id.proyekpaba

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class FormDataDiri : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_data_diri)

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

        // Age
        var ageValue: Int
        btn_plusAge.setOnClickListener {
            if (edt_age.text.isNotEmpty()) {
                ageValue = Integer.parseInt(edt_age.text.toString())
                edt_age.setText("${ageValue + 1}")
            } else {
                edt_age.setText("1")
            }
        }
        btn_minusAge.setOnClickListener {
            if (edt_age.text.isNotEmpty()) {
                ageValue = Integer.parseInt(edt_age.text.toString())
                if (ageValue > 0) {
                    edt_age.setText("${ageValue - 1}")
                }
            } else {
                edt_age.setText("0")
            }
        }

        //gender
        rdb_male.setOnClickListener {
            genderValue = "male"
        }
        rdb_female.setOnClickListener {
            genderValue = "female"
        }

        //weight
        var weightValue: Int
        btn_plusWeight.setOnClickListener {
            if (edt_weight.text.isNotEmpty()) {
                weightValue = Integer.parseInt(edt_weight.text.toString())
                edt_weight.setText("${weightValue + 1}")
            } else {
                edt_weight.setText("1")
            }
        }
        btn_minusWeight.setOnClickListener {
            if (edt_weight.text.isNotEmpty()) {
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
            if (edt_height.text.isNotEmpty()) {
                heightValue = Integer.parseInt(edt_height.text.toString())
                edt_height.setText("${heightValue + 1}")
            } else {
                edt_height.setText("1")
            }
        }
        btn_minusHeight.setOnClickListener {
            if (edt_height.text.isNotEmpty()) {
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
            if (edt_neck.text.isNotEmpty()) {
                neckValue = Integer.parseInt(edt_neck.text.toString())
                edt_neck.setText("${neckValue + 1}")
            } else {
                edt_neck.setText("1")
            }
        }
        btn_minusNeck.setOnClickListener {
            if (edt_neck.text.isNotEmpty()) {
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
            if (edt_waist.text.isNotEmpty()) {
                waistValue = Integer.parseInt(edt_waist.text.toString())
                edt_waist.setText("${waistValue + 1}")
            } else {
                edt_waist.setText("1")
            }
        }
        btn_minusWaist.setOnClickListener {
            if (edt_waist.text.isNotEmpty()) {
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
            if (edt_hip.text.isNotEmpty()) {
                hipValue = Integer.parseInt(edt_hip.text.toString())
                edt_hip.setText("${hipValue + 1}")
            } else {
                edt_hip.setText("1")
            }
        }
        btn_minusHip.setOnClickListener {
            if (edt_hip.text.isNotEmpty()) {
                hipValue = Integer.parseInt(edt_hip.text.toString())
                if (hipValue > 0) {
                    edt_hip.setText("${hipValue - 1}")
                }
            } else {
                edt_hip.setText("0")
            }
        }

        val _btnSaveData = findViewById<Button>(R.id.btn_saveDataForm)
        _btnSaveData.setOnClickListener {
            // Save Data

            val age = edt_age.text.toString()
            val weight = edt_weight.text.toString()
            val height = edt_height.text.toString()
            val neck = edt_neck.text.toString()
            val waist = edt_waist.text.toString()
            val hip = edt_hip.text.toString()

            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userName = sharedPreferences.getString("userName", "")

            if (edt_age.text.isNotEmpty() &&
                edt_weight.text.isNotEmpty() &&
                genderValue.isNotEmpty() &&
                edt_height.text.isNotEmpty() &&
                edt_neck.text.isNotEmpty() &&
                edt_waist.text.isNotEmpty() &&
                edt_hip.text.isNotEmpty()
                ) {

                if (userName != null) {
                    val dataBaru = DataDiri(userName, age, genderValue, weight, height, neck, waist, hip)
                    db.collection("tbUserData")
                        .document(userName)
                        .set(dataBaru)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Data Berhasil Ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            val btn_back = findViewById<Button>(R.id.btn_backTohome)
            btn_back.setOnClickListener {
                val intent = Intent(this@FormDataDiri, Home::class.java)
                startActivity(intent)
            }
        }
    }
}