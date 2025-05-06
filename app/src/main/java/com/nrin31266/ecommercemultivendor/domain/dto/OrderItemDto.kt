package com.nrin31266.ecommercemultivendor.domain.dto
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//@JsonBackReference
//@ManyToOne
//@JoinColumn(name = "seller_order_id", nullable = false)
//SellerOrder sellerOrder;
//@ManyToOne(cascade = CascadeType.ALL)
//@JoinColumn(name = "product_id")
//Product product;
//@ManyToOne(cascade = CascadeType.ALL)
//@JoinColumn(name = "sub_product_id")
//SubProduct subProduct;
//int quantity;
//Long mrpPrice;
//Long sellingPrice;
//Long userId;
data class OrderItemDto(
    val id: Long,
    val sellerOrder: SellerOrderDto?=null,
    val product: ProductDto,
    val subProduct: SubProductDto,
    val quantity: Int,
    val mrpPrice: Long,
    val sellingPrice: Long,
    val userId: Long,
    val isRated: Boolean = false
)
