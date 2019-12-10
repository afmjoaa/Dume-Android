package io.dume.dume.teacher.testing.fragments.skills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import kotlinx.android.synthetic.main.fragment_teacher_skill.*

class TeacherSkillFragment : Fragment(),View.OnClickListener {
    private lateinit var teacherSkillViewModel: TeacherSkillViewModel
    private lateinit var navController: NavController
    private lateinit var forwardFlowViewModel: ForwardFlowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        teacherSkillViewModel = ViewModelProviders.of(this).get(TeacherSkillViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teacher_skill, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
    }

    private fun init() {
        skipBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.skipBtn -> {
                navController.navigate(R.id.action_addSkillFragment_to_paymentFragment)
            }
        }
    }
}