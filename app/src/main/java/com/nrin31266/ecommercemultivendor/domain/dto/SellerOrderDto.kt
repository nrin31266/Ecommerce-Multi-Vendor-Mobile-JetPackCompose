package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.common.constant.SELLER_ORDER_STATUS
import java.time.LocalDateTime

//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//
//@JsonBackReference
//@ManyToOne
//@JoinColumn(name = "order_id", nullable = false)
//Order order;
//
//Integer totalItem = 0;
//
//Long totalPrice = 0L;
//Long discountShipping = 0L;
//Long discountShop = 0L;
//Long discountPlatform = 0L;
//Long finalPrice = 0L;
//
//@JsonManagedReference
//@OneToMany(mappedBy = "sellerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
//List<OrderItem> orderItems = new ArrayList<>();
//
//String cancelReason;
//@Enumerated(EnumType.STRING)
//SELLER_ORDER_STATUS status;
//
//@Embedded
//@AttributeOverrides({
//    @AttributeOverride(name = "paymentStatus", column = @Column(name = "payment_status")),
//})
//PaymentDetails paymentDetails = new PaymentDetails();
//
//@CreatedDate
//LocalDateTime createdDate;
//
//@LastModifiedDate
//LocalDateTime updatedDate;
//
//@ManyToOne
//@JoinColumn(name = "seller_id")
//Seller seller;
//
//LocalDateTime deliveryDate;
//
//Boolean isApproved = false;
data class SellerOrderDto(
    val id: Long,
    val order: OrderDto,
    val totalItem: Int,
    val totalPrice: Long,
    val discountShipping: Long,
    val discountShop: Long,
    val discountPlatform: Long,
    val finalPrice: Long,
    val orderItems: List<OrderItemDto>?= emptyList(),
    val cancelReason: String,
    val status: SELLER_ORDER_STATUS,
    val paymentDetails: PaymentDetailsDto,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime?=null,
    val seller: SellerDto,
)
