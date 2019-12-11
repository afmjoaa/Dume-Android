package io.dume.dume.firstTimeUser.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowStatStudent
import io.dume.dume.firstTimeUser.ForwardFlowStatTeacher
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.firstTimeUser.Role
import kotlinx.android.synthetic.main.fragment_privacy.*

class PrivacyFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_privacy, container, false)
        root.findViewById<WebView>(R.id.termsWebView).loadUrl("file:///android_asset/pages/teacher_privacy.html")
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")

        navController = Navigation.findNavController(view)
        init()
    }

    private fun init() {
        continueBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.continueBtn -> {
                if (viewModel.role.value == Role.STUDENT) {
                    viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.PERMISSION)
                } else {
                    viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.PERMISSION)
                }
                navController.navigate(R.id.action_privacyFragment_to_permissionFragment)
            }
        }
    }
}
