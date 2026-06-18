package com.quizly.android.data.remote

import com.quizly.android.data.model.Avatar
import com.quizly.android.data.model.Frage
import com.quizly.android.data.model.Invite
import com.quizly.android.data.model.Kategorie
import com.quizly.android.data.model.LeaderboardEntry
import com.quizly.android.data.model.Match
import com.quizly.android.data.model.Schwierigkeitsgrad
import com.quizly.android.data.model.Settings
import com.quizly.android.data.model.Spieler
import com.quizly.android.data.remote.dto.CreateDuelMatchRequest
import com.quizly.android.data.remote.dto.CreateMatchResponse
import com.quizly.android.data.remote.dto.CreateSingleMatchRequest
import com.quizly.android.data.remote.dto.GameModeInfo
import com.quizly.android.data.remote.dto.GameStateInfo
import com.quizly.android.data.remote.dto.GenericMessageResponse
import com.quizly.android.data.remote.dto.InviteRequest
import com.quizly.android.data.remote.dto.SignInRequest
import com.quizly.android.data.remote.dto.SignInResponse
import com.quizly.android.data.remote.dto.SignUpRequest
import com.quizly.android.data.remote.dto.SignUpResponse
import com.quizly.android.data.remote.dto.SubmitAnswerRequest
import com.quizly.android.data.remote.dto.UpdateUserRequest
import com.quizly.android.data.remote.dto.UploadAvatarRequest
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 1:1 port of python-backend/src/services/api_client.py.
 * Every method below corresponds to one Python method of the same intent.
 */
interface ApiService {

    // ===== USER MANAGEMENT (confirmed) =====

    @PUT("user/{email}/signup")
    suspend fun signup(@Path("email") email: String, @Body request: SignUpRequest): Response<SignUpResponse>

    @HTTP(method = "GET", path = "user/{user}/signin", hasBody = true)
    suspend fun signin(@Path("user") user: String, @Body request: SignInRequest): Response<SignInResponse>

    @GET("user/{user}/signout")
    suspend fun signout(@Path("user") user: String): Response<GenericMessageResponse>

    @GET("user/{email}/verify")
    suspend fun verifyEmail(@Path("email") email: String, @Query("token") token: String): Response<GenericMessageResponse>

    @PATCH("user/{user}")
    suspend fun updateUser(@Path("user") user: String, @Body request: UpdateUserRequest): Response<Spieler>

    @GET("user")
    suspend fun getUsers(): Response<List<Spieler>>

    @GET("user/{user}")
    suspend fun getUser(@Path("user") user: String): Response<Spieler>

    @GET("user/{email}/password")
    suspend fun requestPasswordReset(@Path("email") email: String): Response<GenericMessageResponse>

    // ===== AVATAR (confirmed) =====

    @PUT("user/{user}/avatar")
    suspend fun uploadAvatar(@Path("user") user: String, @Body request: UploadAvatarRequest): Response<GenericMessageResponse>

    @Multipart
    @POST("user/{user}/avatar")
    suspend fun uploadAvatarFile(@Path("user") user: String, @Part avatar: MultipartBody.Part): Response<GenericMessageResponse>

    @GET("user/{user}/avatar")
    suspend fun getAvatarJson(@Path("user") user: String, @Query("type") type: String = "json"): Response<Avatar>

    @GET("user/{user}/avatar")
    suspend fun getAvatarRaw(@Path("user") user: String, @Query("type") type: String = "raw"): Response<ResponseBody>

    // ===== CATALOG (confirmed) =====

    @GET("category")
    suspend fun getCategories(@Query("number") number: Int? = null): Response<List<Kategorie>>

    @GET("category/{id}")
    suspend fun getCategory(@Path("id") id: Int): Response<Kategorie>

    @GET("difficulty")
    suspend fun getDifficulties(): Response<List<Schwierigkeitsgrad>>

    @GET("difficulty/{id}")
    suspend fun getDifficulty(@Path("id") id: Int): Response<Schwierigkeitsgrad>

    @GET("setting")
    suspend fun getSettings(): Response<Settings>

    @GET("gamemode")
    suspend fun getGameModes(): Response<List<GameModeInfo>>

    @GET("gamemode/{id}")
    suspend fun getGameMode(@Path("id") id: Int): Response<GameModeInfo>

    @GET("gamestate")
    suspend fun getGameStates(): Response<List<GameStateInfo>>

    @GET("gamestate/{id}")
    suspend fun getGameState(@Path("id") id: Int): Response<GameStateInfo>

    // ===== GAMEPLAY - MATCH MANAGEMENT (confirmed) =====

    @GET("user/random")
    suspend fun getRandomUser(): Response<Spieler>

    @GET("user/{user}/match")
    suspend fun getUserMatches(@Path("user") user: String, @Query("last") limit: Int? = null): Response<List<Match>>

    @GET("user/{user}/match/{matchType}")
    suspend fun getUserMatchesByType(
        @Path("user") user: String,
        @Path("matchType") matchType: String,
        @Query("last") limit: Int? = null
    ): Response<List<Match>>

    @POST("user/{userId}/match/single")
    suspend fun createSingleMatch(@Path("userId") userId: Int, @Body request: CreateSingleMatchRequest): Response<CreateMatchResponse>

    @POST("user/{userId}/match/duel")
    suspend fun createDuelMatch(@Path("userId") userId: Int, @Body request: CreateDuelMatchRequest): Response<CreateMatchResponse>

    // ===== GAMEPLAY - SPECULATIVE: not in API docs, verify against live server =====

    @GET("match/{matchId}")
    suspend fun getMatchDetails(@Path("matchId") matchId: Int): Response<Match>

    @GET("match/{matchId}/round/{roundNumber}/questions")
    suspend fun getRoundQuestions(@Path("matchId") matchId: Int, @Path("roundNumber") roundNumber: Int): Response<List<Frage>>

    @POST("match/{matchId}/answer")
    suspend fun submitAnswer(@Path("matchId") matchId: Int, @Body request: SubmitAnswerRequest): Response<GenericMessageResponse>

    @POST("match/{matchId}/end_turn")
    suspend fun endTurn(@Path("matchId") matchId: Int): Response<GenericMessageResponse>

    @POST("invite")
    suspend fun inviteFriend(@Body request: InviteRequest): Response<Invite>

    @POST("invite/{inviteId}/accept")
    suspend fun acceptInvite(@Path("inviteId") inviteId: Int): Response<GenericMessageResponse>

    @GET("invite")
    suspend fun getInvites(): Response<List<Invite>>

    @GET("leaderboard")
    suspend fun getLeaderboard(@Query("limit") limit: Int? = null, @Query("period") period: String? = null): Response<List<LeaderboardEntry>>
}
