package ru.profapp.androidacademy.Activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.profapp.androidacademy.Adapters.NewsRecyclerViewAdapter
import ru.profapp.androidacademy.Adapters.OnLoadMoreListener
import ru.profapp.androidacademy.Models.Category
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R
import ru.profapp.androidacademy.Utils.DataUtils

class NewsListActivity : AppCompatActivity() {

    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var newsList: MutableList<NewsItem?> = mutableListOf()
    private var page: Int = 1
    private lateinit var newsAdapter: NewsRecyclerViewAdapter
    var request: Disposable? = null

    private val clickListener = object : NewsRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(item: NewsItem?) {
            if (item != null) {
                val intent = Intent(this@NewsListActivity, NewsDetailsActivity::class.java)
                intent.putExtra("item_news", item)
                startActivity(intent)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN)
        newsRecyclerView = findViewById(R.id.recycler_news)

        val displayMode = resources.configuration.orientation
        if (displayMode == Configuration.ORIENTATION_PORTRAIT) {
            newsRecyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            newsRecyclerView.layoutManager = GridLayoutManager(this, 2)
        }

        newsAdapter = NewsRecyclerViewAdapter(newsRecyclerView, this, newsList, clickListener)

        newsAdapter.onLoadMoreListener = object : OnLoadMoreListener {
            override fun onLoadMore() {
                loadNews()
            }
        }

        newsRecyclerView.adapter = newsAdapter

        swipeRefreshLayout.setOnRefreshListener {
            page = 0
            val prevSize = newsList.size
            newsList.clear()
            newsAdapter.notifyItemRangeRemoved(0, prevSize)
            loadNews()
        }

        loadNews()

    }

    private fun loadNews() {
        swipeRefreshLayout.isRefreshing = true
        request = Observable.fromArray(DataUtils.generateNews())

                .map {
                    Thread.sleep(2000)
                    return@map it
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    swipeRefreshLayout.isRefreshing = false
                    newsAdapter.isLoading = false
                }
                .subscribe({
                    val prevSize = newsList.size
                    newsList.add(
                            NewsItem("Page $page", null, Category(0, "Title"))
                    )
                    newsList.addAll(it)
                    newsAdapter.notifyItemRangeInserted(prevSize, it.size + 1)
                    page++
                }, { error ->
                    Log.e("loadNews", error.message)
                })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_switch -> {
                //  startActivity(Intent(this, ActorRecyclerActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.dispose()
    }

}
