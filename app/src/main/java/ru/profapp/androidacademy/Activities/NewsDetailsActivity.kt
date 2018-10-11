package ru.profapp.androidacademy.Activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R
import java.text.SimpleDateFormat
import java.util.*

class NewsDetailsActivity : AppCompatActivity() {
    private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_error_outline_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .centerCrop()
    private lateinit var imageLoader: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)

        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val news = intent.getParcelableExtra<NewsItem>("item_news")
        this.title = news.category.name

        val imageView: ImageView = findViewById(R.id.news_image)
        val title: TextView = findViewById(R.id.title)
        val content: TextView = findViewById(R.id.content)
        val date: TextView = findViewById(R.id.date)

        title.text= news.title
        content.text=news.fullText
        date.text = SimpleDateFormat(getString(R.string.date_string), Locale.getDefault()).format(news.publishDate)
        imageLoader = Glide.with(this).applyDefaultRequestOptions(imageOption)
        imageLoader.load(news.imageUrl).into(imageView)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}