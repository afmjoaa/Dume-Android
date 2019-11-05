package io.dume.dume.teacher.dashboard.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.teacher.dashboard.DashboardCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import kotlinx.android.synthetic.main.activity_job_board.*

class JobBoardActivity : DashboardCompatActivity(), DashboardContact.View, BottomNavigationView.OnNavigationItemSelectedListener {


    private val presenter = DashboardPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_job_board)
        super.onCreate(savedInstanceState)
        presenter.enqueue()
    }

    override fun init() {
        setDarkStatusBarIcon()
        bottom_menu.setOnNavigationItemSelectedListener(this)
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(this, R.color.mColorPrimaryVariant))
        bottom_menu.selectedItemId = R.id.my_job_board


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
        bottom_menu.selectedItemId = R.id.my_job_board
        bottom_menu.setOnNavigationItemSelectedListener(this)
    }

}
