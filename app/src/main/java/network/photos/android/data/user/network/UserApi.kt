package network.photos.android.data.user.network

import network.photos.android.data.user.network.model.TokenResponse
import network.photos.android.data.user.network.model.UserResponse
import retrofit2.http.*

interface UserApi {
    /**
     * OAuth token request based on [RFC6749](https://tools.ietf.org/html/rfc6749#section-4.1.3)
     *
     * @param code The authorization code received from the authorization server.
     * @param clientId The client identifier issued by the authorization server.
     * @param redirectUri The redirect_uri included in the authorization request.
     */
    @FormUrlEncoded
    @POST("/oauth/token?grant_type=authorization_code")
    suspend fun accessTokenRequest(
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): TokenResponse

    /**
     * OAuth refresh token request based on [RFC6749](https://tools.ietf.org/html/rfc6749#section-6)
     *
     * @param refreshToken The refresh token issued to the client.
     * @param clientId The client identifier issued by the authorization server.
     * @param clientSecret The client secret issued during registration process.
     * @param scope list of case-sensitive strings to grant access based on.
     */
    @POST("/oauth/token?grant_type=refresh_token")
    suspend fun refreshTokenRequest(
        @Query("refresh_token") refreshToken: String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("scope") scope: List<String>
    ): TokenResponse

    /**
     * OAuth token revocation based on [RFC7000](https://tools.ietf.org/html/rfc7009#section-2.1)
     *
     * @param token The token to revoke on the oauth server.
     * @param tokenTypeHint This optional param should either be 'access_token' or 'refresh_token'.
     */
    @POST("/revoke")
    suspend fun revokeTokenRequest(
        @Query("token") token: String,
        @Query("token_type_hint") tokenTypeHint: String?
    ): TokenResponse

    @GET("/v1/user/")
    suspend fun me(@Header("Authorization") token: String): UserResponse
}
