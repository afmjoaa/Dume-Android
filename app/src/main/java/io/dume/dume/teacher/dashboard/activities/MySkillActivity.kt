package io.dume.dume.teacher.dashboard.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.student.pojo.BaseAppCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import io.dume.dume.teacher.pojo.Skill
import io.dume.dume.util.DumeUtils
import kotlinx.android.synthetic.main.activity_my_skill.*

class MySkillActivity : BaseAppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, DashboardContact.View<List<Skill>>, SwipeRefreshLayout.OnRefreshListener {


    private val presenter = DashboardPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_skill)
        setActivityContext(this, 1112)
        DumeUtils.configureAppbar(this, "My Skill", true)
        presenter.enqueue()

    }

    override fun init() {
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(this, R.color.mColorPrimaryVariant))
        bottom_menu.selectedItemId = R.id.my_skill
    }
    override fun initListeners() {
        bottom_menu.setOnNavigationItemSelectedListener(this)

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

    override fun onDataLoaded(t: List<Skill>) {
    }

    override fun error(error: String) {

    }

    override fun onRefresh() {
        presenter.onRefresh()
    }
    override fun stopRefresh() {
        swipe_to_refres.isRefreshing = false
    }
}
