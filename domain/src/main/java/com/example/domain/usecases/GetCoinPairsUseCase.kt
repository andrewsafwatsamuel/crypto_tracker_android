package com.example.domain.usecases

import com.example.domain.models.CryptoPairItem
import com.example.domain.repository.CoinPairsRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit

class GetCoinPairsUseCase(
    private val repo: CoinPairsRepo,
    private val dispatcher: CoroutineDispatcher,
    private val getCoinsDataUseCase: GetCoinsDataUseCase
) {

    private val semaphore = Semaphore(5)
    private val mutex = Mutex()

    operator fun invoke(): Flow<Result<List<CryptoPairItem>>> = channelFlow {
        getCoinsDataUseCase()
            .getOrElse {
                send(Result.failure(it))
                return@channelFlow
            }
            .also { send(Result.success(it)) }
            .toMutableList()
            .also { putSparkLines(it) }
            .also { send(Result.success(it)) }
    }.flowOn(dispatcher)

    private fun ProducerScope<Result<MutableList<CryptoPairItem>>>.putSparkLines(
        pairs: MutableList<CryptoPairItem>
    ) = pairs.forEachIndexed { index, pair ->
        launch {
            semaphore.withPermit {
                val line = repo.getSparkline(pair.pair.symbol).getOrNull()
                mutex.withLock {
                    pairs[index] = pairs[index].copy(sparkline = line)
                }
            }
        }
    }
}