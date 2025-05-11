package com.nrin31266.ecommercemultivendor.presentation.customer.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.ecommercemultivendor.common.AuthPreferences
import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.common.constant.PRICE_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.RATING_FILTER
import com.nrin31266.ecommercemultivendor.common.constant.SORT_PRODUCTS
import com.nrin31266.ecommercemultivendor.domain.dto.ProductDto
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.SendEmailOtpUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.UserLoginUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.UserSignupUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.auth.VerifyTokenUseCase
import com.nrin31266.ecommercemultivendor.domain.usecase.products.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    val getProductUseCase: GetProductUseCase,
    private val savedStateHandle: SavedStateHandle,  // ← Thêm dòng này
) : ViewModel() {
    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state



    init {
//        "shoes"	"shoes"
//        ""	null
//        " "	null
//        null	null
        val search = savedStateHandle.get<String>("search")?.takeIf { it.isNotBlank() }
        val category = savedStateHandle.get<String>("category")?.takeIf { it.isNotBlank() }
//        val sort = savedStateHandle.get<String>("sort")?.takeIf { it.isNotBlank() }
        Log.d(TAG, "Query: $search, $category")
        _state.value = _state.value.copy(
            search = search,
            category = category,
        )
        getProduct()
    }

    fun onSort(sort: SORT_PRODUCTS){
        _state.value = _state.value.copy(
            sort = sort,
            products = emptyList()
        )
        getProduct()
    }

    fun onChangeFilter(filter: ProductFilter) {
        _state.update {
            when (filter) {
                is ProductFilter.Price -> it.copy(priceFilter = filter.filter)
                is ProductFilter.Rating -> it.copy(ratingFilter = filter.filter)
            }
        }
    }


    fun onResetFilter(){
        _state.value = _state.value.copy(
            priceFilter = null,
            ratingFilter = null,
            products = emptyList()
        )
        onToggleRightSheet()
        getProduct()
    }

    fun onFilter(){
        _state.value = _state.value.copy(
            products = emptyList()
        )
        onToggleRightSheet()
        getProduct()
    }

     fun onToggleRightSheet(){
        _state.value = _state.value.copy(isVisibleRightSheet = !state.value.isVisibleRightSheet)
    }

    private fun getProduct() {
        viewModelScope.launch {
            getProductUseCase(
                pageNumber = 1,
                search = state.value.search,
                category = state.value.category,
                sort = state.value.sort,
                priceFilter = state.value.priceFilter,
                ratingFilter = state.value.ratingFilter
            ).collect { rs->
                when (rs) {
                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(
                            isLoading = true,
                            errorMessage = null
                        )
                    }

                    is ResultState.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            pageNumber = 1,
                            products = rs.data.content?: emptyList(),
                            totalPages = rs.data.totalPages,
                            totalElements = rs.data.totalElements,
                            lastPage = rs.data.last,
                            size = rs.data.size
                        )

                    }

                    is ResultState.Error -> {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            errorMessage = rs.message
                        )
                    }
                }
            }
        }
    }
    fun loadMoreProducts() {
        Log.d("ProductsViewModel", "loadMoreProducts")
        if (state.value.isLoadMoreLoading || state.value.lastPage) return
        viewModelScope.launch {
            getProductUseCase(
                pageNumber = state.value.pageNumber + 1,
                search = state.value.search,
                category = state.value.category,
                sort = state.value.sort,
                priceFilter = state.value.priceFilter,
                ratingFilter = state.value.ratingFilter
            ).collect {
                when (it) {

                    is ResultState.Loading -> {
                        _state.value = _state.value.copy(
                            isLoadMoreLoading = true,
                            errorMessage = null
                        )

                    }
                    is ResultState.Success -> {
                        _state.value = _state.value.copy(
                            pageNumber = it.data.number + 1,
                            isLoadMoreLoading = false,
                            products = _state.value.products + it.data.content,
                            totalPages = it.data.totalPages,
                            totalElements = it.data.totalElements,
                            lastPage = it.data.last,
                            size = it.data.size,


                        )

                    }
                    is ResultState.Error -> {
                        _state.value = _state.value.copy(
                            isLoadMoreLoading = false,
                            errorMessage = it.message
                        )
                    }

                }
            }
        }
    }
    sealed class ProductsEvent {
        data class ShowSnackbar(val message: String): ProductsEvent()
    }


    sealed class ProductFilter {
        data class Price(val filter: PRICE_FILTER?) : ProductFilter()
        data class Rating(val filter: RATING_FILTER?) : ProductFilter()
    }
}

data class State(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoadMoreLoading: Boolean = false,
    val products: List<ProductDto> = emptyList(),
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val lastPage: Boolean = true,
    val size: Int = 0, //pageSize
    val pageNumber: Int = 1, // number

    val category: String? = null,
    val sort: SORT_PRODUCTS = SORT_PRODUCTS.DEFAULT,
    val search: String? = null,

    //filter
    val priceFilter: PRICE_FILTER? = null,
    val ratingFilter: RATING_FILTER? = null,

    val isVisibleRightSheet: Boolean = false,

    )


