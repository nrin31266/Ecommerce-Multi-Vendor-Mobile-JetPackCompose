package com.nrin31266.ecommercemultivendor.domain.dto

import com.nrin31266.ecommercemultivendor.common.constant.ACCOUNT_STATUS
import com.nrin31266.ecommercemultivendor.common.constant.USER_ROLE

//Long id;
//String sellerName;
//String mobile;
//@Column(unique = true, nullable = false)
//String email;
//String password;
//@Embedded
//BusinessDetails businessDetails = new BusinessDetails();
//@Embedded
//BankDetails bankDetails = new BankDetails();
//@Enumerated(EnumType.STRING)
//USER_ROLE role;
//boolean isEmailVerified;
//@Enumerated(EnumType.STRING)
//ACCOUNT_STATUS accountStatus;
//String taxCode;
//@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//Address pickupAddress;
//String gstin;
data class SellerDto(
    var id : Long? = null,
    var sellerName : String? = null,
    var mobile : String? = null,
    var email : String? = null,
    var password : String? = null,
    var businessDetails : BusinessDetailsDto? = null,
    var bankDetails : BankDetailsDto? = null,
    var role : USER_ROLE? = null,
    var isEmailVerified : Boolean? = null,
    var accountStatus : ACCOUNT_STATUS? = null,
    var taxCode : String? = null,
    var pickupAddress : AddressDto? = null,
    var gstin : String? = null,

)
