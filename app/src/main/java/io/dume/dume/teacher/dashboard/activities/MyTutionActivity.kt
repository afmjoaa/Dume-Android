package io.dume.dume.teacher.dashboard.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.student.pojo.CustomStuAppCompatActivity
import io.dume.dume.student.recordsPage.Record
import io.dume.dume.teacher.dashboard.DashboardCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import io.dume.dume.teacher.dashboard.adapters.TutionAdapter
import io.dume.dume.util.DumeUtils
import kotlinx.android.synthetic.main.activity_my_tution.*

class MyTutionActivity : CustomStuAppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, DashboardContact.View<List<Record>>, SwipeRefreshLayout.OnRefreshListener {


    private val presenter = DashboardPresenter(this, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tution)
        setActivityContext(this, 1112)
        DumeUtils.configureAppbar(this, "My Tuition", true)
        presenter.enqueue()

    }

    override fun init() {
        bottom_menu.selectedItemId = R.id.my_tution
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(this, R.color.mColorPrimaryVariant))
        presenter.getTutions()

    }

    override fun initListeners() {
        bottom_menu.setOnNavigationItemSelectedListener(this)
        swipe_to_refres.setOnRefreshListener(this)
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
        bottom_menu.selectedItemId = R.id.my_tution
        bottom_menu.setOnNavigationItemSelectedListener(this)

    }

    override fun onDataLoaded(t: List<Record>) {
        tution_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        tution_rv.adapter = TutionAdapter(t, presenter)
    }

    override fun error(error: String) {
        toast(message = error)
    }

    override fun onRefresh() {
        presenter.getTutions()
        stopRefresh()
    }

    override fun stopRefresh() {
        swipe_to_refres.isRefreshing = false
    }


}
