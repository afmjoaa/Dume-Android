package io.dume.dume.teacher.dashboard.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import io.dume.dume.R
import io.dume.dume.student.recordsPage.Record
import kotlinx.android.synthetic.main.tution_deal_item.view.*


class TutionAdapter(var t: List<Record>) : RecyclerView.Adapter<TutionAdapter.VH>() {

    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.tution_deal_item, parent, false))
    }

    override fun getItemCount(): Int {
        return t.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val record = t[position]
        holder.itemView.mentor_name.text = record.mentorName
        holder.itemView.student_name.text = record.studentName
        holder.itemView.subject_in_demand.text = record.subjectExchange
        holder.itemView.tution_status.text = record.status
        holder.itemView.salary_in_demand.text = record.salaryInDemand
        Glide.with(holder.itemView.context).load(record.mentorDpUrl).into(holder.itemView.mentor_display_pic)
        Glide.with(holder.itemView.context).load(record.studentDpUrl).into(holder.itemView.student_display_pic)
        if (record.status.equals("Pending")) {
            holder.itemView.tution_status.setTextColor(Color.GREEN)
        } else if (record.status.equals("Rejected")) {
            holder.itemView.tution_status.setTextColor(Color.GRAY)

        } else if (record.status.equals("Completed")) {
            holder.itemView.tution_status.setTextColor(ContextCompat.getColor(context, R.color.mColorPrimaryVariant))

        } else if (record.status.equals("Current")) {
            holder.itemView.tution_status.text = "Running Now"
            holder.itemView.tution_status.setTextColor(Color.BLUE)


        }

    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}