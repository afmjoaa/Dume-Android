package io.dume.dume.teacher.dashboard.activities

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.dume.dume.R
import io.dume.dume.student.studentPayment.adapterAndData.PaymentData
import io.dume.dume.teacher.dashboard.DashboardCompatActivity
import io.dume.dume.teacher.dashboard.DashboardContact
import io.dume.dume.teacher.dashboard.DashboardPresenter
import kotlinx.android.synthetic.main.activity_my_payment.*


/**
 * DashboardContact.View<PaymentData>
 *     here PaymentData is the main data of that activity
 */
class MyPaymentActivity : DashboardCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener, DashboardContact.View<PaymentData>, SwipeRefreshLayout.OnRefreshListener {


    private val presenter = DashboardPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_my_payment)
        super.onCreate(savedInstanceState)
        presenter.enqueue()

    }

    override fun init() {
        swipe_to_refres.setColorSchemeColors(ContextCompat.getColor(this, R.color.mColorPrimaryVariant))
        bottom_menu.setOnNavigationItemSelectedListener(this)
        bottom_menu.selectedItemId = R.id.my_payment
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
        bottom_menu.selectedItemId = R.id.my_payment
        bottom_menu.setOnNavigationItemSelectedListener(this)
    }

    override fun onDataLoaded(t: PaymentData) {

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
