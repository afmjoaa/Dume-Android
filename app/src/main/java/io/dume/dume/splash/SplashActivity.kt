package io.dume.dume.splash

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.dume.dume.auth.AuthModel
import io.dume.dume.firstTimeUser.ForwardFlowHostActivity
import io.dume.dume.firstTimeUser.RoleChooserActivity
import io.dume.dume.foreignObligation.PayActivity
import io.dume.dume.teacher.homepage.TeacherContract
import io.dume.dume.util.DumeUtils

class SplashActivity : AppCompatActivity(), SplashContract.View {
    lateinit var presenter: SplashContract.Presenter
    private var prefs: SharedPreferences? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SplashPresenter(this, AuthModel(this, this))
        prefs = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
        presenter.enqueue(this)
        presenter.init(this)
    }

    override fun foundUpdates() {
        DumeUtils.notifyDialog(this, false, false, "Mandatory Update !!", updateDescription, "Update", object : TeacherContract.Model.Listener<Boolean?> {
            override fun onSuccess(yes: Boolean?) {
                if (yes!!) {
                    val appPackageName = packageName
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
                    } catch (anfe: ActivityNotFoundException) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
                    }
                } else {
                    Toast.makeText(this@SplashActivity, "Update Ignored", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(msg: String) {
                Log.w(TAG, "onError: $msg")
            }
        })
    }

    override fun foundErr(msg: String) {
        DumeUtils.notifyDialog(this, true, false, "No Internet or Error...", msg, "Retry", object : TeacherContract.Model.Listener<Boolean?> {
            override fun onSuccess(yes: Boolean?) { //already handled
                presenter!!.enqueue(applicationContext)
            }

            override fun onError(msg: String) { //already handled
                Toast.makeText(this@SplashActivity, "Something went wrong...", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun gotoLoginActivity() {
        val isShown = prefs!!.getBoolean("isShown", false)
        if (isShown) {
            startActivity(Intent(this, RoleChooserActivity::class.java))
            finish()
        } else {
            startActivity(Intent(this, RoleChooserActivity::class.java))
            finish()
        }
    }

    override fun gotoForwardFlowActivity(){
        startActivity(Intent(this, RoleChooserActivity::class.java))
        finish()
    }

    override fun gotoTeacherActivity() {
        gotoLoginActivity()
        finish()
    }

    override fun gotoStudentActivity() {
        gotoLoginActivity()
        finish()
    }

    override fun gotoForeignObligation() {
        startActivity(Intent(this, PayActivity::class.java))
        finish()
    }

    companion object {
        private const val MY_PREFS_NAME = "welcome"
        private const val TAG = "SplashActivity"
        @JvmField
        var updateDescription = ""
        @JvmField
        var updateVersionName = ""

    }
}