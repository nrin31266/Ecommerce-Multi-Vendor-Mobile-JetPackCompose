package com.nrin31266.ecommercemultivendor.domain.dto.response

data class PageableDto<T>(
    val content: List<T>,
    val pageable: Pageable,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val number: Int,
    val sort: List<Any>,
    val numberOfElements: Int,
    val first: Boolean,
    val empty: Boolean
)

data class Pageable(
    val pageNumber: Int,
    val pageSize: Int,
    val sort: List<Any>,
    val offset: Long,
    val paged: Boolean,
    val unpaged: Boolean
)

