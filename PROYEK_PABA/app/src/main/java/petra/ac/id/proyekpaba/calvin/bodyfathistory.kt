package petra.ac.id.proyekpaba.calvin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import petra.ac.id.proyekpaba.Home
import petra.ac.id.proyekpaba.Login
import petra.ac.id.proyekpaba.R

class bodyfathistory : AppCompatActivity() {

    val db = Firebase.firestore

    var data_bodyfathistory = ArrayList<databodyfat>()
    private lateinit var _rvbodyfathistory: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bodyfathistory)

        _rvbodyfathistory = findViewById(R.id.rv_bodyfathistory)

        tampilkanData()

        val _btnBacktoHome = findViewById<Button>(R.id.btn_BackToHome)
        _btnBacktoHome.setOnClickListener {
            val intent = Intent(this@bodyfathistory, Home::class.java)
            startActivity(intent)
        }
    }

    fun tampilkanData() {
        db.collection("tb_bodyfat")
            .get()
            .addOnSuccessListener {
                    result ->
                data_bodyfathistory.clear()
                for (document in result) {
                    val hasil = databodyfat(
                        document.data["age"].toString(),
                        document.data["gender"].toString(),
                        document.data["weight"].toString(),
                        document.data["height"].toString(),
                        document.data["neck"].toString(),
                        document.data["waist"].toString(),
                        document.data["hip"].toString(),
                        document.data["navyMethod"].toString(),
                        document.data["category"].toString(),
                        document.data["fatMass"].toString(),
                        document.data["leanBodyMass"].toString(),
                        document.data["bmiMethod"].toString()
                    )
                    data_bodyfathistory.add(hasil)
                }

                if (data_bodyfathistory.isNotEmpty()) {

                    val adapterP = adapterBodyFat(data_bodyfathistory)
                    _rvbodyfathistory.adapter = adapterP
                    _rvbodyfathistory.layoutManager = LinearLayoutManager(this)

                    adapterP.setOnItemClickCallback(object :
                        adapterBodyFat.OnItemClickCallback {
                        override fun onItemClicked(data: databodyfat) {
                            val intent = Intent(this@bodyfathistory, Edit_Bodyfat::class.java)
                            intent.putExtra("kirimDatabodyfat", data)
                            /*intent.putExtra("age", data.age)
                            intent.putExtra("gender", data.gender)
                            intent.putExtra("weight", data.weight)
                            intent.putExtra("height", data.height)
                            intent.putExtra("neck", data.neck)
                            intent.putExtra("waist", data.waist)
                            intent.putExtra("hip", data.hip)
                            intent.putExtra("navyMethod", data.navyMethod)
                            intent.putExtra("category", data.category)
                            intent.putExtra("fatMass", data.fatMass)
                            intent.putExtra("leanBodyMass", data.leanBodyMass)
                            intent.putExtra("bmiMethod", data.bmiMethod)*/
                            startActivity(intent)
                        }

                        override fun delData(pos: Int) {
                            val datatodelete = data_bodyfathistory[pos].age

                            // Hapus dokumen user dari Firestore berdasarkan email
                            val collectionRef = db.collection("tb_bodyfat")
                            val query = collectionRef.whereEqualTo("age", datatodelete)

                            query.get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        document.reference.delete()
                                            .addOnSuccessListener {

                                                /*Toast.makeText(
                                                    this,
                                                    "Akun Berhasil Dihapus",
                                                    Toast.LENGTH_LONG
                                                ).show()*/

                                                /*val intent = Intent(this@bodyfathistory, Login::class.java)
                                                startActivity(intent)*/
                                            }
                                            .addOnFailureListener { e ->
                                                print("ERROR: $e")
                                                /*Toast.makeText(
                                                    this,
                                                    "Akun Gagal Dihapus",
                                                    Toast.LENGTH_LONG
                                                ).show()*/
                                            }
                                    }
                                }

                        }
                       /* override fun delData(pos: Int) {
                            AlertDialog.Builder(this@bodyfathistory)
                                .setTitle("Hapus Data")
                                .setMessage("Apakah anda yakin ingin menghapus data?")
                                .setPositiveButton("Ya", { dialog, which ->
                                    db.collection("tb_bodyfat")
                                        .document(data_bodyfathistory[pos].age)
                                        .delete()
                                    tampilkanData()
                                })
                                .setNegativeButton("Tidak", { dialog, which ->
                                    Toast.makeText(
                                        this@bodyfathistory,
                                        "Data Tidak Terhapus",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }).show()
                        }*/
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

