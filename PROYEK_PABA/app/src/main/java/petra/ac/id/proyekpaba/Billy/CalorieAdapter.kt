package petra.ac.id.proyekpaba.Billy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import petra.ac.id.proyekpaba.Billy.Edit_Daily_Calorie
import petra.ac.id.proyekpaba.R
import java.text.SimpleDateFormat
import java.util.Locale

class CalorieAdapter(
    private val context: Context,
    private val calorieList: List<Daily_Calorie_Requirements.DailyCalorieData>,
    private val onDeleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<CalorieAdapter.CalorieViewHolder>() {

    interface OnDeleteClickListener {
        fun onDeleteClick(position: Int)
    }

    inner class CalorieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAge: TextView = itemView.findViewById(R.id.tv_Age_calorie)
        val tvWeight: TextView = itemView.findViewById(R.id.tv_Weight_calorie)
        val tvHeight: TextView = itemView.findViewById(R.id.tv_Height_Calorie)
        val tvMode: TextView = itemView.findViewById(R.id.tv_mode_calorie)
        val tvDate: TextView = itemView.findViewById(R.id.tv_tanggal_Calorie)
        val tvGender: TextView = itemView.findViewById(R.id.tv_Gender)
        val tvCalorie: TextView = itemView.findViewById(R.id.tv_Calorie_rv)
        val btnEdit: Button = itemView.findViewById(R.id.btn_Edit_calorie)
        val btnDelete: Button = itemView.findViewById(R.id.btn_remove_Calorie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalorieViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calorie, parent, false)
        return CalorieViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CalorieViewHolder, position: Int) {
        val currentItem = calorieList[position]

        holder.tvAge.text = "${currentItem.age}"
        holder.tvWeight.text = "${currentItem.weight} kg"
        holder.tvHeight.text = "${currentItem.height} cm"
        holder.tvMode.text = "Mode: ${currentItem.mode}"
        holder.tvGender.text = "Gender: ${currentItem.gender}"
        holder.tvCalorie.text = "Calorie: \n${currentItem.weightLossCalories}"

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.tvDate.text = "Date: ${dateFormat.format(currentItem.date.toDate())}"

        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, Edit_Daily_Calorie::class.java)
            // Pass necessary data to the Edit_Daily_Calorie activity if needed
            intent.putExtra("documentId", currentItem.documentId)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClickListener.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return calorieList.size
    }
}
