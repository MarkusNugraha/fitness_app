package petra.ac.id.proyekpaba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import petra.ac.id.proyekpaba.markus.Account

class SignUp : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val inputEmail = findViewById<EditText>(R.id.ed_email)
        val inputUsername = findViewById<EditText>(R.id.ed_username)
        val inputPassword = findViewById<EditText>(R.id.ed_password)

        val _btnsignUp = findViewById<Button>(R.id.btn_signup)
        _btnsignUp.setOnClickListener {

            val email = inputEmail.text.toString()
            val username = inputUsername.text.toString()
            val password = inputPassword.text.toString()

            if (email.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {

                var isEmailTaken: Boolean
                var isUsernameTaken: Boolean

                // Check apakah email sudah pernah dibuat
                db.collection("tbUserAccount")
                    .whereEqualTo("emailAkun", email)
                    .get()
                    .addOnSuccessListener { result ->
                        isEmailTaken = !result.isEmpty

                        // Check apakah username sudah pernah dibuat
                        db.collection("tbUserAccount")
                            .whereEqualTo("namaAkun", username)
                            .get()
                            .addOnSuccessListener { resultUsername ->
                                isUsernameTaken = !resultUsername.isEmpty

                                if (!isEmailTaken && !isUsernameTaken) {
                                    // Tambahkan akun baru
                                    val dataBaru = Account(username, password, email)
                                    db.collection("tbUserAccount")
                                        .document(email)
                                        .set(dataBaru)
                                        .addOnSuccessListener {
                                            inputUsername.setText("")
                                            inputPassword.setText("")
                                            inputEmail.setText("")

                                            Toast.makeText(
                                                this,
                                                "Akun Berhasil Dibuat",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val intent = Intent(this@SignUp, Login::class.java)
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.d("PROJ_PABA", "Error adding document", e)
                                        }
                                } else {
                                    if (isEmailTaken) {
                                        Toast.makeText(
                                            this,
                                            "E-mail sudah pernah dipakai",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    if (isUsernameTaken) {
                                        Toast.makeText(
                                            this,
                                            "Username sudah pernah dipakai",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                    }
            } else {
                Toast.makeText(
                    this,
                    "Data tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val _txtSignUp = findViewById<TextView>(R.id.txt_logIn)
        _txtSignUp.setOnClickListener {
            val intent = Intent(this@SignUp, Login::class.java)
            startActivity(intent)
        }
    }
}
