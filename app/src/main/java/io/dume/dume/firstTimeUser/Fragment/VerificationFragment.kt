package io.dume.dume.firstTimeUser.Fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import io.dume.dume.student.DashBoard.StudentDashBoard
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_verification.*

class VerificationFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var parent: ForwardFlowHostActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_verification, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parent = activity as ForwardFlowHostActivity
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        navController = Navigation.findNavController(view)
        initObservers()
        initListeners()
    }


    private fun initObservers() {
        viewModel.isExiting.observe(this, Observer {
            it?.let {
                when (it.userStat) {
                    UserState.ERROR.userStat -> {
                        parent.flush("Check your internet connection and try again")
                    }
                    UserState.NEWUSERFOUND.userStat -> {
                        if (viewModel.role.value == Role.STUDENT) {
                            viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.REGISTER)
                            navController.navigate(R.id.action_verificationFragment_to_registerFragment)
                        } else {
                            viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.NID)
                            navController.navigate(R.id.action_verificationFragment_to_nidFragment)
                        }
                    }
                    UserState.USERFOUND.userStat -> {
                        viewModel.updateFirstTimeUser(false)
                        if (viewModel.role.value == Role.STUDENT) {
                            startActivity(Intent(activity, StudentDashBoard::class.java))
                        } else {
                            startActivity(Intent(activity, StudentDashBoard::class.java))
                        }
                    }
                }
            }
        })
        viewModel.error.observe(this, Observer { it?.let { parent.flush(it) } })
        viewModel.load.observe(this, Observer {
            if (it) {
                parent.showProgress()
                sendCodeBtn?.isEnabled = false
            } else {
                parent.hideProgress()
                sendCodeBtn?.isEnabled = true
            }
        })
    }

    private fun initListeners() {
        pinEditTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = run { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = run { }
            override fun afterTextChanged(s: Editable?) {
                verifyFab.isEnabled = s.toString().length == 6
            }
        })
        verifyFab.setOnClickListener { viewModel.matchCode(pinEditTxt.text.toString()) }
        resendButton.setOnClickListener { viewModel.resendCode(phoneNumber = phoneNumberEditText.text.toString()) }
    }


}
