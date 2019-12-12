package io.dume.dume.firstTimeUser.Fragment

import android.os.Bundle
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
import io.dume.dume.firstTimeUser.ForwardFlowViewModel

class RegisterFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.run {
            viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        } ?: throw Throwable("invalid activity")
        navController = Navigation.findNavController(view)
        init()
        initObservers()
    }

    private fun init() {
        /*arguments?.let {
            val args = RegisterFragmentArgs.fromBundle(it)
            register_name.setText(args.nidName)
            register_birth_date.setText(args.nidBirthDate)
            nidNo.setText(args.nidNo.toString())
        }*/

    }

    private fun flush(msg: String?) {
        msg?.let {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }

    }

    private fun initObservers() {
        viewModel.scan.observe(this, Observer {
            it?.let {
                flush("NID Data Recived")
            }
        })
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.registerBtn -> {
                navController.navigate(R.id.action_registerFragment_to_qualificationFragment)
            }
        }
    }
}
