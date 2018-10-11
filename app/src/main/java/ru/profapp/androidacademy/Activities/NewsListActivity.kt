package ru.profapp.androidacademy.Activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_news.*
import ru.profapp.androidacademy.Adapters.NewsRecyclerViewAdapter
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R
import ru.profapp.androidacademy.Utils.DataUtils


class NewsListActivity : AppCompatActivity() {

    private val clickListener = object : NewsRecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(item: NewsItem) {
            val intent = Intent(this@NewsListActivity, NewsDetailsActivity::class.java)
            intent.putExtra("item_news",item )
            startActivity(intent)

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)

        val list: RecyclerView = findViewById(R.id.recycler_news)
        list.adapter = NewsRecyclerViewAdapter(this, DataUtils.generateNews(), clickListener)

        val displayMode = resources.configuration.orientation
        if (displayMode == Configuration.ORIENTATION_PORTRAIT) {
            list.layoutManager = LinearLayoutManager(this)
        } else {
            list.layoutManager = GridLayoutManager(this,2)
        }


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

}
