package ru.profapp.androidacademy.Activities

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R

class NewsDetailsActivity : AppCompatActivity() {

    lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val news = intent.getParcelableExtra<NewsItem>("item_news")
        title = news.category

        webView = findViewById(R.id.wbV_news)

        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false
        webView.loadUrl(news.url)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}