package io.dume.dume.firstTimeUser.Fragment

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
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
import io.dume.dume.teacher.DashBoard.TeacherDashboard
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_verification.*

class VerificationFragment : Fragment() {

    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var parent: ForwardFlowHostActivity
    private lateinit var myTimer: CountDownTimer

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
        viewModel.isExiting.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                when (it.state.userStat) {
                    UserState.ERROR.userStat -> {
                        parent.flush("Check your internet connection and try again")
                    }
                    UserState.NEWUSERFOUND.userStat -> {
                        Log.e("TAG", "on new user found")

                        if (viewModel.role.value == Role.STUDENT) {
                            viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.REGISTER)
                            navController.navigate(R.id.action_verificationFragment_to_registerFragment)
                        } else {
                            viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.NID)
                            navController.navigate(R.id.action_verificationFragment_to_nidFragment)
                        }
                    }
                    UserState.USERFOUND.userStat -> {
                        Log.e("Joaa", it.response.toString())
                        val isEducated = if (it.response?.educated == null) false else it.response?.educated as Boolean
                        if (isEducated) {
                            viewModel.updateFirstTimeUser(false)
                            Log.e("TAG", "on uaser found educated true")
                            if (viewModel.role.value == Role.STUDENT) {
                                viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.LOGIN)
                                startActivity(Intent(activity, StudentDashBoard::class.java))
                                finish()
                            } else {
                                viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.LOGIN)
                                startActivity(Intent(activity, TeacherDashboard::class.java))
                                finish()
                            }
                        } else {
                            if (viewModel.role.value == Role.STUDENT) {
                                viewModel.updateStudentCurrentPosition(ForwardFlowStatStudent.POSTJOB)
                                navController.navigate(R.id.action_verificationFragment_to_postJobFragment)
                            } else {
                                viewModel.updateTeacherCurrentPosition(ForwardFlowStatTeacher.QUALIFICATION)
                                navController.navigate(R.id.action_verificationFragment_to_qualificationFragment)
                            }
                        }
                    }
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer {
            it?.let {
                parent.flush(it)
                verifyFab.isEnabled = true
            }
        })
        viewModel.load.observe(viewLifecycleOwner, Observer {
            if (it) {
                parent.showProgress()
                sendCodeBtn?.isEnabled = false
            } else {
                parent.hideProgress()
                sendCodeBtn?.isEnabled = true
            }
        })
        viewModel.phoneNumber.observe(viewLifecycleOwner, Observer {
            it?.let {
                detailsTxt.text = Html.fromHtml("Enter the 6 digit verification code sent to you at <br/><b><b>+88 %s </b></b>".format(it))
            }
        })

    }


    private fun finish() {
        activity?.finish()
    }

    private fun initListeners() {
        pinEditTxt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = run { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = run { }
            override fun afterTextChanged(s: Editable?) {
                verifyFab.isEnabled = s.toString().length == 6
            }
        })
        verifyFab.setOnClickListener {
            it.isEnabled = false
            viewModel.matchCode(pinEditTxt.text.toString())
        }
        resendButton.setOnClickListener {
            it.visibility = View.GONE
            timerTxt.visibility = View.VISIBLE
            myTimer.start()
            viewModel.resendCode(phoneNumber = phoneNumberEditText.text.toString())
        }

        myTimer = object : CountDownTimer(60000, 1000) {
            override fun onFinish() = run {
                resendButton?.visibility = View.VISIBLE
                timerTxt?.visibility = View.GONE
                verifyFab?.isEnabled = true
            }

            override fun onTick(millisUntilFinished: Long) = run { timerTxt?.text = StringBuilder("Resend Code in ${millisUntilFinished.div(1000)} seconds").toString() }
        }
        myTimer.start()

    }

    override fun onDetach() {
        super.onDetach()
        myTimer.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        myTimer.cancel()
    }

}
