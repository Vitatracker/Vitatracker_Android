package app.mybad.network.utils

sealed interface ApiResult {
    data class ApiSuccess(val data: List<Any>) : ApiResult
    data class ApiError(val code: Int, val message: String?) : ApiResult
    data class ApiException(val e: Throwable) : ApiResult
}