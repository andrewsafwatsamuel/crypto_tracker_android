package com.example.domain.usecases

import com.example.domain.mappers.Mapper
import com.example.domain.models.CryptoPair
import com.example.domain.models.CryptoPairItem
import com.example.domain.models.CryptoPrice
import com.example.domain.repository.CoinPairsRepo
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetCoinsDataUseCase(
    private val repo: CoinPairsRepo,
    private val mapper: Mapper<Pair<List<CryptoPair>, List<CryptoPrice>>, List<CryptoPairItem>>
) {
    suspend operator fun invoke(): Result<List<CryptoPairItem>> = coroutineScope {
        val pairs =
            async { repo.getPairs() }.await().getOrElse { return@coroutineScope Result.failure(it) }
        val prices = async { repo.getPrices() }.await().getOrNull().orEmpty()
        Result.success(mapper.map(pairs to prices))
    }
}