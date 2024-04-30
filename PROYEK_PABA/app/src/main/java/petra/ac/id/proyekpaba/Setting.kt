package petra.ac.id.proyekpaba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class Setting : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val _tvPprofileName = findViewById<TextView>(R.id.tv_profileName)
        val _tvProfileEmail = findViewById<TextView>(R.id.tv_profileEmail)

        // Ambil data dari SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val namaAkun = sharedPreferences.getString("namaAkunProfil", "")
        val emailAkun = sharedPreferences.getString("emailAkunProfil", "")

        if (!namaAkun.isNullOrBlank() && !emailAkun.isNullOrBlank()) {
            _tvPprofileName.text = "$namaAkun"
            _tvProfileEmail.text = "$emailAkun"
        } else {
            // Handle jika data tidak ditemukan
            _tvPprofileName.text = "Data tidak ditemukan"
            _tvProfileEmail.text = "Data tidak ditemukan"
        }

        val _btnDeleteAccount = findViewById<Button>(R.id.btn_deleteAccount)
        _btnDeleteAccount.setOnClickListener {

            val emailToDelete = "$emailAkun"

            // Hapus dokumen user dari Firestore berdasarkan email
            val collectionRef = db.collection("tbUserAccount")
            val query = collectionRef.whereEqualTo("emailAkun", emailToDelete)

            query.get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.reference.delete()
                            .addOnSuccessListener {

                                Toast.makeText(
                                    this,
                                    "Akun Berhasil Dihapus",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this@Setting, Login::class.java)
                                startActivity(intent)
                            }
                            .addOnFailureListener { e ->
                                print("ERROR: $e")
                                Toast.makeText(
                                    this,
                                    "Akun Gagal Dihapus",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }
        }

        val _btnLogout = findViewById<Button>(R.id.btn_logout)
        _btnLogout.setOnClickListener {
            Toast.makeText(
                this,
                "Logout dari akun $namaAkun",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this@Setting, Login::class.java)
            startActivity(intent)
        }

        val _btnEditProfile = findViewById<Button>(R.id.btn_editProfile)
        _btnEditProfile.setOnClickListener {
            val intent = Intent(this@Setting, EditProfile::class.java)
            startActivity(intent)
        }


        val _btnBackToHome = findViewById<Button>(R.id.btn_backToHome)
        _btnBackToHome.setOnClickListener {
            val intent = Intent(this@Setting, Home::class.java)
            startActivity(intent)
        }

    }
}