package io.dume.dume.poko.sub_pojo

data class UnreadRecords(
    val accepted_count: String,
    val completed_count: String,
    val current_count: String,
    val pending_count: String,
    val rejected_count: String
)