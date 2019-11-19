package io.dume.dume.student.DashBoard

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.student.DashBoard.Fragment.JobBoard.StudentJobBoardFragment
import io.dume.dume.student.DashBoard.Fragment.SearchMentor.SearchMentorFragment
import io.dume.dume.student.DashBoard.Fragment.Tuition.StudentTuitionFragment
import androidx.annotation.NonNull
import io.dume.dume.student.pojo.CustomStuAppCompatActivity
import io.dume.dume.util.DumeUtils
import io.dume.dume.util.DumeUtils.configAppToolBarTitle


class StudentDashBoard : CustomStuAppCompatActivity() {

    val searchMentorFragment: Fragment = SearchMentorFragment()
    val jobBoardFragment: Fragment = StudentJobBoardFragment()
    val tuitionFragment: Fragment = StudentTuitionFragment()
    val fm: FragmentManager = supportFragmentManager
    var active = searchMentorFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dash_board2)
        setActivityContext(this, 1112)
        settingStatusBarTransparent()
        setDarkStatusBarIcon()
        DumeUtils.configureAppbar(this, "Search Mentor", true)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fm.beginTransaction().add(R.id.main_container, tuitionFragment, "3").hide(tuitionFragment).commit()
        fm.beginTransaction().add(R.id.main_container, jobBoardFragment, "2").hide(jobBoardFragment).commit()
        fm.beginTransaction().add(R.id.main_container, searchMentorFragment, "1").commit()
    }



    private val mOnNavigationItemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.search_mentor -> {
                    configAppToolBarTitle(context, "")
                    fm.beginTransaction().hide(active).show(searchMentorFragment).commit()
                    active = searchMentorFragment
                    return true
                }

                R.id.job_board -> {
                    configAppToolBarTitle(context, "Job Board")
                    fm.beginTransaction().hide(active).show(jobBoardFragment).commit()
                    active = jobBoardFragment
                    return true
                }

                R.id.my_tuition -> {
                    configAppToolBarTitle(context, "My Tuition")
                    fm.beginTransaction().hide(active).show(tuitionFragment).commit()
                    active = tuitionFragment
                    return true
                }
            }
            return false
        }
    }
}
