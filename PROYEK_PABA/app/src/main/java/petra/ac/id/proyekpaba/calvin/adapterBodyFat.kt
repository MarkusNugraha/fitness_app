package petra.ac.id.proyekpaba.calvin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import petra.ac.id.proyekpaba.R

class adapterBodyFat(private val listdatabodyfat: ArrayList<databodyfat>)
    : RecyclerView.Adapter<adapterBodyFat.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: databodyfat)
        fun delData(pos: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvage: TextView = itemView.findViewById(R.id.tv_age)
        var _tvgender: TextView = itemView.findViewById(R.id.tv_gender)
        var _tvweight: TextView = itemView.findViewById(R.id.tv_weight)
        var _tvheight: TextView = itemView.findViewById(R.id.tv_height)
        var _tvneck: TextView = itemView.findViewById(R.id.tv_neck)
        var _tvwaist: TextView = itemView.findViewById(R.id.tv_waist)
        var _tvhip: TextView = itemView.findViewById(R.id.tv_hip)
        var _tvnavyMethod: TextView = itemView.findViewById(R.id.tv_navymethod)
        var _tvcategory: TextView = itemView.findViewById(R.id.tv_category)
        var _tvfatMass: TextView = itemView.findViewById(R.id.tv_fatmass)
        var _tvleanBodyMass: TextView = itemView.findViewById(R.id.tv_leanbodymass)
        var _tvbmimethod: TextView = itemView.findViewById(R.id.tv_bmimethod)
        var _btndelete: TextView = itemView.findViewById(R.id.btn_hapusdata)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bodyfat, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listdatabodyfat.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val databodyfat = listdatabodyfat[position]
        holder._tvage.text = databodyfat.age
        holder._tvgender.text = databodyfat.gender
        holder._tvweight.text = databodyfat.weight
        holder._tvheight.text = databodyfat.height
        holder._tvneck.text = databodyfat.neck
        holder._tvwaist.text = databodyfat.waist
        holder._tvhip.text = databodyfat.hip
        holder._tvnavyMethod.text = databodyfat.navyMethod
        holder._tvcategory.text = databodyfat.category
        holder._tvfatMass.text = databodyfat.fatMass
        holder._tvleanBodyMass.text = databodyfat.leanBodyMass
        holder._tvbmimethod.text = databodyfat.bmiMethod

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(databodyfat)
        }

        holder._btndelete.setOnClickListener {
            onItemClickCallback.delData(position)
        }
    }

}