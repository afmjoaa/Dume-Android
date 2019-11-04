package io.dume.dume.teacher.dashboard

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import io.dume.dume.R
import io.dume.dume.teacher.dashboard.activities.JobBoardActivity
import io.dume.dume.teacher.dashboard.activities.MyPaymentActivity
import io.dume.dume.teacher.dashboard.activities.MySkillActivity
import io.dume.dume.teacher.dashboard.activities.MyTutionActivity

class DashboardPresenter(var context: Context, var view: DashboardContact.View) : DashboardContact.Presenter {


    override fun enqueue() {
        view.init()

    }

    override fun onBottomMenuClicked(item: MenuItem) {
        when (item.itemId) {
            R.id.my_job_board -> context.startActivity(Intent(context, JobBoardActivity::class.java))
            R.id.my_tution -> context.startActivity(Intent(context, MyTutionActivity::class.java))
            R.id.my_skill -> context.startActivity(Intent(context, MySkillActivity::class.java))
            R.id.my_payment -> context.startActivity(Intent(context, MyPaymentActivity::class.java))
        }
    }

}