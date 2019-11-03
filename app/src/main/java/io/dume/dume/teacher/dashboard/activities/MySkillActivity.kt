package io.dume.dume.teacher.dashboard.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.teacher.dashboard.DashboardCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import kotlinx.android.synthetic.main.activity_my_skill.*

class MySkillActivity : DashboardCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, DashboardContact.View  {
    private val presenter = DashboardPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_my_skill)
        super.onCreate(savedInstanceState)
        presenter.enqueue()

    }
    override fun init() {
        setDarkStatusBarIcon()
        bottom_menu.setOnNavigationItemSelectedListener(this)
        bottom_menu.selectedItemId = R.id.my_skill
    }

    override fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        presenter.onBottomMenuClicked(item = item)
        return true
    }
    override fun onResume() {
        super.onResume()
        bottom_menu.setOnNavigationItemSelectedListener(null)
        bottom_menu.selectedItemId = R.id.my_skill
        bottom_menu.setOnNavigationItemSelectedListener(this)
    }
}
