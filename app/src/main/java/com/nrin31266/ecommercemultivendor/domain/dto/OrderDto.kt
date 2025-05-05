package com.nrin31266.ecommercemultivendor.domain.dto

import com.vanrin05.app.domain.PAYMENT_METHOD
import java.time.LocalDateTime

//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//@ManyToOne
//@JoinColumn(name = "user_id", nullable = false)
//User user;
//@JsonManagedReference
//@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//List<SellerOrder> sellerOrders;
//@ManyToOne
//@JoinColumn(name = "shipping_address_id")
//Address shippingAddress;
//Long totalMrpPrice;
//Long totalSellingPrice;
//int discountPercentage;
//int totalItem;
//@Enumerated(EnumType.STRING)
//@JsonBackReference
//@OneToOne
//@JoinColumn(name = "payment_order_id")
//Payment payment;
//PAYMENT_METHOD paymentMethod;
//@CreatedDate
//LocalDateTime orderDate;
data class OrderDto(
    val id: Long,
    val user: UserDto,
    val sellerOrders: List<SellerOrderDto>,
    val shippingAddress: AddressDto,
    val totalMrpPrice: Long,
    val totalSellingPrice: Long,
    val discountPercentage: Int,
    val totalItem: Int,
    val payment: PaymentDto?=null,
    val paymentMethod: PAYMENT_METHOD,
    val orderDate: String
)
