package com.quizly.android.data.repository

import com.quizly.android.data.model.Avatar
import com.quizly.android.data.model.Spieler
import com.quizly.android.data.remote.ApiResult
import com.quizly.android.data.remote.ApiService
import com.quizly.android.data.remote.dto.GenericMessageResponse
import com.quizly.android.data.remote.dto.UploadAvatarRequest

/** Ported from api_client.py get_users/get_user/get_random_user/avatar*. */
class UserRepository(private val api: ApiService) {

    suspend fun getUsers(): ApiResult<List<Spieler>> = safeCall { api.getUsers() }

    suspend fun getUser(user: String): ApiResult<Spieler> = safeCall { api.getUser(user) }

    suspend fun getRandomUser(): ApiResult<Spieler> = safeCall { api.getRandomUser() }

    suspend fun getAvatar(user: String): ApiResult<Avatar> = safeCall { api.getAvatarJson(user) }

    suspend fun uploadAvatar(user: String, mimeType: String, base64Data: String): ApiResult<GenericMessageResponse> =
        safeCall { api.uploadAvatar(user, UploadAvatarRequest(mimeType, base64Data)) }
}
