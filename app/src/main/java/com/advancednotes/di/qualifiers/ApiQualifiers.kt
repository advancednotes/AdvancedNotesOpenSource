package com.advancednotes.di.qualifiers

import javax.inject.Qualifier

class ApiQualifiers {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BasicInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AccessInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RefreshInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ForgotPasswordInterceptor

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BasicClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AccessClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RefreshClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ForgotPasswordClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class BasicRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class AccessRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class RefreshRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ForgotPasswordRetrofit
}