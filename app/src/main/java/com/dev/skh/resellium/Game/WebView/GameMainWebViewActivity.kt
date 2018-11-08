package com.dev.skh.resellium.Game.WebView

import android.databinding.DataBindingUtil
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityGameMainWebViewBinding

class GameMainWebViewActivity : InnerBaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> {
                end()
            }
        }
    }


    lateinit var binding: ActivityGameMainWebViewBinding
    private var uri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_main_web_view)
        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
        binding.layoutAppbar?.onClickListener = this
        binding.layoutAppbar?.title = "인터넷"
        val i = intent

        uri = i.getStringExtra("URI")
        binding.webview.settings.javaScriptEnabled = true
        if (uri != null)
            binding.webview.loadUrl(uri)

        binding.webview.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                shortToast("나중에 다시 시도해주세요")
            }

            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                super.onReceivedSslError(view, handler, error)
                shortToast("나중에 다시 시도해주세요")
            }

            override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                super.onReceivedHttpError(view, request, errorResponse)
                shortToast("나중에 다시 시도해주세요")
            }
        }


    }

    private fun end() {
        finish()
    }


}
