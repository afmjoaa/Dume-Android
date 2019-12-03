package io.dume.dume.firstTimeUser.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import io.dume.dume.R
import kotlinx.android.synthetic.main.fragment_privacy.*

class PrivacyFragment : Fragment(), View.OnClickListener{

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_privacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        termsWebView.loadUrl("file:///android_asset/pages/teacher_privacy.html")
        /* if (local.getAccountManjor() == DataStore.TEACHER) {
             webViewPrivacey.loadUrl("file:///android_asset/pages/teacher_privacy.html")
             DumeUtils.configureAppbar(this, "Teacher Guide", true)
         } else {
             webViewPrivacey.loadUrl("file:///android_asset/pages/student_privacy.html")
             DumeUtils.configureAppbar(this, "Student Guide", true)
         }*/
        continueBtn.extend()
    }

    override fun onClick(v: View?) {

    }
}
