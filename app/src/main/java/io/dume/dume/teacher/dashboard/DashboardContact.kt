package io.dume.dume.teacher.dashboard

import android.view.MenuItem

interface DashboardContact {

    interface View<T> {
        fun init()
        fun initListeners()
        fun toast(message: String)
        fun onDataLoaded(t: T)
        fun error(error: String)
        fun stopRefresh()

    }

    interface Presenter {
        fun enqueue()
        fun onBottomMenuClicked(item: MenuItem)
        fun onRefresh()
    }
}