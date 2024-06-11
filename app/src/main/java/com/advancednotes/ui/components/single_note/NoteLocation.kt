package com.advancednotes.ui.components.single_note

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.advancednotes.BuildConfig
import com.advancednotes.R
import com.advancednotes.domain.models.LocationWebViewClient
import com.advancednotes.domain.models.MyLocation
import com.advancednotes.ui.components.buttons.MyIconButton
import com.advancednotes.ui.components.buttons.MyTextButtonAnimated
import com.google.accompanist.web.WebView
import com.google.accompanist.web.WebViewState
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun NoteLocation(
    myLocation: MyLocation,
    onDeleteLocation: () -> Unit
) {
    val context: Context = LocalContext.current

    val url =
        "${BuildConfig.BASE_URL}/${BuildConfig.UI_MAP_ENDPOINT}/?latitude=${myLocation.latitude}&longitude=${myLocation.longitude}"

    val webViewState: WebViewState = rememberWebViewState(
        url,
        mapOf(
            Pair("Client-ID", BuildConfig.CLIENT_ID)
        )
    )

    var webViewError: Boolean by remember { mutableStateOf(false) }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            MyTextButtonAnimated(
                text = stringResource(id = R.string.view_in_map),
                onClick = {
                    val gmmIntentUri: Uri =
                        Uri.parse("geo:${myLocation.latitude},${myLocation.longitude}?q=${myLocation.latitude},${myLocation.longitude}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    mapIntent.resolveActivity(context.packageManager)?.let {
                        context.startActivity(mapIntent)
                    }
                },
                icon = Icons.Filled.Map,
            )

            Spacer(modifier = Modifier.weight(1f))

            MyIconButton(
                icon = Icons.Filled.Delete,
                onClick = onDeleteLocation,
                contentDescription = null
            )
        }

        if (!webViewError) {
            WebView(
                state = webViewState,
                modifier = Modifier
                    .height(220.dp)
                    .clip(MaterialTheme.shapes.small),
                captureBackPresses = false,
                onCreated = {
                    it.settings.javaScriptEnabled = true
                },
                client = remember {
                    LocationWebViewClient(
                        url = url,
                        onPageStarted = {},
                        onPageFinished = {},
                        onReceivedError = { _, _ ->
                            webViewError = true
                        },
                        onReceivedHttpError = { _, _ ->
                            // Aqui tira siempre 401 (com.google.accompanist:accompanist-webview:0.32.0)
                            //webViewError = true
                        }
                    )
                },
            )
        }
    }
}