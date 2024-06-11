package com.advancednotes.data.api.interceptors

import com.advancednotes.BuildConfig
import com.advancednotes.utils.user_agent.UserAgentHelper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class BasicInterceptor @Inject constructor(
    private val userAgentHelper: UserAgentHelper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        val authenticatedRequest: Request = request.newBuilder()
            .header("Client-ID", BuildConfig.CLIENT_ID)
            .header("User-Agent", userAgentHelper.generateUserAgent())
            .build()

        return chain.proceed(authenticatedRequest)
    }
}