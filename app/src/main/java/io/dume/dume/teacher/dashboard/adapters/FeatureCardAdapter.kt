package io.dume.dume.teacher.dashboard.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import io.dume.dume.R

class FeatureCardSlider : RecyclerView.Adapter<FeatureCardSlider.VH>() {

    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        context = parent.context
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.slider_card_item, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

        val card: MaterialCardView = holder.itemView as MaterialCardView
        if (position == 1) {
            card.setCardBackgroundColor(Color.WHITE)
        } else if (position == 2) {
            card.setCardBackgroundColor(Color.WHITE)
        } else {
            card.setCardBackgroundColor(Color.WHITE)

        }


    }


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

}