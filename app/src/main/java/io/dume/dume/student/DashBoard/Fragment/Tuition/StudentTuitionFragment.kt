package io.dume.dume.student.DashBoard.Fragment.Tuition

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.dume.dume.R
import io.dume.dume.student.pojo.BaseFragment


class StudentTuitionFragment : BaseFragment() {
    private lateinit var studentTuitionViewModel: StudentTuitionViewModel


    override fun provideYourFragmentView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        studentTuitionViewModel = ViewModelProviders.of(this).get(StudentTuitionViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_student_tuition, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        studentTuitionViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

}