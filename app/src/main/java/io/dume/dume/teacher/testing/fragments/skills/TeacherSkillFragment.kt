package io.dume.dume.teacher.testing.fragments.skills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.dume.dume.R

class TeacherSkillFragment : Fragment() {
    private lateinit var teacherSkillViewModel: TeacherSkillViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        teacherSkillViewModel = ViewModelProviders.of(this).get(TeacherSkillViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teacher_skill, container, false)
        return root
    }
}