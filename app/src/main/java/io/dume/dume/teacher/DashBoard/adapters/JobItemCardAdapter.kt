package io.dume.dume.teacher.DashBoard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.dume.dume.R
import io.dume.dume.teacher.DashBoard.fragments.jobboard.JobItem

class JobItemCardAdapter : RecyclerView.Adapter<JobItemCardAdapter.VH>() {

    lateinit var context: Context
    var jobItems: List<JobItem> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.job_item_card, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val card: MaterialCardView = holder.itemView as MaterialCardView

        val jTitle = card.findViewById<TextView>(R.id.job_item_title)
        val jDesc = card.findViewById<TextView>(R.id.job_item_description)
        val location = card.findViewById<TextView>(R.id.job_item_location)
        val salary = card.findViewById<TextView>(R.id.job_item_salary)
        val jImg = card.findViewById<ImageView>(R.id.job_item_image)
        //...

        val applyBtn = card.findViewById<Button>(R.id.job_item_apply)

        jTitle?.text = jobItems[position].title
        jDesc?.text = jobItems[position].description
        salary.text = jobItems[position].salary.toString()
        location.text = jobItems[position].location

        applyBtn.setOnClickListener { v ->
            Toast.makeText(context, "applying for: ${jobItems[position].title}", Toast.LENGTH_SHORT).show()
            // create a record as pending here..

        }


    }

    override fun getItemCount(): Int {
        return jobItems.size
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

}