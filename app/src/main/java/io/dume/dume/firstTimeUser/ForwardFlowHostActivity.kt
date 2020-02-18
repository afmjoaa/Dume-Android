package io.dume.dume.firstTimeUser


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import io.dume.dume.R
import io.dume.dume.student.pojo.BaseAppCompatActivity
import io.dume.dume.util.DumeUtils.configAppToolBarTitle
import io.dume.dume.util.DumeUtils.configureAppbar
import io.dume.dume.util.StateManager
import kotlinx.android.synthetic.main.activity_forward_flow_host.*

class ForwardFlowHostActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var navController: NavController
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forward_flow_host)
        viewModel = ViewModelProviders.of(this).get(ForwardFlowViewModel::class.java)
        viewModel.inject(this)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        init()
    }

    private fun init() {
        setActivityContext(this, 1113)
        setDarkStatusBarIcon()
        configureAppbar(this, "Testing", true)
        sharedPreferenceObserver()
        initView()
        initListener()
        updateSate()

    }

    private fun updateSate() {

        val role: String = StateManager.getInstance(this).sharedPreferences().all[StateManager.ROLE] as? String
                ?: ""
        val isFirstTimeUser: Boolean = StateManager.getInstance(this).sharedPreferences().all[StateManager.FIRST_TIME_USER] as? Boolean
                ?: true
        val teacherCurrentPosition: String = StateManager.getInstance(this).sharedPreferences().all[StateManager.TEACHER_POSITION] as? String
                ?: ""
        val studentCurrentPosition: String = StateManager.getInstance(this).sharedPreferences().all[StateManager.STUDENT_POSITION] as? String
                ?: ""
        FirebaseAuth.getInstance().currentUser?.let {
            viewModel.phoneNumber.value = it.phoneNumber?.substring(3, 14)
            Log.e("debug", "Phone Number" + it.phoneNumber?.substring(3, 14))
        }
        if (role.equals(Role.STUDENT.flow)) {
            viewModel.role.value = Role.STUDENT
            when (studentCurrentPosition) {
                ForwardFlowStatStudent.ROLE.flow -> navController.navigate(R.id.roleChooser)
                ForwardFlowStatStudent.PRIVACY.flow -> navController.navigate(R.id.privacyFragment)
                ForwardFlowStatStudent.PERMISSION.flow -> navController.navigate(R.id.permissionFragment)
                ForwardFlowStatStudent.LOGIN.flow -> navController.navigate(R.id.loginFragment)
                ForwardFlowStatStudent.REGISTER.flow -> navController.navigate(R.id.registerFragment)
                ForwardFlowStatStudent.POSTJOB.flow -> navController.navigate(R.id.postJobFragment)
            }
        } else if (role.equals(Role.TEACHER.flow)) {
            viewModel.role.value = Role.TEACHER
            when (teacherCurrentPosition) {
                ForwardFlowStatTeacher.ROLE.flow -> navController.navigate(R.id.roleChooser)
                ForwardFlowStatTeacher.PRIVACY.flow -> navController.navigate(R.id.privacyFragment)
                ForwardFlowStatTeacher.PERMISSION.flow -> navController.navigate(R.id.permissionFragment)
                ForwardFlowStatTeacher.LOGIN.flow -> navController.navigate(R.id.loginFragment)
                ForwardFlowStatTeacher.NID.flow -> navController.navigate(R.id.nidFragment)
                ForwardFlowStatTeacher.REGISTER.flow -> navController.navigate(R.id.registerFragment)
                ForwardFlowStatTeacher.QUALIFICATION.flow -> navController.navigate(R.id.qualificationFragment)
                ForwardFlowStatTeacher.ADDSKILL.flow -> navController.navigate(R.id.addSkillFragment)
                ForwardFlowStatTeacher.PAYMENT.flow -> navController.navigate(R.id.paymentFragment)

            }

        }


    }

    private fun sharedPreferenceObserver() {
        viewModel.role.observe(this, Observer {
            StateManager.getInstance(this).setRole(it.flow)
        })

        viewModel.firstTimeUser.observe(this, Observer {
            StateManager.getInstance(this).setFirstTimeUser(it)
        })

        viewModel.teacherCurrentPosition.observe(this, Observer {
            StateManager.getInstance(this).setTeacherCurrentState(it.flow)
        })

        viewModel.studentCurrentPosition.observe(this, Observer {
            StateManager.getInstance(this).setStudentCurrentState(it.flow)
        })

    }

    private fun initView() {
    }

    private fun initListener() {

        registerBtn.setOnClickListener(this)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->

            when (destination.id) {
                R.id.roleChooser -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    configAppToolBarTitle(this, "")
                    hideActionBar()
                }
                R.id.permissionFragment -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    configAppToolBarTitle(this, "Provide Permission")
                    showActionBar()
                }
                R.id.privacyFragment -> {
                    registerBtn.hide()
                    continueBtn.show()
                    showActionBar()
                    if(Role.TEACHER.flow == viewModel.role.value?.flow){
                        configAppToolBarTitle(this, "Teacher Guide")
                    }else{
                        configAppToolBarTitle(this, "Student Guide")
                    }
                }
                R.id.loginFragment -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Login")
                }
                R.id.nidFragment -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "NID Verification")
                }
                R.id.registerFragment -> {
                    registerBtn.show()
                    continueBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Provide Info")
                }
                R.id.qualificationFragment -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Add Qualification")
                }
                R.id.addSkillFragment -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Add Skill")
                }
                R.id.postJobFragment -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Post Job")
                }
                R.id.paymentFragment -> {
                    registerBtn.hide()
                    continueBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Activation Fee")
                }
            }
            settingsAppbar.setExpanded(true, true)
        }
    }


    private fun hideActionBar() {
        settingsAppbar.visibility = View.GONE
        supportActionBar?.hide()
    }

    private fun showActionBar() {
        settingsAppbar.visibility = View.VISIBLE
        supportActionBar?.show()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    fun onRegisterButtonClick(listener: (view: View) -> Unit) {
        registerBtn.setOnClickListener {
            listener(it)
        }
    }

    fun onContinuePrivacyClick(listener: (view: View) -> Unit) {
        continueBtn.setOnClickListener {
            listener(it)
        }
    }


    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.nidFragment) {
            if (doubleBackToExitPressedOnce) {
                // super.onBackPressed()
                super.onBackPressed()
                return
            }
            this.doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to start over", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        } else if (viewModel.role.value != null) {
            if (viewModel.role.value!!.equals(Role.STUDENT) && navController.currentDestination?.id == R.id.registerFragment) {
                if (doubleBackToExitPressedOnce) {
                    // super.onBackPressed()
                    super.onBackPressed()
                    return
                }
                this.doubleBackToExitPressedOnce = true
                Toast.makeText(this, "Please click BACK again to start over", Toast.LENGTH_SHORT).show()
                Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            } else super.onBackPressed()
        } else super.onBackPressed()

    }
}
