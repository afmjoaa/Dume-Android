package io.dume.dume.poko

data class Job(
        val common_query_str: String = "",
        val creation: Any = Any(),
        val location: Any = Any(),
        val enrolled: Int = 0,
        val id: String = "",
        val jizz: Map<String, Any> = emptyMap(),
        val mentor_uid: String = "",
        val package_name: String = "",
        val query_list: List<String> = emptyList(),
        val query_list_name: List<String> = emptyList(),
        val query_string: String = "",
        val rating: Int = 0,
        val salary: Int = 0,
        val status: Boolean = true,
        val totalRating: Int = 0,
        val likes: Map<String, Any> = emptyMap(),
        val dislikes: Map<String, Any> = emptyMap(),
        var proposals: List<Teacher> = emptyList(),
        var student_id: String = "",
        val job_status: String = Job.OPEN

) {


    companion object {
        val OPEN = "open"
        val CLOSED = "closed"
    }
}