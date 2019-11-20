package io.dume.dume.teacher.dashboard.jobboard.models

class JobItem {
    var title: String? = null
    var description: String? = null
    var salary: Int? = null
    var location: String? = null
    var skill: Map<String, Any>? = null
    var for_whom: Map<String, Any>? = null
    var view_count: Int? = null
    var proposals: Int? = null

    var sp_info: Map<String, Any>? = null

}