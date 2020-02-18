package io.dume.dume.firstTimeUser.Fragment

import android.annotation.SuppressLint
import android.content.Context.TELEPHONY_SERVICE
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.*
import io.dume.dume.util.DumeUtils
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var parent: ForwardFlowHostActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        parent = activity as ForwardFlowHostActivity
        navController = Navigation.findNavController(view)
        init()
        initObservers()
    }


    @SuppressLint("MissingPermission", "HardwareIds")
    private fun init() {
        try {
            val tMgr = context?.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val mPhoneNumber = tMgr.line1Number
            phoneNumberEditText.setText(mPhoneNumber)
        } catch (e: Exception) {
            print(e)
        }

        sendCodeBtn.setOnClickListener(this)
        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val phoneNumber = s.toString()
                if (phoneNumber.isEmpty()) {
                    showErrorInInput("Should not be empty")
                    sendCodeBtn.isEnabled = false
                } else if (!DumeUtils.isInteger(phoneNumber)) {
                    showErrorInInput("Only Digits Allowed (0-9)")
                    sendCodeBtn.isEnabled = false
                } else if (phoneNumber.length > 11) {
                    showErrorInInput("Should be 11 Digits")
                    sendCodeBtn.isEnabled = false
                } else {
                    viewModel.phoneNumber.postValue(s.toString())
                    sendCodeBtn.isEnabled = true
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = run { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = run {
                phoneWrapper.error = null
            }

        })
    }

    private fun initObservers() {

        viewModel.autoVerified.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                if (it) skipNextAction()
            }
        })
        viewModel.codeSent.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let {
                if (it) {
                    flush("Code sent to your number")
                    nextAction()
                }
            }
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { it?.let { flush(it) } })
        viewModel.load.observe(viewLifecycleOwner, Observer {
            if (it) {
                parent.showProgress()
                sendCodeBtn.isEnabled = false
            } else {
                parent.hideProgress()
                sendCodeBtn.isEnabled = true

            }
        })
    }


    private fun nextAction() {
        navController.navigate(R.id.action_loginFragment_to_verificationFragment)
    }

    private fun skipNextAction() {
        if (viewModel.role.value == Role.TEACHER) {
            navController.navigate(R.id.action_loginFragment_to_nidFragment)
        } else {
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun flush(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }

    private fun showErrorInInput(msg: String) {
        phoneWrapper.setError(msg)

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.sendCodeBtn -> {
                if (phoneNumberEditText.text.toString().length != 11) showErrorInInput("Should be 11 Digits")
                else viewModel.login(phoneNumberEditText.text.toString())
            }
        }
    }
}

