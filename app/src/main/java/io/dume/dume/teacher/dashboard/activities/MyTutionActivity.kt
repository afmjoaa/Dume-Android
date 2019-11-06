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
import io.dume.dume.student.recordsPage.Record
import io.dume.dume.teacher.dashboard.DashboardCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import io.dume.dume.teacher.dashboard.adapters.FeatureCardSlider
import kotlinx.android.synthetic.main.activity_my_tution.*

class MyTutionActivity : DashboardCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, DashboardContact.View<List<Record>>, SwipeRefreshLayout.OnRefreshListener {


    private val presenter = DashboardPresenter(this, this)

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_my_tution)
        super.onCreate(savedInstanceState)
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
        toast("OnDataLoaded")
        tution_rv.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        tution_rv.adapter = FeatureCardSlider(t)
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
