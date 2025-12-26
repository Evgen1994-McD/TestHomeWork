package com.example.testhomework.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Базовый класс для Use Cases в Clean Architecture
 * @param P - тип параметров
 * @param R - тип результата
 */
abstract class UseCase<in P, R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    /**
     * Выполняет Use Case
     * @param params параметры для выполнения
     * @return результат выполнения
     */
    suspend operator fun invoke(params: P): Result<R> {
        return try {
            withContext(dispatcher) {
                val result = execute(params)
                Result.success(result)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Абстрактный метод для реализации логики Use Case
     */
    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(params: P): R
}

/**
 * Use Case без параметров
 */
abstract class UseCaseNoParams<R>(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend operator fun invoke(): Result<R> {
        return try {
            withContext(dispatcher) {
                Result.success(execute())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(): R
}
