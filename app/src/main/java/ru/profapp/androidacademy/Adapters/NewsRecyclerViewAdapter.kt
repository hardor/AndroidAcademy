package ru.profapp.androidacademy.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R
import java.text.SimpleDateFormat
import java.util.*

class NewsRecyclerViewAdapter(private val recyclerView: RecyclerView, private val context: Context, private val news: List<NewsItem?>, private val clickListener: OnItemClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ItemType(val flag: Int) {
        ITEM(0),
        WITHOUT_IMAGE(1),
        TITLE(2),
        LOADING(3);
    }

    var isLoading: Boolean = false
    var pastVisibleItems: Int = 0
    var lastVisibleItem: Int = 0
    var totalItemCount: Int = 0
    var onLoadMoreListener: OnLoadMoreListener? = null

    init {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                if (dy > 0) {
                    lastVisibleItem = linearLayoutManager.childCount
                    totalItemCount = linearLayoutManager.itemCount
                    pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading) {
                        if (lastVisibleItem + pastVisibleItems >= totalItemCount) {

                            if (onLoadMoreListener != null) {
                                isLoading = true
                                onLoadMoreListener!!.onLoadMore()
                            } else {
                                isLoading = false
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ItemType.ITEM.flag -> (holder as ItemViewHolder).bind(news[position]!!)
            ItemType.WITHOUT_IMAGE.flag -> (holder as WithoutImageViewHolder).bind(news[position]!!)
            ItemType.TITLE.flag -> (holder as TitleViewHolder).bind(news[position]!!)
        }

    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_error_outline_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .centerCrop()
    private val imageLoader: RequestManager = Glide.with(context).applyDefaultRequestOptions(imageOption)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.ITEM.flag -> ItemViewHolder(inflater.inflate(R.layout.item_news, parent, false), clickListener)
            ItemType.WITHOUT_IMAGE.flag -> WithoutImageViewHolder(inflater.inflate(R.layout.item_news_without_image, parent, false), clickListener)
            ItemType.TITLE.flag -> TitleViewHolder(inflater.inflate(R.layout.item_title, parent, false))
            ItemType.LOADING.flag -> LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false))
            else -> LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false))
        }

    }

    override fun getItemViewType(position: Int): Int {
        val item = news[position]

        return when {
            item == null -> ItemType.LOADING.flag
            item.category.name == "Title" -> ItemType.TITLE.flag
            item.imageUrl.isNullOrBlank() -> ItemType.WITHOUT_IMAGE.flag
            else -> ItemType.ITEM.flag
        }

    }

    override fun getItemCount(): Int {
        return news.size
    }

    interface OnItemClickListener {
        fun onItemClick(item: NewsItem?)
    }

    inner class ItemViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.image)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val preview: TextView = itemView.findViewById(R.id.preview)
        private val date: TextView = itemView.findViewById(R.id.date)

        init {
            itemView.setOnClickListener { view ->
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(news[position])
                }
            }

        }

        fun bind(item: NewsItem) {
            imageLoader.load(item.imageUrl).into(imageView)
            title.text = item.title
            preview.text = item.previewText
            category.text = item.category.name
            date.text = SimpleDateFormat(context.getString(R.string.date_string), Locale.getDefault()).format(item.publishDate)
        }
    }

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)

    }

    //Just for example

    inner class TitleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val title: TextView = itemView.findViewById(R.id.title)

        fun bind(item: NewsItem) {
            title.text = item.title
        }

    }

    inner class WithoutImageViewHolder(itemView: View, listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {

        private val category: TextView = itemView.findViewById(R.id.category)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val preview: TextView = itemView.findViewById(R.id.preview)
        private val date: TextView = itemView.findViewById(R.id.date)

        init {
            itemView.setOnClickListener { view ->
                val position = adapterPosition
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(news[position])
                }
            }

        }

        fun bind(item: NewsItem) {
            title.text = item.title
            preview.text = item.previewText
            category.text = item.category.name
            date.text = SimpleDateFormat(context.getString(R.string.date_string), Locale.getDefault()).format(item.publishDate)
        }

    }
}