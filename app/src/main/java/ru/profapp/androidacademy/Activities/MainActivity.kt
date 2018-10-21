package ru.profapp.androidacademy.Activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.profapp.androidacademy.Adapters.NewsRecyclerViewAdapter
import ru.profapp.androidacademy.Adapters.OnLoadMoreListener
import ru.profapp.androidacademy.Data.network.RestApi
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R


class MainActivity : AppCompatActivity() {

    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var newsList: MutableList<NewsItem?> = mutableListOf()
    private var page: Int = 1
    private lateinit var newsAdapter: NewsRecyclerViewAdapter
    var request: Disposable? = null
    lateinit var spinner: Spinner

    private val clickListener = object : NewsRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(item: NewsItem?) {
            if (item != null) {
                val intent = Intent(this@MainActivity, NewsDetailsActivity::class.java)
                intent.putExtra("item_news", item)
                startActivity(intent)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            refresh_news()
        }


        // loadNews()

    }

    private fun refresh_news() {
        page = 0
        val prevSize = newsList.size
        newsList.clear()
        newsAdapter.notifyItemRangeRemoved(0, prevSize)
        loadNews()
    }

    private fun loadNews() {
        swipeRefreshLayout.isRefreshing = true
        request = RestApi.getNews(spinner.selectedItem.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    swipeRefreshLayout.isRefreshing = false
                    newsAdapter.isLoading = false
                }
                .subscribe({
                    val prevSize = newsList.size
                    newsList.add(
                            NewsItem("Page $page", null, "Title")
                    )
                    newsList.addAll(it)
                    newsAdapter.notifyItemRangeInserted(prevSize, it.size + 1)
                    page++
                }, { error ->
                    Log.e("loadNews", error.message)
                })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_title_spinner, menu)

        val item = menu.findItem(R.id.spinner_news_list)
        spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.articles_NY_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    swipeRefreshLayout.isRefreshing = true
                    refresh_news()
            }

        }
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
