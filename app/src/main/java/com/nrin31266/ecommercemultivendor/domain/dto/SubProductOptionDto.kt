package com.nrin31266.ecommercemultivendor.domain.dto
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//private Long id;
//private String optionValue;
//@ManyToOne
//@JoinColumn(name = "product_option_type_id")
//private ProductOptionType optionType;
//
//@ManyToOne
//@JsonIgnore
//@JoinColumn(name = "sub_product_id")
//private SubProduct subProduct;


data class SubProductOptionDto(
    var id : Long? = null,
    var optionValue : String? = null,
    var optionType : ProductOptionTypeDto? = null,
    var subProduct : SubProductDto? = null
)
