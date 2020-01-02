package io.dume.dume.poko

import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName
import io.dume.dume.poko.sub_pojo.*

class User(val name: String? = null,
           val academic: Academic? = null,
           val account_active: Boolean = true,
           val achievements: Achievements = Achievements(),
           val applied_promo: List<Any>? = emptyList(),
           val available_promo: List<Any>? = emptyList(),
           val avatar: String? = null,
           val birth_date: String? = null,
           val current_status: String? = null,
           val custom_query_array: List<String> = emptyList(),
           val d: D? = null,
           val daily_i: String? = null,
           val daily_r: String? = null,
           val email: String? = null,
           val first_name: String? = null,
           val g: String? = null,
           val gender: String? = null,
           val l: L? = null,
           val last_name: String? = null,
           val location: GeoPoint? = null,
           val marital: String? = null,
           val obligation: Boolean? = null,
           val p_daily_i: String? = null,
           val p_daily_r: String? = null,
           val payments: Payments = Payments(),
           val penalty: Int? = null,
           val phone_number: String? = null,
           @SerializedName("pro_com_%") val pro_com: String? = null,
           val rating_array: List<Any>? = emptyList(),
           val referer_id: String? = null,
           val referred: Boolean? = null,
           val religion: String? = null,
           val self_rating: SelfRating = SelfRating(),
           val skill_id: String? = null,
           val unread_msg: String? = null,
           val unread_noti: String? = null,
           val unread_records: UnreadRecords = UnreadRecords(),
           val user_ref_link: String? = null,
           val nid: Long? = null,
           val id: String? = null) {

}