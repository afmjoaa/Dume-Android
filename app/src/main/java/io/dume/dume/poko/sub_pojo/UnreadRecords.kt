package io.dume.dume.poko.sub_pojo

data class UnreadRecords(
    val accepted_count: String = "0",
    val completed_count: String = "0",
    val current_count: String = "0",
    val pending_count: String = "0",
    val rejected_count: String = "0"
)