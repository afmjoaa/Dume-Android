package io.dume.dume.poko.sub_pojo

data class Payments(
        val obligation_amount: String = "0",
        val obligation_currency: String = "0",
        val penalty_paid: String = "0",
        val total_paid: String = "0"
)