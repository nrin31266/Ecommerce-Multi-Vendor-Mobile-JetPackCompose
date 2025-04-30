package com.nrin31266.ecommercemultivendor.domain.dto
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//@OneToOne
//User user;
//@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
//List<CartItem> cartItems = new ArrayList<>();
//Long totalSellingPrice = 0L;
//int totalItems = 0;
//Long totalMrpPrice = 0L;
//int discount;
//String couponCode;
//
//public Cart(User user) {
//    this.user = user;
//}
data class CartDto(
    val id : Long? = null,
    val user : UserDto? = null,
    val cartItems : List<CartItemDto>? = null,
    val totalSellingPrice : Long? = null,
    val totalMrpPrice : Long? = null,
    val totalItems : Int? = null,
    val discount : Int? = null,
    val couponCode : String? = null
)