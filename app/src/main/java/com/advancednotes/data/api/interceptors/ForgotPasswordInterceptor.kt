package com.advancednotes.data.api.interceptors

import android.content.Context
import com.advancednotes.BuildConfig
import com.advancednotes.utils.user_agent.UserAgentHelper
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class ForgotPasswordInterceptor @Inject constructor(
    private val context: Context,
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