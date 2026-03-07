package com.medcare.pillreminder

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true

        webView.loadData(
            """
            <html>
            <body style="font-family:sans-serif; padding:40px;">
                <h2>복약 알림 앱</h2>
                <p>앱 화면 연결 테스트 성공</p>
            </body>
            </html>
            """.trimIndent(),
            "text/html",
            "utf-8"
        )
    }
}
