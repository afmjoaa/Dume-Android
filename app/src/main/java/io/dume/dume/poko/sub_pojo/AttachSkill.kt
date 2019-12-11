package io.dume.dume.poko.sub_pojo

data class AttachSkill(
        val common_query_str: String,
        val creation: Creation,
        val dislikes: Dislikes,
        val enrolled: Int,
        val id: String,
        val jizz: Jizz,
        val likes: Likes,
        val location: Location,
        val mentor_uid: String,
        val package_name: String,
        val query_list: List<String>,
        val query_list_name: List<String>,
        val query_string: String,
        val rating: Int,
        val salary: Int,
        val status: Boolean,
        val totalRating: Int
)