package com.advancednotes.domain.models

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.google.accompanist.web.AccompanistWebViewClient

class LocationWebViewClient(
    val url: String,
    val onPageStarted: () -> Unit,
    val onPageFinished: () -> Unit,
    val onReceivedError: (errorCode: Int?, description: String?) -> Unit,
    val onReceivedHttpError: (errorCode: Int?, description: String?) -> Unit,
) : AccompanistWebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (Uri.parse(request.url.toString()).host != Uri.parse(url).host) {
            Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString())).apply {
                view.context?.startActivity(this)
            }

            return false
        }

        return true
    }

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        onPageStarted()
    }

    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)
        onPageFinished()
    }

    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        onReceivedError(error?.errorCode, error?.description.toString())
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, error)
        onReceivedHttpError(error?.statusCode, error?.data.toString())
    }
}