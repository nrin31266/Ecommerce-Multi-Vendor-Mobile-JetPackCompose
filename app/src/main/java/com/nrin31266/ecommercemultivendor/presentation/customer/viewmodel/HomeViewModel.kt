package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.BannerDto
import com.nrin31266.ecommercemultivendor.domain.dto.response.HomeCategoryResponse
import com.nrin31266.ecommercemultivendor.domain.usecase.home.GetBannersUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.home.GetHomeCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBannersUseCase: GetBannersUseCase,
    private val getHomeCategoriesUseCase: GetHomeCategoriesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        getHomeData()
    }

    fun getHomeData() {
        viewModelScope.launch {
            Log.d(TAG, "Get Home Data")
            _state.value = _state.value.copy(isLoading = true)
            combine(
                getBannersUseCase(),
                getHomeCategoriesUseCase()
            ) { bannerResult, categoryResult ->
                when {
                    bannerResult is ResultState.Success && categoryResult is ResultState.Success -> {
                        _state.value = _state.value.copy(
                            banners = bannerResult.data,
                            homeCategory = categoryResult.data,
                            isLoading = false
                        )
                    }

                    bannerResult is ResultState.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = bannerResult.message
                        )
                    }

                    categoryResult is ResultState.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = categoryResult.message
                        )
                    }


                }
            }.collect()
        }
    }

}

data class HomeState(
    val banners: List<BannerDto> = emptyList(),
    val homeCategory: HomeCategoryResponse = HomeCategoryResponse(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,

    )