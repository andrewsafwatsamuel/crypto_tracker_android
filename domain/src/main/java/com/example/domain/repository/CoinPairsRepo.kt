package com.example.domain.repository

import com.example.domain.models.CryptoPair
import com.example.domain.models.CryptoPrice

interface CoinPairsRepo {
    suspend fun getPairs(): Result<List<CryptoPair>>
    suspend fun getPrices(): Result<List<CryptoPrice>>
    suspend fun getSparkline(symbol: String): Result<List<Double>>
}