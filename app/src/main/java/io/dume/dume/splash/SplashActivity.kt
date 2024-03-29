package io.dume.dume.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.dume.dume.TestActivity
import io.dume.dume.firstTimeUser.AuthRepository
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.commonActivity.foreignObligation.PayActivity
import io.dume.dume.student.DashBoard.StudentDashBoard
import io.dume.dume.teacher.DashBoard.TeacherDashboard
import io.dume.dume.teacher.homepage.TeacherContract
import io.dume.dume.util.DumeUtils

class SplashActivity : AppCompatActivity(), SplashContract.View {
    lateinit var presenter: SplashContract.Presenter

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        presenter = SplashPresenter(this, AuthRepository(this, this))
        presenter.enqueue(this)
    }

    private fun initView() {}

    override fun foundErr(msg: String) {
        DumeUtils.notifyDialog(this, true, false, "No Internet or Error...", msg, "Retry", object : TeacherContract.Model.Listener<Boolean?> {
            override fun onSuccess(yes: Boolean?) { //already handled
                presenter.enqueue(applicationContext)
            }

            override fun onError(msg: String) { //already handled
                Toast.makeText(this@SplashActivity, "Something went wrong...", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun gotoLoginActivity() {
        startActivity(Intent(this, TestActivity::class.java))
        finish()
    }

    override fun gotoForwardFlowActivity() {
        startActivity(Intent(this, ForwardFlowHostActivity::class.java))
        finish()
    }

    override fun gotoTeacherActivity() {
        startActivity(Intent(this, TeacherDashboard::class.java))
        finish()
    }

    override fun gotoStudentActivity() {
        startActivity(Intent(this, StudentDashBoard::class.java))
        finish()
    }

    override fun gotoForeignObligation() {
        startActivity(Intent(this, PayActivity::class.java))
        finish()
    }
}