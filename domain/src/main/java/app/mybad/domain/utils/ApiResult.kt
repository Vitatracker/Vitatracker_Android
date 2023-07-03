package app.mybad.domain.utils

const val ERROR_LOCAL: Int = 778
const val ERROR_NETWORK: Int = 777

sealed interface ApiResult {
    data class ApiSuccess(val data: Any) : ApiResult
    data class ApiError(val code: Int, val message: String?) : ApiResult
    data class ApiException(val e: Throwable) : ApiResult
}
