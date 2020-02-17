package io.dume.dume.student.DashBoard.Fragment.JobBoard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.student.DashBoard.StudentDashBoard
import io.dume.dume.teacher.DashBoard.adapters.JobItemCardAdapter
import io.dume.dume.teacher.DashBoard.fragments.jobboard.JobItem
import kotlinx.android.synthetic.main.fragment_student_job_board.*
import kotlinx.android.synthetic.main.fragment_student_job_board.view.*


class StudentJobBoardFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var viewModelStudent: StudentJobBoardViewModel
    private lateinit var vm: ForwardFlowViewModel
    private var jobItemCardAdapter = JobItemCardAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModelStudent = ViewModelProviders.of(this).get(StudentJobBoardViewModel::class.java)
        activity?.run {
            vm = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        return inflater.inflate(R.layout.fragment_student_job_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    private fun initialize() {
        initializeFabs()
        val destination = arguments?.getString(DESTINATION)
        if (destination != "forward") {
            jobRecycleView.visibility = View.VISIBLE
            no_data_block.visibility = View.GONE
            initializeRV()
        } else {
            jobRecycleView.visibility = View.GONE
            no_data_block.visibility = View.VISIBLE
        }
        initializeButtons()
    }

    private fun initializeButtons() {
        jobPost.setOnClickListener {
            jobMultiple.visibility = View.VISIBLE
            jobMultiple.expand()
        }
        jobSkip.setOnClickListener {
            vm.firstTimeUser.value = false
            startActivity(Intent(context, StudentDashBoard::class.java))
            activity?.finish()
        }
    }

    private fun initializeRV() {
        jobRecycleView.layoutManager = LinearLayoutManager(context, carbon.widget.RecyclerView.VERTICAL, false)
        jobRecycleView.adapter = jobItemCardAdapter
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.mColorPrimaryVariant))
        swipe_to_refres.setOnRefreshListener(this)
        viewModelStudent.fetchJobs()
        viewModelStudent.jobListLive.observe(this, Observer {
            updateJobRecView(it)
            swipe_to_refres.isRefreshing = false
        })

    }

    private fun initializeFabs() {
        jobRecycleView.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        jobPost.hide()
                        jobSkip.hide()
                        jobMultiple.visibility = View.INVISIBLE
                    } else if (dy < 0) {
                        jobPost.show()
                        jobSkip.show()
                        jobMultiple.visibility = View.VISIBLE
                    }
                }


            })
        }
    }

    private fun updateJobRecView(updatedList: List<JobItem>) {
        jobItemCardAdapter.jobItems = updatedList
        jobItemCardAdapter.notifyDataSetChanged()
    }

    override fun onRefresh() {
        viewModelStudent.fetchJobs()
    }

    companion object {
        const val DESTINATION = "destination"
    }

}