package io.dume.dume.firstTimeUser

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import io.dume.dume.R
import io.dume.dume.student.DashBoard.StudentDashBoard
import io.dume.dume.student.pojo.BaseAppCompatActivity
import io.dume.dume.teacher.DashBoard.TeacherDashboard
import io.dume.dume.util.DumeUtils.configAppToolBarTitle
import io.dume.dume.util.DumeUtils.configureAppbar
import io.dume.dume.util.StateManager
import kotlinx.android.synthetic.main.activity_forward_flow_host.*


class ForwardFlowHostActivity : BaseAppCompatActivity(), View.OnClickListener {
    private lateinit var viewModel: ForwardFlowViewModel
    private lateinit var navController: NavController
    private var menuState = false
    private var menu: Menu? = null

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

        if (isFirstTimeUser) {
            if (role.equals(Role.STUDENT.flow)) {
                when (studentCurrentPosition) {
                    ForwardFlowStatStudent.ROLE.flow -> navController.navigate(R.id.roleChooser)
                    ForwardFlowStatStudent.PRIVACY.flow -> navController.navigate(R.id.privacyFragment)
                    ForwardFlowStatStudent.PERMISSION.flow -> navController.navigate(R.id.permissionFragment)
                    ForwardFlowStatStudent.LOGIN.flow -> navController.navigate(R.id.loginFragment)
                    ForwardFlowStatStudent.REGISTER.flow -> navController.navigate(R.id.registerFragment)
                }
            } else if (role.equals(Role.TEACHER.flow)) {
                when (teacherCurrentPosition) {
                    ForwardFlowStatTeacher.ROLE.flow -> navController.navigate(R.id.roleChooser)
                    ForwardFlowStatTeacher.PRIVACY.flow -> navController.navigate(R.id.privacyFragment)
                    ForwardFlowStatTeacher.PERMISSION.flow -> navController.navigate(R.id.permissionFragment)
                    ForwardFlowStatTeacher.LOGIN.flow -> navController.navigate(R.id.loginFragment)
                    ForwardFlowStatTeacher.REGISTER.flow -> navController.navigate(R.id.registerFragment)
                    ForwardFlowStatTeacher.QUALIFICATION.flow -> navController.navigate(R.id.qualificationFragment)
                    ForwardFlowStatTeacher.ADDSKILL.flow -> navController.navigate(R.id.addSkillFragment)
                    ForwardFlowStatTeacher.PAYMENT.flow -> navController.navigate(R.id.paymentFragment)

                }
            }

        } else {
            if (role == Role.STUDENT.flow) startActivity(Intent(this, StudentDashBoard::class.java)) else startActivity(Intent(this, TeacherDashboard::class.java))
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
                    configAppToolBarTitle(this, "")
                    hideActionBar()
                }
                R.id.permissionFragment -> {
                    registerBtn.hide()
                    configAppToolBarTitle(this, "Provide Permission")
                    showActionBar()
                }
                R.id.privacyFragment -> {
                    registerBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Teacher Guide")

                }
                R.id.loginFragment -> {
                    registerBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Login")

                }
                R.id.nidFragment -> {
                    registerBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "NID Verification")
                }
                R.id.registerFragment -> {
                    registerBtn.show()
                    showActionBar()
                    configAppToolBarTitle(this, "Provide Info")
                }
                R.id.qualificationFragment -> {
                    registerBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Add Qualification")
                }
                R.id.addSkillFragment -> {
                    registerBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Add Skill")
                }
                R.id.postJobFragment -> {
                    registerBtn.hide()
                    showActionBar()
                    configAppToolBarTitle(this, "Post Job")
                }
                R.id.paymentFragment -> {
                    registerBtn.hide()
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
        when (v!!.id) {
            R.id.registerBtn -> {
                navController.navigate(R.id.action_registerFragment_to_qualificationFragment)
            }

        }
    }

    fun onRegisterButtonClick(listener: (view: View) -> Unit) {
        registerBtn.setOnClickListener {
            listener(it)
        }
    }
}
