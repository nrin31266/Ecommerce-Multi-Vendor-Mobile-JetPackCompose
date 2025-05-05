package com.nrin31266.ecommercemultivendor.domain.dto.request

import com.vanrin05.app.domain.PAYMENT_METHOD

//PAYMENT_METHOD paymentMethod;
//Long addressId;
data class CreateOrderRequest(
    val paymentMethod: PAYMENT_METHOD,
    val addressId: Long,
)
