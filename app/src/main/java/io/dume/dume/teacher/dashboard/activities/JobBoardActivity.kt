package io.dume.dume.teacher.dashboard.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import carbon.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.teacher.dashboard.DashboardCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import io.dume.dume.teacher.dashboard.adapters.CirclePagerIndicatorDecoration
import io.dume.dume.teacher.dashboard.adapters.FeatureCardSlider
import io.dume.dume.teacher.dashboard.pojo.JobsItem
import kotlinx.android.synthetic.main.activity_job_board.*

class JobBoardActivity : DashboardCompatActivity(), DashboardContact.View<List<JobsItem>>, BottomNavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {


    private val presenter = DashboardPresenter(this, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_job_board)
        super.onCreate(savedInstanceState)
        presenter.enqueue()
    }

    override fun init() {
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(this, R.color.mColorPrimaryVariant))
        bottom_menu.selectedItemId = R.id.my_job_board
        navigation.menu.getItem(0).isChecked = true
        job_card_rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        job_card_rv.addItemDecoration(CirclePagerIndicatorDecoration())
        job_card_rv.adapter = FeatureCardSlider()

        /*    var cardSnapHelper = CardSnapHelper()
            cardSnapHelper.attachToRecyclerView(job_card_rv)*/

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
        bottom_menu.selectedItemId = R.id.my_job_board
        bottom_menu.setOnNavigationItemSelectedListener(this)
    }

    override fun onDataLoaded(t: List<JobsItem>) {

        /*  job_card_rv.layoutManager = CardSliderLayoutManager(context)
          job_card_rv.adapter = FeatureCardSlider()
          CardSnapHelper().attachToRecyclerView(job_card_rv)*/
/*        job_card_rv.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        job_card_rv.adapter = FeatureCardSlider()*/
    }

    override fun error(error: String) {
    }

    /**
     * Called when a swipe gesture triggers a refresh.
     */
    override fun onRefresh() {
        presenter.onRefresh()
    }

    override fun stopRefresh() {
        swipe_to_refres.isRefreshing = false
    }
}
