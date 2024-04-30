package petra.ac.id.proyekpaba.markus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import petra.ac.id.proyekpaba.Home
import petra.ac.id.proyekpaba.R
import petra.ac.id.proyekpaba.calvin.databodyfat

class BMI_History : AppCompatActivity() {

    val db = Firebase.firestore

    var listDataBMI_History = ArrayList<BMI_Result>()
    private lateinit var _rvBMI_History: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_history)

        // Menggunakan recycleview
        _rvBMI_History = findViewById(R.id.rv_BMI_History)
        tampilkanData()  // Read Data dari database

        val _btnBacktoHome = findViewById<Button>(R.id.btn_BackToHome)
        _btnBacktoHome.setOnClickListener {
            val intent = Intent(this@BMI_History, Home::class.java)
            startActivity(intent)
        }
    }

    fun tampilkanData() {
        db.collection("tbBMI_Result")
            .get()
            .addOnSuccessListener { result ->
                listDataBMI_History.clear()
                for (document in result) {
                    val hasil = BMI_Result(
                        document.data["namaAkun"].toString(),
                        document.data["age"].toString(),
                        document.data["weight"].toString(),
                        document.data["height"].toString(),
                        document.data["scoreBMI"].toString(),
                        document.data["statusBMI"].toString(),
                        document.data["date"].toString()
                    )
                    listDataBMI_History.add(hasil)
                }

                if (listDataBMI_History.isNotEmpty()) {
                    _rvBMI_History.layoutManager = LinearLayoutManager(this)
                    val adapterP = adapterBMI_History(listDataBMI_History)
                    _rvBMI_History.adapter = adapterP

                    adapterP.setOnItemClickCallback(object :
                        adapterBMI_History.OnItemClickCallback {
                        // Saat card ditekan
                        override fun onItemClicked(data: BMI_Result) {
                            val intent = Intent(this@BMI_History, BMI_Calculator::class.java)
                            intent.putExtra("kirimData", data)
                            startActivity(intent)
                        }
                    })
                } else {
                    Toast.makeText(
                        this,
                        "Data Tidak Tersedia",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}