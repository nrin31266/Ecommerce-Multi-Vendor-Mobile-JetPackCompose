package com.nrin31266.ecommercemultivendor.domain.dto

//String accountNumber;
//String accountHolderName;
//String ifscCode;
//
//String swiftCode;
data class BankDetailsDto(
    val accountNumber: String?=null,
    val accountHolderName: String?=null,
    val ifscCode: String?=null,
    val swiftCode: String?=null
)
