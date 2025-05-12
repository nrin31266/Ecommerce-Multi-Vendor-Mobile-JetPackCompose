package com.nrin31266.ecommercemultivendor.domain.dto

//@EqualsAndHashCode.Include
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//String title;
//@Column(length = 9999)
//String description;
//Long minMrpPrice;
//Long maxMrpPrice;
//int discountPercentage;
//Long minSellingPrice;
//Long maxSellingPrice;
//int totalSubProduct;
//int totalSold=0;
//int totalOrder=0;
//Boolean isSubProduct = false;
//@ElementCollection
//@CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
//@Column(name = "image_url")
//List<String> images;
//int numberRating;
//@ManyToOne
//@JoinColumn(name = "category_id")
//Category category;
//@ManyToOne
//Seller seller;
//LocalDateTime createdAt;
//@JsonIgnore
//@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//List<Review> reviews;
//@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//Set<ProductOptionType> optionsTypes = new HashSet<>();
//String optionKey;
//@JsonManagedReference
//@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//private List<SubProduct> subProducts = new ArrayList<>();
//@PrePersist
//protected void prePersist() {
//    this.createdAt = LocalDateTime.now();
//    this.reviews = new ArrayList<>();
//}
//@PreRemove
//private void preRemove() {
//    images.clear();
//}
data class ProductDto(
    var id : Long,
    var title : String = "",
    var description : String = "",
    var minMrpPrice : Long = 0L,
    var maxMrpPrice : Long = 0L,
    var discountPercentage : Int = 0,
    var minSellingPrice : Long = 0L,
    var maxSellingPrice : Long = 0L,
    var totalSubProduct : Int = 0,
    var totalSold : Int = 0,
    var totalOrder : Int = 0,
    var isSubProduct : Boolean = false,
    var images : List<String> = listOf(),
    var numberRating : Int = 0,
    var avgRating : Double = 0.0,
    var category : CategoryDto? = null,
    var seller : SellerDto?= null,
    var createdAt : String? = null,
//    var reviews : List<ReviewDto>? = null,
    var optionsTypes : List<ProductOptionTypeDto>? = null,
    var optionKey : String? = null,
    var subProducts : List<SubProductDto>? = null,
)
