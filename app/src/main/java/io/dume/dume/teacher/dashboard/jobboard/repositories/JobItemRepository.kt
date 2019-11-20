package io.dume.dume.teacher.dashboard.jobboard.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import io.dume.dume.teacher.dashboard.jobboard.models.JobInfo
import io.dume.dume.teacher.dashboard.jobboard.models.JobItem
import io.dume.dume.teacher.dashboard.jobboard.models.RecordInfo

@Suppress("UNCHECKED_CAST")
class JobItemRepository private constructor() {

    companion object {
        private var _instance: JobItemRepository? = null
        fun getInstance(): JobItemRepository {
            if (_instance == null) _instance = JobItemRepository()
            return _instance!!
        }
    }

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val allJobs: MutableLiveData<List<JobItem>> = MutableLiveData()

    fun __getAllJobs(): LiveData<List<JobItem>> {

        db.collection("jobs").get().addOnSuccessListener { querySnapshot ->
            val ret = mutableListOf<JobItem>()
            if (!querySnapshot.isEmpty) {
                for (doc in querySnapshot) {
                    val data: MutableMap<String, Any> = doc.data
                    val job_info: Map<String, Any> = data["job_info"] as Map<String, Any>
                    val name: String = job_info["job_title"] as String
                    Log.d("JobItemRepository: ", name)
                    ret.add(JobItem("", JobInfo(name, "", ""), RecordInfo(null, null, null)))
                }
            }
            allJobs.value = ret
        }
//        ret.add(JobItem("", JobInfo("manually added", "", ""), RecordInfo(null, null, null)))
        return allJobs
    }

}