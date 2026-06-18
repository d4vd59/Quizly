package com.quizly.android.data.repository

import com.quizly.android.data.remote.ApiResult
import retrofit2.Response
import java.io.IOException

/** Shared wrapper so every repository method returns [ApiResult] uniformly. */
internal suspend fun <T> safeCall(call: suspend () -> Response<T>): ApiResult<T> {
    return try {
        val response = call()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            ApiResult.HttpError(response.code(), response.errorBody()?.string())
        }
    } catch (e: IOException) {
        ApiResult.NetworkError(e)
    } catch (e: Exception) {
        ApiResult.NetworkError(e)
    }
}
