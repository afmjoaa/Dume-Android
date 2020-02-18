package io.dume.dume.student.DashBoard.Fragment.JobBoard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.dume.dume.teacher.DashBoard.fragments.jobboard.JobItem
import io.dume.dume.teacher.DashBoard.fragments.jobboard.JobItemRepository

class StudentJobBoardViewModel : ViewModel() {

    private val jobRepo: JobItemRepository = JobItemRepository.getInstance()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var jobListLive: LiveData<List<JobItem>> = MutableLiveData()

    fun fetchJobs(): LiveData<List<JobItem>> {
        jobListLive = jobRepo.getAllJobsFromDb()
        return jobListLive
    }
}