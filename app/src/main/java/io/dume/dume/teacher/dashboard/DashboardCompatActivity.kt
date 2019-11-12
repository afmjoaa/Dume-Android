package io.dume.dume.teacher.dashboard

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.otaliastudios.cameraview.controls.Flash
import com.otaliastudios.cameraview.controls.Hdr
import io.dume.dume.R
import io.dume.dume.student.pojo.CustomStuAppCompatActivity

open class DashboardCompatActivity : CustomStuAppCompatActivity() {
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
