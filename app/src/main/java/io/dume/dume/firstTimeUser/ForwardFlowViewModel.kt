package io.dume.dume.firstTimeUser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ForwardFlowViewModel : ViewModel() {
    val role = MutableLiveData<Role>()
    val firstTimeUser = MutableLiveData<Boolean>()
    val studentCurrentPosition = MutableLiveData<ForwardFlowStatStudent>()
    val teacherCurrentPosition = MutableLiveData<ForwardFlowStatTeacher>()

    fun updateRole(roleOne: Role) = role.postValue(roleOne)
    fun updateFirstTimeUser(status: Boolean) = firstTimeUser.postValue(status)
    fun updateStudentCurrentPosition(forwardFlowStatStudent: ForwardFlowStatStudent) =
            studentCurrentPosition.postValue(forwardFlowStatStudent)

    fun updateTeacherCurrentPosition(forwardFlowStatTeacher: ForwardFlowStatTeacher) =
            teacherCurrentPosition.postValue(forwardFlowStatTeacher)
}