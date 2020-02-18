package io.dume.dume.firstTimeUser.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.teacher.DashBoard.TeacherDashboard
import io.dume.dume.util.StateManager
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment : Fragment() {
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var navController: NavController

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ForwardFlowViewModel::class.java)
        activity?.let { viewModel.inject(it) }
        navController = Navigation.findNavController(view)
        initialize()
    }

    private fun initialize() {
        initializeButton()

    }

    private fun initializeButton() {
        pay_now.setOnClickListener {

        }
        pay_later?.setOnClickListener {
            updateForwardFlowState()
            startActivity(Intent(it.context, TeacherDashboard::class.java))
        }

    }


    private fun updateForwardFlowState() = context?.let {
        StateManager.getInstance(it).setFirstTimeUser(false)
        FirebaseAuth.getInstance().currentUser?.let {
            viewModel.toggleIsEducatedSync(it.uid, true)
        }
    }


}
