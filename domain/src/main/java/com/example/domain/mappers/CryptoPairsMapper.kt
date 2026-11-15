package com.example.domain.mappers

import com.example.domain.models.CryptoPair
import com.example.domain.models.CryptoPairItem
import com.example.domain.models.CryptoPrice

class CryptoPairsMapper :
    Mapper<Pair<List<CryptoPair>, List<CryptoPrice>>, List<CryptoPairItem>> {
    override fun map(
        from: Pair<List<CryptoPair>, List<CryptoPrice>>
    ): List<CryptoPairItem> =from.let{ (pairsSource, pricesSource) ->
        val prices = pricesSource.associateBy { it.symbol }
        return pairsSource.map { CryptoPairItem(it, prices[it.symbol], null) }
    }
}