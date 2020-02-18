package io.dume.dume.teacher.DashBoard.fragments.skills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowStatTeacher
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.util.StateManager
import kotlinx.android.synthetic.main.fragment_teacher_skill.*

class TeacherSkillFragment : Fragment() {
    private lateinit var viewModel: TeacherSkillViewModel
    private var navController: NavController? = null
    private lateinit var forwardFlowViewModel: ForwardFlowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_teacher_skill, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            navController = Navigation.findNavController(view)
        } catch (exception: Exception) {

        }
        viewModel = ViewModelProviders.of(this).get(TeacherSkillViewModel::class.java)
        init()
    }

    private fun init() {
        skipBtn.setOnClickListener {
            updateForwardFlowState()
            navController?.navigate(R.id.action_addSkillFragment_to_paymentFragment)
        }
    }

    private fun updateForwardFlowState() {
        context?.apply {
            StateManager.getInstance(this).setTeacherCurrentState(ForwardFlowStatTeacher.ADDSKILL.flow)
        }
    }


}