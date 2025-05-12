package com.nrin31266.ecommercemultivendor.domain.usecase.wishlist

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.WishlistItemDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserWishlistUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(): Flow<ResultState<List<WishlistItemDto>>> =repo.getUserWishlist()
}