package io.dume.dume.teacher.dashboard.jobboard.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.dume.dume.teacher.dashboard.jobboard.models.JobItem
import io.dume.dume.teacher.dashboard.jobboard.repositories.JobItemRepository

class JobBoardActivityViewModel : ViewModel() {
    private val jobRepo: JobItemRepository = JobItemRepository.getInstance()

    // maintain MutableLiveData<JobItem>

    var _allJobs: MutableLiveData<List<JobItem>>? = null

    fun getAllJobs(): MutableLiveData<List<JobItem>>? {
        _allJobs = jobRepo.__getAllJobs()
        return _allJobs
    }

}