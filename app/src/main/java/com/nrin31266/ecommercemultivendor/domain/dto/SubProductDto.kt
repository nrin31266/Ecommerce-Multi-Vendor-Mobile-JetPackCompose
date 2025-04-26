package com.nrin31266.ecommercemultivendor.domain.dto
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//@EqualsAndHashCode.Include
//Long id;
//int quantity;
//Long mrpPrice;
//Long sellingPrice;
//int discountPercentage;
//@ElementCollection
//List<String> images;
//
//@JsonBackReference
//@ManyToOne
//@JoinColumn(name = "product_id")
//private Product product;
//
//@OneToMany(mappedBy = "subProduct", cascade = CascadeType.ALL, orphanRemoval = true)
//private Set<SubProductOption> options;
data class SubProductDto(
    var id : Long? = null,
    var quantity : Int? = null,
    var mrpPrice : Long? = null,
    var sellingPrice : Long? = null,
    var discountPercentage : Int? = null,
    var images : List<String>? = null,
    var product : ProductDto? = null,
    var options : List<SubProductOptionDto>? = null
)
