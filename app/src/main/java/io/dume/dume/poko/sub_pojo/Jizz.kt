package io.dume.dume.poko.sub_pojo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.firebase.firestore.IgnoreExtraProperties

@JsonIgnoreProperties(ignoreUnknown = true)
@IgnoreExtraProperties
data class Jizz(
        val Capacity: String = "",
        val Category: String = "",
        val Class: String = "",
        val Gender: String = "",
        val Level: String = "",
        val Medium: String = "",
        val Salary: String = "",
        val Subject: String = ""
)