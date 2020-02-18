package io.dume.dume.firstTimeUser.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowStatStudent
import io.dume.dume.firstTimeUser.ForwardFlowStatTeacher
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.firstTimeUser.Role
import kotlinx.android.synthetic.main.fragment_role_chooser.view.*

class RoleChooserFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_role_chooser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.asTeacher.setOnClickListener(this)
        view.asStudent.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
    }

    override fun onClick(v: View?) {

        when (v!!.id) {
            R.id.asStudent -> {
                viewModel.updateRole(Role.STUDENT)
                viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.PRIVACY)
                navController.navigate(R.id.action_roleChooser_to_privacyFragment)
            }
            R.id.asTeacher ->{
                viewModel.updateRole(Role.TEACHER)
                viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.PRIVACY)
                navController.navigate(R.id.action_roleChooser_to_privacyFragment)
            }
        }
        //viewModel.updateFirstTimeUser(true)
    }
}
