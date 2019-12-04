package io.dume.dume.teacher.testing.fragments.jobboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.dume.dume.R

class TeacherJobBoardFragment : Fragment() {

    private lateinit var teacherJobBoardViewModel: TeacherJobBoardViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        teacherJobBoardViewModel = ViewModelProviders.of(this).get(TeacherJobBoardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teacher_job_board, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        teacherJobBoardViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}