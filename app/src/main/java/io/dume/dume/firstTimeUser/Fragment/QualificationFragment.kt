package io.dume.dume.firstTimeUser.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.firstTimeUser.ForwardFlowStatTeacher
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import kotlinx.android.synthetic.main.fragment_qualification.*

class QualificationFragment : Fragment() {
    private var navController: NavController? = null
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var parent: ForwardFlowHostActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qualification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            navController = Navigation.findNavController(view)
        } catch (exception: Exception) {

        }
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        parent = activity as ForwardFlowHostActivity
        initialize()
    }

    private fun initialize() {
        initializeButton()
        initObservers()
    }

    private fun initializeButton() {
        skipBtn.setOnClickListener {
            updateForwardFlowState()
            navController?.navigate(R.id.action_qualificationFragment_to_addSkillFragment)
        }
        add_qualification_btn.setOnClickListener {

        }
    }

    private fun initObservers() {
        viewModel.success.observe(this, Observer { it?.let { parent.flush("Success Again Called :  ${it.payload}") } })
        viewModel.failure.observe(this, Observer { it?.let { parent.flush("Failure Again Called") } })
    }

    private fun updateForwardFlowState() {
        viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.ADDSKILL)
    }


}
