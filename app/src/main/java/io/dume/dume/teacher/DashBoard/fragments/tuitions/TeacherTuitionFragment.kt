package io.dume.dume.teacher.DashBoard.fragments.tuitions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.dume.dume.R


class TeacherTuitionFragment : Fragment() {

    private lateinit var teacherTuitionViewModel: TeacherTuitionViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        teacherTuitionViewModel = ViewModelProviders.of(this).get(TeacherTuitionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teacher_tuition, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        teacherTuitionViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}