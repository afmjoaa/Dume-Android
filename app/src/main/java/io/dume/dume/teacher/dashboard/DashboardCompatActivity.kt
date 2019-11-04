package io.dume.dume.teacher.dashboard

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import io.dume.dume.R
import io.dume.dume.student.pojo.CustomStuAppCompatActivity

open class DashboardCompatActivity : CustomStuAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toolbar: Toolbar = this.findViewById(R.id.toolbar)
        val drawer: DrawerLayout = this.findViewById(R.id.drawer_layout)
        toolbar.setNavigationOnClickListener { drawer.openDrawer(GravityCompat.START) }

    }
}
