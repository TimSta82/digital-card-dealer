package de.bornholdtlee.defaultprojectkotlin.data.api

import de.bornholdtlee.defaultprojectkotlin.data.api.ResponseEvaluator.Result.Failure.*
import de.bornholdtlee.defaultprojectkotlin.data.api.ResponseEvaluator.Result.Failure.ClientError.*
import de.bornholdtlee.defaultprojectkotlin.data.api.ResponseEvaluator.Result.Failure.ServerError.*
import de.bornholdtlee.defaultprojectkotlin.data.api.ResponseEvaluator.Result.Success
import de.bornholdtlee.defaultprojectkotlin.data.api.ResponseEvaluator.Result.Success.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.net.HttpURLConnection

private const val STATUS_SUCCESS = 200
private const val STATUS_REDIRECT = 300
private const val STATUS_CLIENT_ERROR = 400
private const val STATUS_SERVER_ERROR = 500
private const val STATUS_UNKNOWN = 600

object ResponseEvaluator {

    private const val UNKNOWN_RESULT_CODE = 9001

    fun <T> evaluate(response: Response<T>?): Result<T> =
        response?.let {
            when (response.code()) {
                in STATUS_SUCCESS until STATUS_REDIRECT -> processSuccess(response)
                in STATUS_CLIENT_ERROR until STATUS_SERVER_ERROR -> processClientError(response)
                in STATUS_SERVER_ERROR until STATUS_UNKNOWN -> processServerError(response)
                else -> null
            } ?: UnknownError(response)
        } ?: UnknownError(generateUnknownResult())

    private fun <T> generateUnknownResult(): Response<T> = Response.error(UNKNOWN_RESULT_CODE, "".toResponseBody("application/json".toMediaTypeOrNull()))

    private fun <T> processSuccess(response: Response<T>): Success<T>? =
        when (response.code()) {
            HttpURLConnection.HTTP_OK -> Ok(response)
            HttpURLConnection.HTTP_CREATED -> Created(response)
            HttpURLConnection.HTTP_ACCEPTED -> Accepted(response)
            else -> null
        }

    @Throws(Exception::class)
    private fun <T> processClientError(response: Response<T>): ClientError<T>? =
        when (response.code()) {
            HttpURLConnection.HTTP_BAD_REQUEST -> BadRequest(response)
            HttpURLConnection.HTTP_UNAUTHORIZED -> Unauthorized(response)
            HttpURLConnection.HTTP_FORBIDDEN -> Forbidden(response)
            HttpURLConnection.HTTP_NOT_FOUND -> NotFound(response)
            else -> null
        }

    @Throws(Exception::class)
    private fun <T> processServerError(response: Response<T>): ServerError<T>? =
        when (response.code()) {
            HttpURLConnection.HTTP_INTERNAL_ERROR -> InternalError(response)
            HttpURLConnection.HTTP_BAD_GATEWAY -> BadGateway(response)
            HttpURLConnection.HTTP_UNAVAILABLE -> Unavailable(response)
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> GatewayTimeout(response)
            else -> null
        }

    sealed class Result<T>(val response: Response<T>) {

        /** Http-Status 200 - 299 **/
        sealed class Success<T>(response: Response<T>) : Result<T>(response) {

            /** Http-Status 200 **/
            class Ok<T>(response: Response<T>) : Success<T>(response)

            /** Http-Status 201 **/
            class Created<T>(response: Response<T>) : Success<T>(response)

            /** Http-Status 202 **/
            class Accepted<T>(response: Response<T>) : Success<T>(response)
        }

        /** Http-Status 400 - 599 **/
        sealed class Failure<T>(response: Response<T>) : Result<T>(response) {

            /** Http-Status 400 - 499 **/
            sealed class ClientError<T>(response: Response<T>) : Failure<T>(response) {
                /** Http-Status 400 **/
                class BadRequest<T>(response: Response<T>) : ClientError<T>(response)

                /** Http-Status 401 **/
                class Unauthorized<T>(response: Response<T>) : ClientError<T>(response)

                /** Http-Status 403 **/
                class Forbidden<T>(response: Response<T>) : ClientError<T>(response)

                /** Http-Status 404 **/
                class NotFound<T>(response: Response<T>) : ClientError<T>(response)
            }

            /** Http-Status 500 - 599 **/
            sealed class ServerError<T>(response: Response<T>) : Failure<T>(response) {
                /** Http-Status 500 **/
                class InternalError<T>(response: Response<T>) : ServerError<T>(response)

                /** Http-Status 502 **/
                class BadGateway<T>(response: Response<T>) : ServerError<T>(response)

                /** Http-Status 503 **/
                class Unavailable<T>(response: Response<T>) : ServerError<T>(response)

                /** Http-Status 504 **/
                class GatewayTimeout<T>(response: Response<T>) : ServerError<T>(response)
            }

            /** Http-Status 600 and above **/
            class UnknownError<T>(response: Response<T>) : Failure<T>(response)
        }
    }
}
