package petra.ac.id.proyekpaba.markus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import petra.ac.id.proyekpaba.R

class adapterBMI_History(private val listDataBMI_History: ArrayList<BMI_Result>): RecyclerView.Adapter<adapterBMI_History.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvTtanggal: TextView = itemView.findViewById(R.id.tv_tanggal)
        var _tvAge: TextView = itemView.findViewById(R.id.tv_Age)
        var _tvWeigth: TextView = itemView.findViewById(R.id.tv_Weight)
        var _tvHeigth: TextView = itemView.findViewById(R.id.tv_Height)
        var _tvScore: TextView = itemView.findViewById(R.id.tv_scoreBMI)
        var _tvStatus: TextView = itemView.findViewById(R.id.tv_statusBMI)
        var _tvUsername: TextView = itemView.findViewById(R.id.tv_username)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: BMI_Result)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bmi_history, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDataBMI_History.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dataHistory = listDataBMI_History[position]
        holder._tvUsername.text = dataHistory.namaAkun
        holder._tvAge.text = dataHistory.age
        holder._tvWeigth.text = dataHistory.weight
        holder._tvHeigth.text = dataHistory.height
        holder._tvScore.text = dataHistory.scoreBMI
        holder._tvStatus.text = dataHistory.statusBMI
        holder._tvTtanggal.text = dataHistory.date

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataHistory)
        }
    }

}