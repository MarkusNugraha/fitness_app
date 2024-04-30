package petra.ac.id.proyekpaba.Billy

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import petra.ac.id.proyekpaba.R

class Track_Calorie : AppCompatActivity(), CalorieAdapter.OnDeleteClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CalorieAdapter
    private val calorieList = mutableListOf<Daily_Calorie_Requirements.DailyCalorieData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_calorie)

        recyclerView = findViewById(R.id.rv_Track_Calorie)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CalorieAdapter(this, calorieList, this)
        recyclerView.adapter = adapter

        // Call a function to fetch data from Firebase and update the list
        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("tb_Daily_Calorie")

        collectionReference.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("Firestore", "Error fetching data: ${exception.message}")
                return@addSnapshotListener
            }

            calorieList.clear()

            for (document in snapshot?.documents ?: emptyList()) {
                try {
                    val data =
                        document.toObject(Daily_Calorie_Requirements.DailyCalorieData::class.java)
                    if (data != null) {
                        // Assuming you have a field named documentId in your data model
                        data.documentId = document.id
                        calorieList.add(data)
                    }
                } catch (e: Exception) {
                    Log.e("Firestore", "Error converting document: ${e.message}")
                }
            }

            adapter.notifyDataSetChanged()
        }
    }

    override fun onDeleteClick(position: Int) {
        val documentId = calorieList[position].documentId
        val db = FirebaseFirestore.getInstance()
        val collectionReference = db.collection("tb_Daily_Calorie")

        collectionReference.document(documentId).delete()
            .addOnSuccessListener {
                Log.d("Firestore", "Document deleted successfully")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error deleting document: ${e.message}")
            }
    }
}