package io.dume.dume.teacher.DashBoard.fragments.tuitions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TeacherTuitionViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is tuition Fragment"
    }
    val text: LiveData<String> = _text
}