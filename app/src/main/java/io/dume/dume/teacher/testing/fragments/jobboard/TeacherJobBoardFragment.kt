package io.dume.dume.teacher.testing.fragments.jobboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import carbon.widget.RecyclerView
import io.dume.dume.R
import io.dume.dume.teacher.testing.adapters.CirclePagerIndicatorDecoration
import io.dume.dume.teacher.testing.adapters.FeatureCardSlider
import io.dume.dume.teacher.testing.adapters.JobItemCardAdapter


class TeacherJobBoardFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var jobBoardViewModel: TeacherJobBoardViewModel? = null

    val jAdapter: JobItemCardAdapter = JobItemCardAdapter()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        jobBoardViewModel = ViewModelProviders.of(this).get(TeacherJobBoardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teacher_job_board, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val job_items_rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.job_items_rv)
        val job_card_rv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.job_card_rv)
        val swipe_to_refres = view.findViewById<SwipeRefreshLayout>(R.id.swipe_to_refres)
        job_items_rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        job_items_rv.adapter = jAdapter

        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.mColorPrimaryVariant))
        job_card_rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        job_card_rv.addItemDecoration(CirclePagerIndicatorDecoration())
        job_card_rv.adapter = FeatureCardSlider()

        /*    var cardSnapHelper = CardSnapHelper()
            cardSnapHelper.attachToRecyclerView(job_card_rv)*/

        jobBoardViewModel!!.isLoading.observe(this, Observer {
            /*if (it) {
                job_items_loading_pb.visibility = View.VISIBLE
            } else {
                job_items_loading_pb.visibility = View.GONE
            }*/
        })
        jobBoardViewModel!!.getAllJobs().observe(this, Observer {
            // set adapter or update it..
            updateJobRecView(it)
        })

    }

    private fun updateJobRecView(updatedList: List<JobItem>) {
        // Live data is updated...
        jAdapter.jobItems = updatedList
        jAdapter.notifyDataSetChanged()
        // Log.d("JobBoardActivity", "updated list")
    }

    override fun onRefresh() {

    }


}