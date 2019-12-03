package io.dume.dume.student.DashBoard.Fragment.JobBoard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.dume.dume.R
import io.dume.dume.student.pojo.BaseFragment

class StudentJobBoardFragment : Fragment() {
    private lateinit var dashboardViewModelStudent: StudentJobBoardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dashboardViewModelStudent = ViewModelProviders.of(this).get(StudentJobBoardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_student_job_board, container, false)
        return root
    }
}