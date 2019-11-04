package io.dume.dume.teacher.dashboard

import android.view.MenuItem

interface DashboardContact {

    interface View {
        fun init()
        fun toast(message: String)
        fun setupRecycler()
    }

    interface Presenter {
        fun enqueue()
        fun onBottomMenuClicked(item: MenuItem)
    }
}