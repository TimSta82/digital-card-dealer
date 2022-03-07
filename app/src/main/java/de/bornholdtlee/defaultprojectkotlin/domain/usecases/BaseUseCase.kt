package de.bornholdtlee.defaultprojectkotlin.domain.usecases

import de.bornholdtlee.defaultprojectkotlin.data.api.ResponseEvaluator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.component.KoinComponent

abstract class BaseUseCase : KoinComponent {

    protected val useCaseScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    sealed class UseCaseResult<T> {
        data class Success<T>(val resultObject: T) : UseCaseResult<T>()
        class Failure<T> : UseCaseResult<T>()
    }

    protected fun <T> responseCall(result: ResponseEvaluator.Result<T>): UseCaseResult<T> {
        return when (result) {
            is ResponseEvaluator.Result.Success -> result.response.body()?.let { body -> UseCaseResult.Success(body) } ?: UseCaseResult.Failure()
            else -> UseCaseResult.Failure()
        }
    }

    protected suspend fun <T : Any, S : Any> simpleResponseCall(
        result: ResponseEvaluator.Result<T>,
        onSuccess: suspend (t: T) -> S
    ): UseCaseResult<S> {
        return when (result) {
            is ResponseEvaluator.Result.Success -> result.response.body()?.let { body -> UseCaseResult.Success(onSuccess(body)) } ?: UseCaseResult.Failure()
            else -> UseCaseResult.Failure()
        }
    }
}
