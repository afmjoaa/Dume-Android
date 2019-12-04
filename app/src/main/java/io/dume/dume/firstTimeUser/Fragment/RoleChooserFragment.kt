package io.dume.dume.firstTimeUser.Fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.firstTimeUser.PermissionActivity
import io.dume.dume.student.DashBoard.StudentDashBoard
import io.dume.dume.teacher.dashboard.jobboard.JobBoardActivity
import kotlinx.android.synthetic.main.activity_role_chooser.view.*
import kotlinx.android.synthetic.main.fragment_role_chooser.view.*
import kotlinx.android.synthetic.main.fragment_role_chooser.view.asStudent
import kotlinx.android.synthetic.main.fragment_role_chooser.view.asTeacher

class RoleChooserFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_role_chooser, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.testJoaa.setOnClickListener(this)
        view.testEnam.setOnClickListener(this)
        view.testSumon.setOnClickListener(this)
        view.asTeacher.setOnClickListener(this)
        view.asStudent.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        viewModel.updateActionBarTitle("Custom Title")
    }

    override fun onClick(v: View?) {
        if (v!!.id == R.id.asStudent) {
            startActivity(Intent(activity, PermissionActivity::class.java))
        } else if (v.id == R.id.asTeacher) {
            startActivity(Intent(activity, PermissionActivity::class.java))
        } else if (v.id == R.id.testEnam) {
            startActivity(Intent(activity, JobBoardActivity::class.java))
            return
        } else if (v.id == R.id.testJoaa) {
            startActivity(Intent(activity, ForwardFlowHostActivity::class.java))
            return
        } else if (v.id == R.id.testSumon) {
            startActivity(Intent(activity, StudentDashBoard::class.java))
            return
        }
        startActivity(Intent(activity, PermissionActivity::class.java))
    }
}
