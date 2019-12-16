package io.dume.dume.teacher.DashBoard.fragments.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import io.dume.dume.R

class TeacherPaymentFragment : Fragment() {

    private lateinit var teacherPaymentViewModel: TeacherPaymentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        teacherPaymentViewModel = ViewModelProviders.of(this).get(TeacherPaymentViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_teacher_payment, container, false)
        val textView: TextView = root.findViewById(R.id.text_notifications)
        teacherPaymentViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

}