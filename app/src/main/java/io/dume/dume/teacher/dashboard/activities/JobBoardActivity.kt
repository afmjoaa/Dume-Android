package io.dume.dume.teacher.dashboard.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import carbon.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.student.pojo.CustomStuAppCompatActivity
import io.dume.dume.teacher.dashboard.DashboardCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import io.dume.dume.teacher.dashboard.adapters.CirclePagerIndicatorDecoration
import io.dume.dume.teacher.dashboard.adapters.FeatureCardSlider
import io.dume.dume.teacher.dashboard.pojo.JobsItem
import io.dume.dume.util.DumeUtils.configureAppbar
import kotlinx.android.synthetic.main.activity_job_board.*

class JobBoardActivity : CustomStuAppCompatActivity(), DashboardContact.View<List<JobsItem>>, BottomNavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {


    private val presenter = DashboardPresenter(this, this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_board)
        setActivityContext(this, 1112)
        configureAppbar(this, "Job Board", true)
        presenter.enqueue()
    }

    fun initJobsReciclyerView() {

        lateinit var recyclerView: androidx.recyclerview.widget.RecyclerView
        lateinit var viewAdapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>
        lateinit var viewManager: androidx.recyclerview.widget.RecyclerView.LayoutManager

        viewManager = LinearLayoutManager(this)
        //viewAdapter = MyAdapter(myDataset)

        recyclerView = findViewById<RecyclerView>(R.id.job_card_rv).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }


    }

    override fun init() {
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(this, R.color.mColorPrimaryVariant))
        bottom_menu.selectedItemId = R.id.my_job_board
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
