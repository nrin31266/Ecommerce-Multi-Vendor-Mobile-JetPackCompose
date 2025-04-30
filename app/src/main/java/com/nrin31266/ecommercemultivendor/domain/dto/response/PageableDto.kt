package com.nrin31266.ecommercemultivendor.domain.dto.response

data class PageableDto<T>(
    val content: List<T>,  // Nội dung trang hiện tại
    val totalPages: Int,   // Tổng số trang
    val totalElements: Int,// Tổng số phần tử trong tất cả các trang
    val size: Int,         // Kích thước trang
    val number: Int,       // Số trang hiện tại
    val first: Boolean,    // Có phải là trang đầu tiên không
    val last: Boolean,     // Có phải là trang cuối cùng không
    val empty: Boolean     // Trang có rỗng hay không
)