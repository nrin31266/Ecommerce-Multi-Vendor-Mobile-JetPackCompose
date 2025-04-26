package com.nrin31266.ecommercemultivendor.domain.dto
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//Long id;
//
//String name;
//
//
//
//@NotNull
//@Column(unique = true, nullable = false)
//String categoryId;
//
//
//String parentCategory;
//
//@NotNull
//Integer level;
data class CategoryDto(
    var id : Long? = null,
    var name : String? = null,
    var categoryId : String? = null,
    var parentCategory : String? = null,
    var level : Int? = null
)
