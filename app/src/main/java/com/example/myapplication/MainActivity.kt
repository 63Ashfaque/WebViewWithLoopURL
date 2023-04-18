package com.example.myapplication

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextUInt


class MainActivity : AppCompatActivity() {


    private var nLoop: Int = 0
    private var nSecond: Long = 0
    var nUrl: String = ""
    private var autoState: Boolean = true
    private val sharedPrefFile = "kotlinsharedpreference"
    private var i: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val edURl = findViewById<EditText>(R.id.edURl)
        val edLoop = findViewById<EditText>(R.id.edLoop)
        val edPreSecond = findViewById<EditText>(R.id.edPreSecond)
        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = false
        webView.settings.setGeolocationEnabled(false)
        webView.settings.allowContentAccess = false
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.setPluginState(WebSettings.PluginState.OFF)

        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(false)
        cookieManager.setAcceptThirdPartyCookies(webView, false)


        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        edURl.setText(sharedPreferences.getString("nUrl", "https://www.geeksforgeeks.org/"))

        val btnAuto = findViewById<Button>(R.id.btnAuto)
        btnAuto.setOnClickListener {

            if (autoState) {
                btnAuto.text = "Stop"
                autoState = false
            } else {
                btnAuto.text = "Start"
                autoState = true
            }

            if (edURl.text.isNotEmpty()) {
                nUrl = edURl.text.toString()
                editor.putString("nUrl", nUrl)
                editor.apply()
                webViewLoadUrl(webView, nUrl)
            }

            // Set web view client
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    if (btnAuto.text == "Stop") {
                        Log.d("Ashu: ", "$i #$nUrl")
                        i++
                        webViewLoadUrl(webView, nUrl)
                    }

                }
            }

        }

        val btnSend = findViewById<Button>(R.id.btnSend)
        btnSend.setOnClickListener {


            nLoop = if (edLoop.text.isNotEmpty()) {
                edLoop.text.toString().toInt()
            } else {
                10
            }

            nSecond = if (edPreSecond.text.isNotEmpty()) {
                edPreSecond.text.toString().toLong()
            } else {
                1000
            }
            if (edURl.text.isNotEmpty()) {
                nUrl = edURl.text.toString()
                GlobalScope.launch {
                    urlLooper(webView, nUrl, nLoop, nSecond)
                }

            }

        }

    }

    private suspend fun urlLooper(webView: WebView, nUrl: String, nLoop: Int, nSecond: Long) {

        for (i in 0..nLoop) {
            webViewLoadUrl(webView, nUrl)
            Log.d("Ashu: ", "$nUrl #$i")
            delay(nSecond) // Delay for 1 second
        }

    }

    private fun webViewLoadUrl(webView: WebView, nUrl: String) {

        val randomNum = Random.nextInt(8, 13)
        val randomNum1 = Random.nextInt(40, 59)

        val randomNum2 = Random.nextFloat()

        val randomNum3 = Random.nextDouble(888.888, 999.999)
        val randomNum4 = Random.nextDouble(499.0, 599.0).toFloat()

        val randomChar1 = ('A'..'Z').random()
        val randomChar2 = ('A'..'Z').random()
        val randomChar3 = ('A'..'Z').random()

        //"Mozilla/5.0 (Linux; Android 10; SM-G970U) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.101 Mobile Safari/537.36"
        val userAgentString = "Mozilla/${randomNum1 + randomNum2}" +
                "(Linux; Android $randomNum; $randomChar1$randomChar2-$randomChar3$randomNum1$randomChar2$randomChar1) " +
                "AppleWebKit/$randomNum4" +
                "(KHTML, like Gecko) Chrome/$randomNum3.$randomNum1 Mobile " +
                "Safari/$randomNum4"
        Log.d("Ashu", "$userAgentString")

        webView.settings.userAgentString = userAgentString
        webView.loadUrl(nUrl)

    }


}