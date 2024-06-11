import java.util.Properties

val properties = Properties()
properties.load(File("local.properties").inputStream())

val clientId: String = properties.getProperty("CLIENT_ID")
val baseUrl: String = properties.getProperty("BASE_URL")

val uiMapEndpoint: String = properties.getProperty("UI_MAP_ENDPOINT")

val verifyEmailEndpoint: String = properties.getProperty("VERIFY_EMAIL_ENDPOINT")
val loginEndpoint: String = properties.getProperty("LOGIN_ENDPOINT")
val refreshLoginEndpoint: String = properties.getProperty("REFRESH_LOGIN_ENDPOINT")
val logoutEndpoint: String = properties.getProperty("LOGOUT_ENDPOINT")
val registerEndpoint: String = properties.getProperty("REGISTER_ENDPOINT")

val filesEndpoint: String = properties.getProperty("FILES_ENDPOINT")

val preSynchronizerNotesEndpoint: String = properties.getProperty("PRE_SYNCHRONIZER_NOTES_ENDPOINT")
val preSynchronizerFilesEndpoint: String = properties.getProperty("PRE_SYNCHRONIZER_FILES_ENDPOINT")
val preSynchronizerTagsEndpoint: String = properties.getProperty("PRE_SYNCHRONIZER_TAGS_ENDPOINT")

val synchronizerNoteEndpoint: String = properties.getProperty("SYNCHRONIZER_NOTE_ENDPOINT")
val synchronizerFileEndpoint: String = properties.getProperty("SYNCHRONIZER_FILE_ENDPOINT")
val synchronizerTagEndpoint: String = properties.getProperty("SYNCHRONIZER_TAG_ENDPOINT")

val userFirstNameEndpoint: String = properties.getProperty("USER_FIRST_NAME_ENDPOINT")
val userLastNameEndpoint: String = properties.getProperty("USER_LAST_NAME_ENDPOINT")
val userPasswordEndpoint: String = properties.getProperty("USER_PASSWORD_ENDPOINT")

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    //id("com.google.gms.google-services")
    //id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.advancednotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.advancednotes"
        minSdk = 26
        targetSdk = 34
        versionCode = 51
        versionName = "1.5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

        buildConfigField("String", "CLIENT_ID", "\"$clientId\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")

        buildConfigField("String", "UI_MAP_ENDPOINT", "\"$uiMapEndpoint\"")

        buildConfigField("String", "VERIFY_EMAIL_ENDPOINT", "\"$verifyEmailEndpoint\"")
        buildConfigField("String", "LOGIN_ENDPOINT", "\"$loginEndpoint\"")
        buildConfigField("String", "REFRESH_LOGIN_ENDPOINT", "\"$refreshLoginEndpoint\"")
        buildConfigField("String", "LOGOUT_ENDPOINT", "\"$logoutEndpoint\"")
        buildConfigField("String", "REGISTER_ENDPOINT", "\"$registerEndpoint\"")

        buildConfigField("String", "FILES_ENDPOINT", "\"$filesEndpoint\"")

        buildConfigField("String", "PRE_SYNCHRONIZER_NOTES_ENDPOINT", "\"$preSynchronizerNotesEndpoint\"")
        buildConfigField("String", "PRE_SYNCHRONIZER_FILES_ENDPOINT", "\"$preSynchronizerFilesEndpoint\"")
        buildConfigField("String", "PRE_SYNCHRONIZER_TAGS_ENDPOINT", "\"$preSynchronizerTagsEndpoint\"")

        buildConfigField("String", "SYNCHRONIZER_NOTE_ENDPOINT", "\"$synchronizerNoteEndpoint\"")
        buildConfigField("String", "SYNCHRONIZER_FILE_ENDPOINT", "\"$synchronizerFileEndpoint\"")
        buildConfigField("String", "SYNCHRONIZER_TAG_ENDPOINT", "\"$synchronizerTagEndpoint\"")

        buildConfigField("String", "USER_FIRST_NAME_ENDPOINT", "\"$userFirstNameEndpoint\"")
        buildConfigField("String", "USER_LAST_NAME_ENDPOINT", "\"$userLastNameEndpoint\"")
        buildConfigField("String", "USER_PASSWORD_ENDPOINT", "\"$userPasswordEndpoint\"")
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }

        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // signingConfig = signingConfigs.getByName("release")

            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")

    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Navigation compose
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Firebase
    /*implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")*/

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.32.0")

    // Gson
    implementation("com.google.code.gson:gson:2.10.1")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Logging interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.5.0")

    // WorkManager --- Comprobar si funcionan los workers despues de modificar :App (v 2.9.0 fallan)
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.1.0")

    // Webview
    implementation("com.google.accompanist:accompanist-webview:0.32.0")

    // Coil
    implementation("io.coil-kt:coil:2.6.0")
    implementation("io.coil-kt:coil-compose:2.6.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.05.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Mockito
    testImplementation("org.mockito:mockito-core:5.12.0")
}