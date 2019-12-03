package io.dume.dume.teacher.dashboard.jobboard.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.dume.dume.teacher.dashboard.jobboard.models.JobItem
import io.dume.dume.teacher.dashboard.jobboard.repositories.JobItemRepository

class JobBoardActivityViewModel : ViewModel() {
    private val jobRepo: JobItemRepository = JobItemRepository.getInstance()

    // maintain MutableLiveData<JobItem>

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)

    fun getAllJobs(): LiveData<List<JobItem>> {
        isLoading.value = false
        return jobRepo.getAllJobsFromDb()
    }

}