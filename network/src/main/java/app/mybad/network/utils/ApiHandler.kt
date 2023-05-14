package app.mybad.network.utils

import app.mybad.domain.utils.ApiResult
import retrofit2.HttpException
import retrofit2.Response

object ApiHandler {

    suspend fun <T : Any?> handleApi(execute: suspend () -> Response<T>): ApiResult {
        return try {
            val response = execute()
            if (response.isSuccessful) {
                ApiResult.ApiSuccess(data = response.body() as Any? ?: "")
            } else {
                ApiResult.ApiError(code = response.code(), message = response.message())
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            ApiResult.ApiError(code = e.code(), message = e.message())
        } catch (e: Throwable) {
            e.printStackTrace()
            ApiResult.ApiException(e = e)
        }
    }
}
