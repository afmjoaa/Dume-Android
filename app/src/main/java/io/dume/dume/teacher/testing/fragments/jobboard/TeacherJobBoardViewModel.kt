package io.dume.dume.teacher.testing.fragments.jobboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class TeacherJobBoardViewModel : ViewModel() {

    private val jobRepo: JobItemRepository = JobItemRepository.getInstance()

    // maintain MutableLiveData<JobItem>

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getAllJobs(): LiveData<List<JobItem>> {
        isLoading.value = false
        return jobRepo.getAllJobsFromDb()
    }
}