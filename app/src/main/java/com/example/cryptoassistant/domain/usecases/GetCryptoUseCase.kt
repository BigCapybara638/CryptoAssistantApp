package com.example.cryptoassistant.domain.usecases

import com.example.cryptoassistant.domain.models.CryptoItem
import com.example.cryptoassistant.domain.repositories.CryptoRepository


class GetCryptoUseCase(
    private val cryptoRepository: CryptoRepository
) {

    suspend operator fun invoke(
        limit: Int = 20
    ) : List<CryptoItem> {
        return cryptoRepository.getTopCryptos(limit)
    }

}