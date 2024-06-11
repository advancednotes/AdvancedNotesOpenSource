package com.advancednotes.di.qualifiers

import javax.inject.Qualifier

class DatabaseQualifiers {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DatabaseNotes

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DatabaseUsers
}