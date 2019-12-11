package io.dume.dume.poko

import com.google.gson.annotations.SerializedName
import io.dume.dume.poko.sub_pojo.*

class Teacher(val academic: Academic,
              val account_active: Boolean,
              val achievements: Achievements,
              val applied_promo: List<Any>,
              val available_promo: List<Any>,
              val avatar: String,
              val birth_date: String,
              val current_status: String,
              val custom_query_array: List<String>,
              val d: D,
              val daily_i: String,
              val daily_r: String,
              val email: String,
              val first_name: String,
              val g: String,
              val gender: String,
              val l: L,
              val last_name: String,
              val location: LocationX,
              val marital: String,
              val obligation: Boolean,
              val p_daily_i: String,
              val p_daily_r: String,
              val payments: Payments,
              val penalty: Int,
              val phone_number: String,
              @SerializedName("pro_com_%") val pro_com: String,
              val rating_array: List<Any>,
              val referer_id: String,
              val referred: Boolean,
              val religion: String,
              val self_rating: SelfRating,
              val skill_id: String,
              val unread_msg: String,
              val unread_noti: String,
              val unread_records: UnreadRecords,
              val user_ref_link: String)  {



}