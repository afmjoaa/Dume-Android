package io.dume.dume.student.DashBoard

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import io.dume.dume.R
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.student.DashBoard.Fragment.JobBoard.StudentJobBoardFragment
import io.dume.dume.student.DashBoard.Fragment.SearchMentor.SearchMentorFragment
import io.dume.dume.student.DashBoard.Fragment.Tuition.StudentTuitionFragment
import io.dume.dume.student.pojo.BaseAppCompatActivity
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.DumeUtils.configAppToolBarTitle
import kotlinx.android.synthetic.main.activity_student_dash_board2.*


class StudentDashBoard : BaseAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val searchMentorFragment: Fragment = SearchMentorFragment()
    val jobBoardFragment: Fragment = StudentJobBoardFragment()
    val tuitionFragment: Fragment = StudentTuitionFragment()
    val fm: FragmentManager = supportFragmentManager
    var active = searchMentorFragment
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sideNavigationView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dash_board2)
        init()
    }

    private fun init() {
        setActivityContext(this, 1112)
        settingStatusBarTransparent()
        setDarkStatusBarIcon()
        DumeUtils.configureAppbar(this, "Search Mentor", true)
        initView()
        initListener()
        initLifeCycleComponents()
    }

    private fun initView() {
        bottomNavigationView = nav_view
        appBarLayout = settingsAppbar
    }

    private fun initListener() {
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun initLifeCycleComponents() {
        fm.beginTransaction().add(R.id.main_container, tuitionFragment, "3").hide(tuitionFragment).commit()
        fm.beginTransaction().add(R.id.main_container, jobBoardFragment, "2").hide(jobBoardFragment).commit()
        fm.beginTransaction().add(R.id.main_container, searchMentorFragment, "1").commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logOut -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, ForwardFlowHostActivity::class.java))
            }
        }
        return true
    }

    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.search_mentor -> {
                    if (active != searchMentorFragment) {
                        configAppToolBarTitle(context, "Search Mentor")
                        appBarLayout.setExpanded(true, true)
                        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                                .hide(active).show(searchMentorFragment).commit()
                        active = searchMentorFragment
                        return true
                    }
                }

                R.id.job_board -> {
                    if (active != jobBoardFragment) {
                        configAppToolBarTitle(context, "Job Board")
                        appBarLayout.setExpanded(true, true)
                        if (active == searchMentorFragment) {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                                    .hide(active).show(jobBoardFragment).commit()
                        } else {
                            fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                                    .hide(active).show(jobBoardFragment).commit()
                        }
                        active = jobBoardFragment
                        return true
                    }
                }

                R.id.my_tuition -> {
                    if (active != tuitionFragment) {
                        configAppToolBarTitle(context, "My Tuition")
                        appBarLayout.setExpanded(true, true)
                        fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                                .hide(active).show(tuitionFragment).commit()
                        active = tuitionFragment
                        return true
                    }
                }
            }
            return false
        }
    }
}
