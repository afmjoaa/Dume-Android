package io.dume.dume.teacher.dashboard

import android.os.Bundle
import io.dume.dume.student.pojo.BaseAppCompatActivity

open class DashboardCompatActivity : BaseAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityContext(this, 7654)
        //val toolbar: Toolbar = this.findViewById(R.id.accountToolbar)
        //val drawer: DrawerLayout = this.findViewById(R.id.drawer_layout)
        //toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }
        settingStatusBarTransparent()
        setDarkStatusBarIcon()

    }


}
