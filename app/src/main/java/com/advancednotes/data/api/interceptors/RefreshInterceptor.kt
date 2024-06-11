package com.advancednotes.data.api.interceptors

import com.advancednotes.BuildConfig
import com.advancednotes.data.database_users.entities.UserEntity
import com.advancednotes.data.database_users.repositories.UsersRepository
import com.advancednotes.utils.user_agent.UserAgentHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class RefreshInterceptor @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userAgentHelper: UserAgentHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val userEntity: UserEntity? = runBlocking(Dispatchers.IO) {
            usersRepository.getUser()
        }

        val authenticatedRequest: Request = if (userEntity?.refreshToken != null) {
            request.newBuilder()
                .header("Client-ID", BuildConfig.CLIENT_ID)
                .header("User-Agent", userAgentHelper.generateUserAgent())
                .header("Authorization", userEntity.refreshToken)
                .build()
        } else {
            request.newBuilder()
                .header("Client-ID", BuildConfig.CLIENT_ID)
                .header("User-Agent", userAgentHelper.generateUserAgent())
                .build()
        }

        return chain.proceed(authenticatedRequest)
    }
}