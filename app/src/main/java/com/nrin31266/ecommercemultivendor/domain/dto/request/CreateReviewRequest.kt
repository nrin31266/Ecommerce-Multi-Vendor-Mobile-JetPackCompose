package com.nrin31266.ecommercemultivendor.domain.dto.request

//String reviewText;
//Integer reviewRating;
//List<String> reviewImages;
data class CreateReviewRequest (
    val reviewText: String,
    val reviewRating: Int,
    var reviewImages: List<String> = emptyList()
)