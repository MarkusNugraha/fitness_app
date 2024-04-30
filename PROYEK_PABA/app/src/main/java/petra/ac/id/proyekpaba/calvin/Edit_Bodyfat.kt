package petra.ac.id.proyekpaba.calvin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import petra.ac.id.proyekpaba.Home
import petra.ac.id.proyekpaba.R

class Edit_Bodyfat : AppCompatActivity() {

    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_bodyfat)

        val _btnBacktoHome = findViewById<Button>(R.id.btn_backhome)
        _btnBacktoHome.setOnClickListener {
            val intent = Intent(this@Edit_Bodyfat, Home::class.java)
            startActivity(intent)
        }

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

        val dataIntent = intent.getParcelableExtra<databodyfat>("kirimDatabodyfat")
        Log.d("data", dataIntent.toString())
    }
}