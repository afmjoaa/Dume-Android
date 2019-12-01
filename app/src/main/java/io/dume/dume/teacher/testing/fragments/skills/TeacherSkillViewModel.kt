package io.dume.dume.teacher.testing.fragments.skills

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TeacherSkillViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is skill Fragment"
    }
    val text: LiveData<String> = _text
}