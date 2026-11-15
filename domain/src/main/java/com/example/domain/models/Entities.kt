package com.example.domain.models

data class CryptoPair(
    val symbol: String,
    val name: String,
    val coinName: String,
    val iconUrl: String?
)

data class CryptoPrice(
    val symbol: String,
    val lastPrice: Double,
    val changePercent: Double
) {
    val isIncreased get() = changePercent >= 0
}

data class CryptoPairItem(
    val pair: CryptoPair,
    val price: CryptoPrice?,
    val sparkline: List<Double>?
)