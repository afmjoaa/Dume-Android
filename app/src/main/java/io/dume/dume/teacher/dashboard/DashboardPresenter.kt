package io.dume.dume.teacher.dashboard

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import io.dume.dume.R
import io.dume.dume.student.recordsPage.Record
import io.dume.dume.teacher.dashboard.activities.JobBoardActivity
import io.dume.dume.teacher.dashboard.activities.MyPaymentActivity
import io.dume.dume.teacher.dashboard.activities.MySkillActivity
import io.dume.dume.teacher.dashboard.activities.MyTutionActivity
import io.dume.dume.teacher.homepage.TeacherContract

class DashboardPresenter<T>(var context: Context, var view: DashboardContact.View<T>) : DashboardContact.Presenter {


    private val model = DashboardModel(context)

    override fun enqueue() {
        view.init()
        view.initListeners()

    }

    override fun onBottomMenuClicked(item: MenuItem) {
        when (item.itemId) {
            R.id.my_job_board -> context.startActivity(Intent(context, JobBoardActivity::class.java))
            R.id.my_tution -> context.startActivity(Intent(context, MyTutionActivity::class.java))
            R.id.my_skill -> context.startActivity(Intent(context, MySkillActivity::class.java))
            R.id.my_payment -> context.startActivity(Intent(context, MyPaymentActivity::class.java))
        }
    }

    override fun onRefresh() {

    }

    fun getTutions() {
        view.toast("getTutions")

        model.getRecords(object : TeacherContract.Model.Listener<List<Record>> {
            override fun onSuccess(list: List<Record>?) {
                if (list != null) {
                    view.onDataLoaded(t = list as T)
                }

            }

            override fun onError(msg: String?) {
                if (msg != null) {
                    view.error(msg)
                }else {
                    view.toast("Msg is null")

                }

            }
        })
    }


}