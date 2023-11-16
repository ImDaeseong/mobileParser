package com.daeseong.htmlparser

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.webkit.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.jsoup.Jsoup

class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.java.simpleName

    private lateinit var webview: WebView
    private lateinit var tv: TextView
    private lateinit var button1: Button

    private var s1: String? = null
    private var s2: String? = null
    private var s3: String? = null
    private var s4: String? = null
    private var s5: String? = null

    private var num1: String? = null
    private var num2: String? = null
    private var num3: String? = null
    private var num4: String? = null
    private var num5: String? = null
    private var num6: String? = null
    private var num7: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview = findViewById(R.id.webview)
        tv = findViewById(R.id.tv)
        tv.movementMethod = ScrollingMovementMethod()

        val webSettings = webview.settings
        webSettings.javaScriptEnabled = true
        webview.addJavascriptInterface(JavaScriptInterface(), "Android")

        webSettings.setSupportZoom(false)//확대 축소 기능
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.builtInZoomControls = false

        webview.webViewClient = CustomWebViewClient()

        if (isConnect()) {
            webview.loadUrl("http://www.lotto.co.kr/article/list/AC01")
        } else {
            webview.loadUrl("about:blank")
        }

        button1 = findViewById(R.id.button1)
        button1.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun isConnect(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected && networkInfo.isAvailable
    }

    private fun getOnlyFileName(url: String): String? {
        var sResult = ""
        var nIndex = url.lastIndexOf("/")
        if (nIndex >= 0) {
            val sTemp = url.substring(nIndex + 1)
            nIndex = sTemp.lastIndexOf(".")
            if (nIndex >= 0) {
                sResult = sTemp.substring(0, nIndex)
            }
        } else {
            nIndex = url.lastIndexOf(".")
            if (nIndex >= 0) {
                sResult = url.substring(0, nIndex)
            }
        }
        return sResult
    }

    private fun isNumeric(sInput: String): Boolean {
        val regex = Regex("[\\d]*$")
        return sInput.matches(regex)
    }

    private inner class CustomWebViewClient : WebViewClient() {
        override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            super.onReceivedError(view, request, error)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            return super.shouldOverrideUrlLoading(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            return super.shouldOverrideUrlLoading(view, url)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            view?.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);")
            super.onPageFinished(view, url)
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }
    }

    private inner class JavaScriptInterface {
        @JavascriptInterface
        fun getHtml(html: String) {
            try {

                getData(html)

                s3 = String.format("%s %s %s %s %s %s %s", num1, num2, num3, num4, num5, num6, num7)
                val sValue1 = String.format("회차: %s \n", s1)
                val sValue2 = String.format("추첨일: %s \n", s2)
                val sValue3 = String.format("당첨번호: %s \n", s3)
                val sValue4 = String.format("1등 당첨자수 : %s \n", s4)
                val sValue5 = String.format("1등 당첨금액 : %s \n", s5)
                tv.text = sValue1 + sValue2 + sValue3 + sValue4 + sValue5

            } catch (ex: Exception) {
                ex.message.toString()
            }
        }

        private fun getData(html: String) {
            val document = Jsoup.parse(html)
            if (document != null) {
                val rowlist = document.select("ul").select("[class=wnr_cur_list]")
                if (rowlist != null) {
                    val liList = rowlist.select("li")
                    if (liList != null) {
                        val plist = liList[0].select("p")
                        if (plist != null) {
                            val alist = plist.select("a")
                            if (alist != null) {
                                var i = 0
                                val imglist = alist.select("img")
                                for (row in imglist) {
                                    val src = row.attr("src")
                                    if (isNumeric(getOnlyFileName(src)!!)) {
                                        when (i) {
                                            0 -> num1 = getOnlyFileName(src)
                                            1 -> num2 = getOnlyFileName(src)
                                            2 -> num3 = getOnlyFileName(src)
                                            3 -> num4 = getOnlyFileName(src)
                                            4 -> num5 = getOnlyFileName(src)
                                            5 -> num6 = getOnlyFileName(src)
                                            6 -> num7 = getOnlyFileName(src)
                                        }
                                        i++
                                    }
                                }
                            }

                            val spanlist = plist.select("span")
                            for ((i, row) in spanlist.withIndex()) {
                                when (i) {
                                    0 -> s1 = row.text()
                                    1 -> s2 = row.text()
                                    2 -> s3 = row.text()
                                    3 -> s4 = row.text()
                                    4 -> s5 = row.text()
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
