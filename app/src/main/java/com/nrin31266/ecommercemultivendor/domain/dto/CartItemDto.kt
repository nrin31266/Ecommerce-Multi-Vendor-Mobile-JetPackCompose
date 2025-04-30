package com.nrin31266.ecommercemultivendor.domain.dto
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//@ManyToOne
//@JsonIgnore
//@JoinColumn(name = "cart_id")
//Cart cart;
//@ManyToOne
//
//@JoinColumn(name = "product_id")
//Product product;
//
//@ManyToOne
//@JoinColumn(name = "sub_product_id")
//SubProduct subProduct;
//
//
//int quantity;
//
//Long userId;
data class CartItemDto(
    val id : Long? = null,
    val cartId : Long? = null,
    val productId : Long? = null,
    val subProductId : Long? = null,
    val quantity : Int? = null,
    val userId : Long? = null
)
