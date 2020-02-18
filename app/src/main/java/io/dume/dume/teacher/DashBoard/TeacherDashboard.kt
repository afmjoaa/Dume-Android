package io.dume.dume.teacher.DashBoard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.firstTimeUser.ForwardFlowStatTeacher
import io.dume.dume.student.pojo.BaseAppCompatActivity
import io.dume.dume.teacher.DashBoard.fragments.jobboard.TeacherJobBoardFragment
import io.dume.dume.teacher.DashBoard.fragments.payments.TeacherPaymentFragment
import io.dume.dume.teacher.DashBoard.fragments.skills.TeacherSkillFragment
import io.dume.dume.teacher.DashBoard.fragments.tuitions.TeacherTuitionFragment
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.StateManager
import kotlinx.android.synthetic.main.activity_student_dash_board2.*

class TeacherDashboard : BaseAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    val jobBoardFragment: Fragment = TeacherJobBoardFragment()
    val tuitionFragment: Fragment = TeacherTuitionFragment()
    val skillFragment: Fragment = TeacherSkillFragment()
    val paymentFragment: Fragment = TeacherPaymentFragment()
    val fm: FragmentManager = supportFragmentManager
    var active = jobBoardFragment
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_dashboard)
        init()
    }


    private fun init() {
        setActivityContext(this, 1112)
        settingStatusBarTransparent()
        setDarkStatusBarIcon()
        DumeUtils.configureAppbar(this, "Job Board", true)
        initView()
        initListener()
        initLifeCycleComponents()
    }

    private fun initView() {
        bottomNavigationView = nav_view
        appBarLayout = settingsAppbar
        navigation.menu.findItem(R.id.payment).setVisible(false)
    }

    private fun initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        navigation.setNavigationItemSelectedListener(this)
    }

    private fun initLifeCycleComponents() {
        fm.beginTransaction().add(R.id.main_container, paymentFragment, "4").hide(paymentFragment).commit()
        fm.beginTransaction().add(R.id.main_container, skillFragment, "3").hide(skillFragment).commit()
        fm.beginTransaction().add(R.id.main_container, tuitionFragment, "2").hide(tuitionFragment).commit()
        fm.beginTransaction().add(R.id.main_container, jobBoardFragment, "1").commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.my_job_board -> {
                if (active != jobBoardFragment) {
                    DumeUtils.configAppToolBarTitle(context, "Job Board")
                    appBarLayout.setExpanded(true, true)
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                            .hide(active).show(jobBoardFragment).commit()
                    active = jobBoardFragment
                    return true
                }
            }

            R.id.my_tuition -> {
                if (active != tuitionFragment) {
                    DumeUtils.configAppToolBarTitle(context, "My Tuition")
                    appBarLayout.setExpanded(true, true)
                    if (active == jobBoardFragment) {
                        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                                .hide(active).show(tuitionFragment).commit()
                    } else {
                        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                                .hide(active).show(tuitionFragment).commit()
                    }
                    active = tuitionFragment
                    return true
                }
            }

            R.id.my_skill -> {
                if (active != skillFragment) {
                    DumeUtils.configAppToolBarTitle(context, "My Skill")
                    appBarLayout.setExpanded(true, true)
                    if (active != paymentFragment) {
                        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                                .hide(active).show(skillFragment).commit()
                    } else {
                        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                                .hide(active).show(skillFragment).commit()
                    }
                    active = skillFragment
                    return true
                }
            }

            R.id.my_payment -> {
                if (active != paymentFragment) {
                    DumeUtils.configAppToolBarTitle(context, "Payments")
                    appBarLayout.setExpanded(true, true)
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                            .hide(active).show(paymentFragment).commit()
                    active = paymentFragment
                    return true
                }
            }

            R.id.logOut -> {
                FirebaseAuth.getInstance().signOut()
                StateManager.getInstance(applicationContext).setTeacherCurrentState(ForwardFlowStatTeacher.LOGIN.flow)
                startActivity(Intent(applicationContext, ForwardFlowHostActivity::class.java))

            }
        }
        return false
    }
}
