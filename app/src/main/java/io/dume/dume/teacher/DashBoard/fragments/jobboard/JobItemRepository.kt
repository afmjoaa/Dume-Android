package io.dume.dume.teacher.DashBoard.fragments.jobboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class JobItemRepository private constructor() {

    companion object {
        private var _instance: JobItemRepository? = null
        fun getInstance(): JobItemRepository {
            if (_instance == null) _instance = JobItemRepository()
            return _instance!!
        }
    }

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val allJobs: MutableLiveData<List<JobItem>> = MutableLiveData()

    fun getAllJobsFromDb(): LiveData<List<JobItem>> {
        db.collection("jobs").get().addOnSuccessListener { querySnapshot ->
            val ret = mutableListOf<JobItem>()
            if (!querySnapshot.isEmpty) {
                for (doc in querySnapshot) {
                    val obj = doc.toObject(JobItem::class.java)

                    /*val data: MutableMap<String, Any> = doc.data
                    val title = data["title"] as String
                    val desc = data["description"] as String
                    val toAdd = JobItem()
                    toAdd.title = title
                    toAdd.description = desc*/

                    ret.add(obj)
                }
            }
            allJobs.value = ret
        }
        return allJobs
    }

    fun addNewJobToDb(jobToAdd: JobItem) {
        val add = db.collection("jobs").add(jobToAdd)
        add.addOnSuccessListener {
            //it.id
        }
    }


}