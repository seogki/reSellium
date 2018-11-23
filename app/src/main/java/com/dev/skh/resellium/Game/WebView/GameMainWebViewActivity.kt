package com.dev.skh.resellium.Game.WebView

import android.annotation.SuppressLint
import android.databinding.DataBindingUtil
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.webkit.*
import com.dev.skh.resellium.Base.InnerBaseActivity
import com.dev.skh.resellium.R
import com.dev.skh.resellium.databinding.ActivityGameMainWebViewBinding

class GameMainWebViewActivity : InnerBaseActivity(), View.OnClickListener {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.layout_undo -> finish()
        }
    }

    lateinit var binding: ActivityGameMainWebViewBinding
    private var uri: String? = null
    private var webview: WebView? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBinding()
        uri = intent.getStringExtra("URI")
        setWebView()
    }

    private fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_game_main_web_view)
        binding.layoutAppbar?.layoutAdd?.visibility = View.GONE
        webview = binding.webview
        binding.layoutAppbar?.onClickListener = this
        binding.layoutAppbar?.title = "인터넷"
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setWebView() {

        uri?.let {
            webview?.apply {
                loadUrl(it)
                settings?.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {

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
        }
    }

    override fun onBackPressed() {

        when {
            webview == null -> super.onBackPressed()
            webview!!.canGoBack() -> webview?.goBack()
            else -> setConfirmDialog()
        }


    }

    private fun setConfirmDialog() {
        AlertDialog.Builder(this@GameMainWebViewActivity, R.style.MyDialogTheme)
                .setTitle("인터넷")
                .setMessage("창을 닫고 앱으로 돌아가시겠습니까?")
                .setPositiveButton("확인") { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }.setNegativeButton("취소") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

}
