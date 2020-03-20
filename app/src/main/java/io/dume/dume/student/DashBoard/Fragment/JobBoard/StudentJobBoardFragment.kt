package io.dume.dume.student.DashBoard.Fragment.JobBoard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.dume.dume.R
import io.dume.dume.firstTimeUser.Flag
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.student.DashBoard.StudentDashBoard
import io.dume.dume.teacher.DashBoard.adapters.JobItemCardAdapter
import io.dume.dume.teacher.DashBoard.fragments.jobboard.JobItem
import io.dume.dume.teacher.crudskill.CrudSkillActivity
import io.dume.dume.util.StateManager
import kotlinx.android.synthetic.main.activity_forward_flow_host.*
import kotlinx.android.synthetic.main.fragment_student_job_board.*


class StudentJobBoardFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var viewModelStudent: StudentJobBoardViewModel
    private var jobItemCardAdapter = JobItemCardAdapter()
    private lateinit var jobPrimaryDialog: AlertDialog
    private var fromForwardFlow: Boolean = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModelStudent = ViewModelProvider(this).get(StudentJobBoardViewModel::class.java)
        return inflater.inflate(R.layout.fragment_student_job_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity is ForwardFlowHostActivity) {
            fromForwardFlow = true
        }
    }

    private fun initialize() {
        initializeFabs()
        initializeRV()
        initializeButtons()
        initDialog()
    }

    private fun goToGrabbingActivity() {
        context?.apply {
            if (fromForwardFlow) startActivity(Intent(this, CrudSkillActivity::class.java).setAction(Flag.FORWARD_STUDENT.flow))
            else startActivity(Intent(this, CrudSkillActivity::class.java).setAction(Flag.STUDENT_POST.flow))
        }

    }

    private fun initializeButtons() {

        if (fromForwardFlow) {
            val parent = activity as ForwardFlowHostActivity
            parent.jobPost.setOnClickListener {
                if (parent.jobPost.isExtended) {
                    parent.jobPost.shrink()
                    parent.jobMultiple.visibility = View.VISIBLE
                    parent.jobMultiple.expand()
                } else {
                    parent.jobPost.extend()
                    parent.jobMultiple.collapseImmediately()
                    parent.jobMultiple.visibility = View.INVISIBLE
                }
            }
            parent.jobSkip.setOnClickListener {
                context?.let {
                    StateManager.getInstance(it).setFirstTimeUser(false)
                }
                startActivity(Intent(context, StudentDashBoard::class.java))
                parent.finish()
            }

            parent.fab_regular.setOnClickListener {
                parent.jobPost.performClick()
                jobPrimaryDialog.show()
            }
            parent.fab_instant.setOnClickListener {
                parent.jobPost.performClick()
                jobPrimaryDialog.show()
            }

        } else {
            val parent = activity as StudentDashBoard
            parent.jobPost.setOnClickListener {
                if (parent.jobPost.isExtended) {
                    parent.jobPost.shrink()
                    parent.jobMultiple.visibility = View.VISIBLE
                    parent.jobMultiple.expand()
                } else {
                    parent.jobPost.extend()
                    parent.jobMultiple.collapseImmediately()
                    parent.jobMultiple.visibility = View.INVISIBLE
                }
            }
            parent.jobSkip.setOnClickListener {
                context?.let {
                    StateManager.getInstance(it).setFirstTimeUser(false)
                }
                startActivity(Intent(context, StudentDashBoard::class.java))
                parent.finish()
            }

            parent.fab_regular.setOnClickListener {
                parent.jobPost.performClick()
                jobPrimaryDialog.show()
            }
            parent.fab_instant.setOnClickListener {
                parent.jobPost.performClick()
                jobPrimaryDialog.show()
            }
        }


    }


    private fun initializeRV() {
        jobRecycleView.layoutManager = LinearLayoutManager(context, carbon.widget.RecyclerView.VERTICAL, false)
        jobRecycleView.adapter = jobItemCardAdapter
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(context!!, R.color.mColorPrimaryVariant))
        swipe_to_refres.setOnRefreshListener(this)
        viewModelStudent.fetchJobs()
        viewModelStudent.jobListLive.observe(viewLifecycleOwner, Observer {
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
        if (updatedList.isNullOrEmpty()) {
            no_data_block.visibility = View.VISIBLE
            jobRecycleView.visibility = View.GONE

        } else {
            no_data_block.visibility = View.GONE
            jobRecycleView.visibility = View.VISIBLE

            jobItemCardAdapter.jobItems = updatedList
            jobItemCardAdapter.notifyDataSetChanged()
        }
    }

    override fun onRefresh() {
        viewModelStudent.fetchJobs()
    }

    fun initDialog() {
        /**
         * Dialog that notifies user about HOW NID VERIFICATION WORKS?
         * */
        val materialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
        val customLayout = layoutInflater.inflate(R.layout.job_board_primary_info, null, false)
        materialAlertDialogBuilder.setView(customLayout)
        jobPrimaryDialog = materialAlertDialogBuilder.create()
        try {
            val dismissBtn = customLayout.findViewById<Button>(R.id.dismiss_btn)
            val yesBtn = customLayout.findViewById<Button>(R.id.yes_button)
            yesBtn.setOnClickListener {
                jobPrimaryDialog.dismiss()
                goToGrabbingActivity()
            }
            dismissBtn.setOnClickListener { v ->
                jobPrimaryDialog.dismiss()
            }
        } catch (npe: NullPointerException) {
            npe.printStackTrace()
        }
    }


    private fun flush(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val DESTINATION = "destination"
    }

}