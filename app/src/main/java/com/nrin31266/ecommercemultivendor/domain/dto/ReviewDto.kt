package com.nrin31266.ecommercemultivendor.domain.dto
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//
//@Column(nullable = false, length = 999)
//String reviewText;
//
//@ElementCollection
//List<String> reviewImages = new ArrayList<>();
//
//Integer reviewRating;
//
//@JsonIgnore
//@JsonBackReference
//@ManyToOne
//@JoinColumn(name = "product_id")
//Product product;
//
//@ManyToOne
//SubProduct subProduct;
//
//@ManyToOne
//User user;
//
//@Column(nullable = false)
//LocalDateTime createdAt;
//
//@PrePersist
//protected void prePersist() {
//    this.createdAt = LocalDateTime.now();
//}
data class ReviewDto(
    val id: Long,
    val reviewText: String,
    val reviewImages: List<String>,
    val reviewRating: Int,
    val product: ProductDto?=null,
    val subProduct: SubProductDto,
    val user: UserDto,
    val createdAt: String,
)