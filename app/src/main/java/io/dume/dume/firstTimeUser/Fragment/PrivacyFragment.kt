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

class PrivacyFragment : Fragment(){
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var parent: ForwardFlowHostActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")

        navController = Navigation.findNavController(view)
        parent = activity as ForwardFlowHostActivity
        init()
    }

    private fun init() {
        initObserver()
    }

    private fun initObserver(){
        parent.onContinuePrivacyClick {
            it.let {
                parent.showProgress()
                viewModel.privacyContinue()
            }
        }

        viewModel.success.observe(this, Observer{
            it.getContentIfNotHandled()?.let{
                if (viewModel.role.value == Role.STUDENT) {
                    viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.PERMISSION)
                } else {
                    viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.PERMISSION)
                }
                parent.hideProgress()
                navController.navigate(R.id.action_privacyFragment_to_permissionFragment)
            }
        })
    }

}
