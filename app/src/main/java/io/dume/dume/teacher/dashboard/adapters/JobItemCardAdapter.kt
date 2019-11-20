package io.dume.dume.teacher.dashboard.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import io.dume.dume.R
import io.dume.dume.teacher.dashboard.jobboard.models.JobItem
import java.util.ArrayList

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

        jTitle?.text = jobItems[position].title
        jDesc?.text = jobItems[position].description
        salary.text = jobItems[position].salary.toString()
        location.text = jobItems[position].location

        card.setOnClickListener {
            Toast.makeText(context, "clicked  on: $position", Toast.LENGTH_SHORT).show()
        }


    }

    override fun getItemCount(): Int {
        return jobItems.size
    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }

}