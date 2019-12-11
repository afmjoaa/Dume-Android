package io.dume.dume.poko.sub_pojo

data class Payments(
    val obligation_amount: String,
    val obligation_currency: String,
    val penalty_paid: String,
    val total_paid: String
)