package io.dume.dume.teacher.dashboard

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import io.dume.dume.R
import io.dume.dume.student.recordsAccepted.RecordsAcceptedActivity
import io.dume.dume.student.recordsCompleted.RecordsCompletedActivity
import io.dume.dume.student.recordsCurrent.RecordsCurrentActivity
import io.dume.dume.student.recordsPage.Record
import io.dume.dume.student.recordsPending.RecordsPendingActivity
import io.dume.dume.student.recordsRejected.RecordsRejectedActivity
import io.dume.dume.teacher.dashboard.jobboard.JobBoardActivity
import io.dume.dume.teacher.dashboard.activities.MyPaymentActivity
import io.dume.dume.teacher.dashboard.activities.MySkillActivity
import io.dume.dume.teacher.dashboard.activities.MyTutionActivity
import io.dume.dume.teacher.dashboard.adapters.TutionAdapter
import io.dume.dume.teacher.homepage.TeacherContract
import io.dume.dume.util.DumeUtils

class DashboardPresenter<T>(var context: Context, var view: DashboardContact.View<T>) : DashboardContact.Presenter, TutionAdapter.RecordClickListener {


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
        model.getRecords(object : TeacherContract.Model.Listener<List<Record>> {
            override fun onSuccess(list: List<Record>?) {
                if (list != null) {
                    view.onDataLoaded(t = list as T)
                }

            }

            override fun onError(msg: String?) {
                if (msg != null) {
                    view.error(msg)
                } else {
                    view.toast("Msg is null")

                }

            }
        })
    }

    override fun onRecordClickListener(position: Int, record: Record) {
        var intent = Intent(context, RecordsRejectedActivity::class.java).setAction(DumeUtils.TEACHER)
        when (record.status) {
            Record.PENDING -> intent = Intent(context, RecordsPendingActivity::class.java).setAction(DumeUtils.TEACHER)

            Record.ACCEPTED -> intent = Intent(context, RecordsAcceptedActivity::class.java).setAction(DumeUtils.TEACHER)

            Record.CURRENT -> intent = Intent(context, RecordsCurrentActivity::class.java).setAction(DumeUtils.TEACHER)

            Record.REJECTED -> intent = Intent(context, RecordsRejectedActivity::class.java).setAction(DumeUtils.TEACHER)

            Record.COMPLETED -> intent = Intent(context, RecordsCompletedActivity::class.java).setAction(DumeUtils.TEACHER)

            else -> view.toast("Unhandled error in line 81")
        }
        intent.putExtra("recordId", record.recordSnap.id)
        context.startActivity(intent)

    }

}