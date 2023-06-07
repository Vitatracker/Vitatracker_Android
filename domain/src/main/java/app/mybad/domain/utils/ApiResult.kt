package app.mybad.domain.utils

sealed interface ApiResult {
    data class ApiSuccess(val data: Any) : ApiResult
    data class ApiError(val code: Int, val message: String?) : ApiResult
    data class ApiException(val e: Throwable) : ApiResult
}
