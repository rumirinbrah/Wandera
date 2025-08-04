package com.zzz.core.domain.result


internal typealias DomainError = Error

/**
 * Wandera Result handler
 */
sealed interface Result<out D , out E : Error> {

    data class Success<out D>(val data: D) : Result<D , Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing , E>

}

/**
 * Utility mapper function. Transforms input and returns it.
 */
inline fun <T , E : Error , R> Result<T , E>.map(map: (T) -> R): Result<R , E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}