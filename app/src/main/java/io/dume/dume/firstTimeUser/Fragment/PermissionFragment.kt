package io.dume.dume.firstTimeUser.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowViewModel
import io.dume.dume.firstTimeUser.PrivacyActivity
import kotlinx.android.synthetic.main.fragment_permission.*

class PermissionFragment : Fragment(), View.OnClickListener {
    private lateinit var navController: NavController
    private lateinit var viewModel: ForwardFlowViewModel
    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private val LOCATION_PERMISSION_REQUEST_CODE = 1234


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        init()
    }

    private fun init() {
        permissionBtn.setOnClickListener(this)
    }

    private fun checkPermissions(): Boolean {
        val resOne: Int? = context?.checkCallingOrSelfPermission(FINE_LOCATION)
        val resTwo: Int? = context?.checkCallingOrSelfPermission(COURSE_LOCATION)
        return (resOne == PackageManager.PERMISSION_GRANTED
                && resTwo == PackageManager.PERMISSION_GRANTED)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.permissionBtn -> {
                if (checkPermissions()) {

                } else {
                    checkPermissions()
                }
                navController.navigate(R.id.action_permissionFragment_to_loginFragment)
            }
        }
    }
}
