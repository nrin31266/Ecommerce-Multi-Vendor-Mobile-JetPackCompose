package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.common.constant.PAYMENT_ORDER_STATUS
import com.vanrin05.app.domain.PAYMENT_METHOD
import java.time.LocalDateTime

//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//
//Long amount;
//@Enumerated(EnumType.STRING)
//PAYMENT_ORDER_STATUS paymentOrderStatus;
//@Enumerated(EnumType.STRING)
//PAYMENT_METHOD paymentMethod;
//
//LocalDateTime expiryDate;
//
//
//@ManyToOne
//@JoinColumn(name = "user_id")
//User user;
//
//
//@JsonManagedReference
//@OneToOne(mappedBy = "payment", orphanRemoval = true, cascade = CascadeType.ALL)
//Order order;

data class PaymentDto(
    val id: Long,
    val amount: Long,
    val paymentOrderStatus: PAYMENT_ORDER_STATUS,
    val paymentMethod: PAYMENT_METHOD,
    val expiryDate: LocalDateTime?=null,
    val user: UserDto,
    val order: OrderDto
)