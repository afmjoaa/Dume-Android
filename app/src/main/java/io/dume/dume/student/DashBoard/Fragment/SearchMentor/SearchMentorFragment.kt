package io.dume.dume.student.DashBoard.Fragment.SearchMentor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import io.dume.dume.R
import io.dume.dume.student.pojo.BaseFragment


class SearchMentorFragment : BaseFragment() {
    private lateinit var searchMentorViewModel: SearchMentorViewModel

    override fun provideYourFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        searchMentorViewModel = ViewModelProviders.of(this).get(SearchMentorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_search_mentor, container, false)

        return root
    }
}