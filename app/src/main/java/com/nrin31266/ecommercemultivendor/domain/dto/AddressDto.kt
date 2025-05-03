package com.nrin31266.ecommercemultivendor.domain.dto
//Long id;
//
//String name;
//
//String phoneNumber;
//
//String street;
//
//String ward;
//
//String district;
//
//String province;
//
//String postalCode;    // Mã bưu điện
data class AddressDto(
    var id : Long? = null,
    var name : String? = null,
    var phoneNumber : String? = null,
    var street : String? = null,
    var ward : String? = null,
    var district : String? = null,
    var province : String? = null,
    var postalCode : String? = null,
    var isDefault : Boolean?= null
)
