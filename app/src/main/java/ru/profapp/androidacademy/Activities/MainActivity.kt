package ru.profapp.androidacademy.Activities

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import ru.profapp.androidacademy.Adapters.NewsRecyclerViewAdapter
import ru.profapp.androidacademy.Adapters.OnLoadMoreListener
import ru.profapp.androidacademy.Data.network.RestApi
import ru.profapp.androidacademy.Data.network.State
import ru.profapp.androidacademy.Data.network.dto.NyNewsDTO
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var newsRecyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var newsList: MutableList<NewsItem?> = mutableListOf()
    private var page: Int = 1
    private lateinit var newsAdapter: NewsRecyclerViewAdapter
    var request: Disposable? = null
    lateinit var spinner: Spinner
    private lateinit var btnTryAgain: Button
    private lateinit var viewError: View
    private lateinit var viewLoading: View
    private lateinit var viewNoData: View
    private lateinit var tvError: TextView

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

        btnTryAgain = findViewById(R.id.btn_try_again)
        btnTryAgain.setOnClickListener {
            refreshNews()
        }
        viewError = findViewById(R.id.lt_error)
        viewLoading = findViewById(R.id.lt_loading)
        viewNoData = findViewById(R.id.lt_no_data)
        tvError = findViewById(R.id.tv_error)

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
            refreshNews()
        }

        // loadNews()

    }

    private fun refreshNews() {
        swipeRefreshLayout.isRefreshing = true
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
                    checkResponseAndShowState(it)

                }, { error ->
                    handleError(error)
                    Log.e("loadNews", error.message)
                })

    }

    private fun checkResponseAndShowState(response: Response<NyNewsDTO>) {

        if (!response.isSuccessful) {
            showState(State.ServerError)
            return
        }

        val data = response.body()
        if (data == null) {
            showState(State.HasNoData)
            return
        }


        if (data.results?.isEmpty()!!) {
            showState(State.HasNoData)
            return
        }

        val prevSize = newsList.size
        newsList.add(NewsItem("Page $page", null, "Title"))
        newsList.addAll(
                data.results!!.map { r ->
                    NewsItem(
                            r.title,
                            r.multimedia?.lastOrNull()?.url,
                            r.subsection,
                            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault()).parse(r.publishedDate),
                            r.abstract,
                            r.url

                    )
                }
        )
        newsAdapter.notifyItemRangeInserted(prevSize, data.results!!.size + 1)
        page++
        showState(State.HasData)
    }

    private fun handleError(throwable: Throwable) {
        if (throwable is IOException) {
            showState(State.NetworkError)
            return
        }
        showState(State.ServerError)
    }

    fun showState(state: State) {

        when (state) {
            State.HasData -> {
                viewError.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                newsRecyclerView.visibility = View.VISIBLE
            }

            State.HasNoData -> {
                newsRecyclerView.visibility = View.GONE
                viewLoading.visibility = View.GONE

                viewError.visibility = View.VISIBLE
                viewNoData.visibility = View.VISIBLE
            }
            State.NetworkError -> {
                newsRecyclerView.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                tvError.text = getText(R.string.error_network)
                viewError.visibility = View.VISIBLE
            }

            State.ServerError -> {
                newsRecyclerView.visibility = View.GONE
                viewLoading.visibility = View.GONE
                viewNoData.visibility = View.GONE

                tvError.text = getText(R.string.error_server)
                viewError.visibility = View.VISIBLE
            }
            State.Loading -> {
                viewError.visibility = View.GONE
                newsRecyclerView.visibility = View.GONE
                viewNoData.visibility = View.GONE

                viewLoading.visibility = View.VISIBLE
            }

            else -> throw IllegalArgumentException("Unknown state: $state")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_title_spinner, menu)

        val item = menu.findItem(R.id.spinner_news_list)
        spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.articles_NY_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented")
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                refreshNews()
            }

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
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
