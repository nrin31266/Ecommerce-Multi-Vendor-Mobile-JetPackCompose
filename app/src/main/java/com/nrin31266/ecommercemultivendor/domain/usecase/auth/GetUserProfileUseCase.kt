package com.nrin31266.ecommercemultivendor.domain.usecase.auth

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.UserDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(): Flow<ResultState<UserDto>> {
        return repo.getUserProfile()
    }
}