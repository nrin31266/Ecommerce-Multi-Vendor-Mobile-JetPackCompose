package com.nrin31266.ecommercemultivendor.domain.usecase.payment

import com.nrin31266.ecommercemultivendor.common.ResultState
import com.nrin31266.ecommercemultivendor.domain.dto.PaymentDto
import com.nrin31266.ecommercemultivendor.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPendingPaymentsUseCase @Inject constructor(private val repo: Repo) {
    operator fun invoke(): Flow<ResultState<List<PaymentDto>>>{
        return repo.userPendingPayment()
    }
}