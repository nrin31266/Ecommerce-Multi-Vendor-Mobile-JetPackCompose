package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.common.constant.PAYMENT_STATUS
import java.time.LocalDateTime

//PAYMENT_STATUS paymentStatus;
//LocalDateTime paymentDate;
data class PaymentDetailsDto(
    val paymentStatus: PAYMENT_STATUS,
    val paymentDate: LocalDateTime?=null
)
