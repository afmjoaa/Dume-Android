package io.dume.dume.poko

import com.google.firebase.firestore.GeoPoint

 data class MiniUser(var name: String?,
                    var birth_date: String?,
                    var mail: String?,
                    var nid: Long?,
                    var parmanent_location: GeoPoint?,
                    var avatar: String?,
                    var accoount_major: String,
                    var first_name: String?,
                    var last_name: String?,
                    var imei: List<String>?,
                    var obligated_user: Any?,
                    var obligation: Boolean = false,
                    var phone_number: String
)