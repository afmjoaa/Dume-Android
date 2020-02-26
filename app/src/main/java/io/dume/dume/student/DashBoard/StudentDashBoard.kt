package io.dume.dume.student.DashBoard

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
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

class StudentDashBoard : BaseAppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationView.OnNavigationItemSelectedListener {

    private val searchMentorFragment: Fragment = SearchMentorFragment()
    private val jobBoardFragment: Fragment = StudentJobBoardFragment()
    private val tuitionFragment: Fragment = StudentTuitionFragment()
    val fm: FragmentManager = supportFragmentManager
    var active = searchMentorFragment
    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_dash_board2)
        init()
        checkForAppUpdate()
    }

    private fun init() {
        setActivityContext(this, 1112)

        initView()
        initListener()
        initLifeCycleComponents()
    }

    private fun initView() {
        settingStatusBarTransparent()
        setDarkStatusBarIcon()
        DumeUtils.configureAppbar(this, "Search Mentor", true)
    }

    private fun initListener() {
        nav_view.setOnNavigationItemSelectedListener(this)
        navigation.setNavigationItemSelectedListener(this)
    }

    private fun initLifeCycleComponents() {
        fm.beginTransaction().add(R.id.main_container, tuitionFragment, "3").hide(tuitionFragment).commit()
        fm.beginTransaction().add(R.id.main_container, jobBoardFragment, "2").hide(jobBoardFragment).commit()
        fm.beginTransaction().add(R.id.main_container, searchMentorFragment, "1").commit()
    }

    override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search_mentor -> {
                if (active != searchMentorFragment) {
                    configAppToolBarTitle(context, "Search Mentor")
                    settingsAppbar.setExpanded(true, true)
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left)
                            .hide(active).show(searchMentorFragment).commit()
                    active = searchMentorFragment
                    return true
                }
            }

            R.id.job_board -> {
                if (active != jobBoardFragment) {
                    configAppToolBarTitle(context, "Job Board")
                    settingsAppbar.setExpanded(true, true)
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
                    settingsAppbar.setExpanded(true, true)
                    fm.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                            .hide(active).show(tuitionFragment).commit()
                    active = tuitionFragment
                    return true
                }
            }

            R.id.logOut -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, ForwardFlowHostActivity::class.java))
            }
        }
        return false

    }

    private fun checkForAppUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                try {
                    val installType = when {
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> AppUpdateType.FLEXIBLE
                        appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> AppUpdateType.IMMEDIATE
                        else -> null
                    }
                    if (installType == AppUpdateType.FLEXIBLE) appUpdateManager.registerListener(appUpdatedListener)

                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            installType!!,
                            this,
                            APP_UPDATE_REQUEST_CODE)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(appUpdatedListener)
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->

            // If the update is downloaded but not installed, notify the user to complete the update.
            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
            }

            try {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            APP_UPDATE_REQUEST_CODE)
                }
            } catch (e: IntentSender.SendIntentException) {
                Log.e("TeacherDashboard", "IMMEDIATE update fail: " +  e.printStackTrace())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == APP_UPDATE_REQUEST_CODE) {
            if (resultCode != Activity.RESULT_OK) {
                Toast.makeText(this, "App Update failed, please try again on the next app launch.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(
                findViewById(R.id.parent_coor_layout),
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE).apply {
            setAction("INSTALL") { appUpdateManager.completeUpdate() }
            setActionTextColor(ContextCompat.getColor(this@StudentDashBoard, R.color.mColorPrimary))
            show()
        }
    }

    private fun popupSnackbarForRetryUpdate() {
        Snackbar.make(
                findViewById(R.id.parent_coor_layout),
                "Unable to download update.",
                Snackbar.LENGTH_LONG).apply {
            setAction("RETRY") { checkForAppUpdate() }
            setActionTextColor(ContextCompat.getColor(this@StudentDashBoard, R.color.mColorPrimary))
            show()
        }
    }

    private val appUpdatedListener: InstallStateUpdatedListener by lazy {
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(installState: InstallState) {
                when {
                    installState.installStatus() == InstallStatus.DOWNLOADED -> popupSnackbarForCompleteUpdate()
                    installState.installStatus() == InstallStatus.INSTALLED -> appUpdateManager.unregisterListener(this)
                    installState.installStatus() == InstallStatus.FAILED -> popupSnackbarForRetryUpdate()
                    else -> Log.d("TeacherDashboard", "InstallStateUpdatedListener: state: ${installState.installStatus()}")
                }
            }
        }
    }

    companion object {
        private const val APP_UPDATE_REQUEST_CODE = 1234
    }
}
