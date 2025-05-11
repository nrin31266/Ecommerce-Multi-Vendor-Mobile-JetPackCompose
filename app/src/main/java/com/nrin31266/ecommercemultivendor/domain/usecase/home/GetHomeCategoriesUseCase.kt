package com.nrin31266.ecommercemultivendor.domain.usecase.home

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.response.HomeCategoryResponse
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

data class GetHomeCategoriesUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke() : Flow<ResultState<HomeCategoryResponse>> {
        return repo.getHomeCategories()
    }
}
