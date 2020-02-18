package io.dume.dume.teacher.DashBoard.fragments.jobboard

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
import io.dume.dume.teacher.DashBoard.adapters.CirclePagerIndicatorDecoration
import io.dume.dume.teacher.DashBoard.adapters.FeatureCardSlider
import io.dume.dume.teacher.DashBoard.adapters.JobItemCardAdapter


class TeacherJobBoardFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var jobBoardViewModel: TeacherJobBoardViewModel? = null

    val jAdapter: JobItemCardAdapter = JobItemCardAdapter()
    private lateinit var swipeToRefresh: SwipeRefreshLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        jobBoardViewModel = ViewModelProviders.of(this).get(TeacherJobBoardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teacher_job_board, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val jobItemsRv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.job_items_rv)
        val jobCardRv = view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.job_card_rv)
        swipeToRefresh = view.findViewById(R.id.swipe_to_refres)
        jobItemsRv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        jobItemsRv.adapter = jAdapter

        swipeToRefresh.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.mColorPrimaryVariant))
        swipeToRefresh.setOnRefreshListener(this)
        jobCardRv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        jobCardRv.addItemDecoration(CirclePagerIndicatorDecoration())
        jobCardRv.adapter = FeatureCardSlider()


        jobBoardViewModel!!.__getAllJobs()

        jobBoardViewModel!!.jobListLive.observe(this, Observer {
            updateJobRecView(it)
            swipeToRefresh.isRefreshing = false
        })

    }

    private fun updateJobRecView(updatedList: List<JobItem>) {
        // Live data is updated...
        jAdapter.jobItems = updatedList
        jAdapter.notifyDataSetChanged()
        // Log.d("JobBoardActivity", "updated list")
    }

    override fun onRefresh() {
        // Toast.makeText(context, "swipe", Toast.LENGTH_SHORT).show()
        // load data...
        jobBoardViewModel!!.__getAllJobs()

    }
}