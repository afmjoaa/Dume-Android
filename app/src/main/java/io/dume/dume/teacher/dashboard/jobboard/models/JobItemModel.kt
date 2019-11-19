package io.dume.dume.teacher.dashboard.jobboard.models

class JobItem(var postedBy: String, var jobInfo: JobInfo, var recordInfo: RecordInfo) {

}

class JobInfo(var title: String, var description: String, var jobStatus: String) {
    var impressions: Int = 0
    var proposalsCount: Int = 0
}

class RecordInfo(var skill: Map<String, Any>?, var forWhom: Map<String, Any>?, var spInfo: Map<String, Any>?) {
    // initially no one has got the job
    fun makeRecord() {
        // accepted the job..
        // create record
    }
}

class JobItemModelData {
    companion object {
        const val JOB_TITLE = "title"
    }
}