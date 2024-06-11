package com.advancednotes.data.api.interceptors

import com.advancednotes.BuildConfig
import com.advancednotes.data.database_users.entities.UserEntity
import com.advancednotes.data.database_users.repositories.UsersRepository
import com.advancednotes.utils.user_agent.UserAgentHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AccessInterceptor @Inject constructor(
    private val usersRepository: UsersRepository,
    private val userAgentHelper: UserAgentHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val userEntity: UserEntity? = runBlocking(Dispatchers.IO) {
            usersRepository.getUser()
        }

        val newRequestBuilder = if (userEntity?.accessToken != null) {
            request.newBuilder()
                .header("Client-ID", BuildConfig.CLIENT_ID)
                .header("User-Agent", userAgentHelper.generateUserAgent())
                .header("Authorization", userEntity.accessToken)
        } else {
            request.newBuilder()
                .header("Client-ID", BuildConfig.CLIENT_ID)
                .header("User-Agent", userAgentHelper.generateUserAgent())
        }

        return chain.proceed(newRequestBuilder.build())
    }
}