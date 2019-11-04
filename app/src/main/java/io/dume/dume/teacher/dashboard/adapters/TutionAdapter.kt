package io.dume.dume.teacher.dashboard.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.dume.dume.R


class TutionAdapter : RecyclerView.Adapter<TutionAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {

        return VH(LayoutInflater.from(parent.context).inflate(R.layout.tution_deal_item, parent, false))
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}