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
import io.dume.dume.firstTimeUser.*
import kotlinx.android.synthetic.main.fragment_qualification.*

class QualificationFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var parent: ForwardFlowHostActivity



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_qualification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        parent = activity as ForwardFlowHostActivity
        initialize()
    }

    private fun initialize() {
        skipBtn.setOnClickListener(this)
        initObservers()
    }

    private fun initObservers() {
        viewModel.success.observe(this, Observer {
            it.getContentIfNotHandled()?.let{
                it.let { parent.flush("Success Again Called :  ${it.payload}") }
            }
        })
        viewModel.failure.observe(this, Observer { it?.let { parent.flush("Failure Again Called") } })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.skipBtn -> {
                navController.navigate(R.id.action_qualificationFragment_to_addSkillFragment)
            }
        }
    }
}
