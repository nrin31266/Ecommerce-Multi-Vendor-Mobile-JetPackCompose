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
    val id: Long,
//    val password: String,
    val email: String,
    val sellerName: String,
    val mobile: String,
    val role: USER_ROLE,
    val isEmailVerified: Boolean,
    val accountStatus: ACCOUNT_STATUS,
    val taxCode: String,
    val gstin: String,
    val businessDetails: BusinessDetailsDto,
    val bankDetails: BankDetailsDto,
    val pickupAddress: AddressDto
    // Các trường khác của SellerDto
)
