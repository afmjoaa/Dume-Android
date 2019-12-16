package io.dume.dume.teacher.DashBoard.fragments.jobboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class TeacherJobBoardViewModel : ViewModel() {

    private val jobRepo: JobItemRepository = JobItemRepository.getInstance()

    // maintain MutableLiveData<JobItem>

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(true)
    var jobListLive: LiveData<List<JobItem>> = MutableLiveData()

    fun __getAllJobs(): LiveData<List<JobItem>> {
        jobListLive = jobRepo.getAllJobsFromDb()
        return jobListLive
    }
}