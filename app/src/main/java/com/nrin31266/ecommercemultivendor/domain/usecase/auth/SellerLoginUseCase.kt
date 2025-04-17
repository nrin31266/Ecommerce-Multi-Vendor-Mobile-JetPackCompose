package com.nrin31266.ecommercemultivendor.domain.usecase.auth

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.request.AuthRequest
import com.nrin31266.ecommercemultivendor.domain.dto.response.AuthResponse
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SellerLoginUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(authRequest: AuthRequest): Flow<ResultState<AuthResponse>> {
        return repo.sellerLogin(authRequest)
    }
}