package petra.ac.id.proyekpaba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class EditProfile : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val _edtPprofileName = findViewById<EditText>(R.id.edt_profileName)
        val _tvProfileEmail = findViewById<TextView>(R.id.tv_profileEmail)

        // Ambil data dari SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val namaAkun = sharedPreferences.getString("namaAkunProfil", "")
        val emailAkun = sharedPreferences.getString("emailAkunProfil", "")

        if (!namaAkun.isNullOrBlank() && !emailAkun.isNullOrBlank()) {
            _edtPprofileName.setText(namaAkun)
            _tvProfileEmail.text = emailAkun
        } else {
            // Handle jika data tidak ditemukan
            _edtPprofileName.setText("Data tidak ditemukan")
            _tvProfileEmail.text = "Data tidak ditemukan"
        }

        val _btnSaveChanges = findViewById<Button>(R.id.btn_save)
        _btnSaveChanges.setOnClickListener {
            val newNamaAkun = _edtPprofileName.text.toString()

            if (newNamaAkun != namaAkun) {
                // Nama baru berbeda dari nama lama
                // Lakukan validasi atau cek apakah nama sudah pernah digunakan sebelumnya
                db.collection("tbUserAccount")
                    .whereEqualTo("namaAkun", newNamaAkun)
                    .get()
                    .addOnSuccessListener { resultUsername ->
                        if (resultUsername.isEmpty) {
                            // Mengupdate data di tbUserAccount
                            var collectionRef = db.collection("tbUserAccount")
                            var query = collectionRef.whereEqualTo("namaAkun", namaAkun)
                            query.get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        document.reference.update(
                                            "namaAkun", newNamaAkun
                                        )
                                    }
                                }

                            // Mengupdate data di tbBMI_Result
                            collectionRef = db.collection("tbBMI_Result")
                            query = collectionRef.whereEqualTo("namaAkun", namaAkun)
                            query.get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        document.reference.update(
                                            "namaAkun", newNamaAkun
                                        )
                                    }
                                }

                            Toast.makeText(
                                this,
                                "Nama berhasil diupdate",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = Intent(this@EditProfile, Login::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                this,
                                "Nama sudah pernah dipakai",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else {
                // Nama tidak berubah
                Toast.makeText(
                    this,
                    "Nama tidak diubah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        _tvProfileEmail.setOnClickListener {
            Toast.makeText(
                this,
                "E-mail tidak bisa diubah",
                Toast.LENGTH_SHORT
            ).show()
        }

        val _btnBackToHome = findViewById<Button>(R.id.btn_backToHome)
        _btnBackToHome.setOnClickListener {
            val intent = Intent(this@EditProfile, Home::class.java)
            startActivity(intent)
        }
    }


}
