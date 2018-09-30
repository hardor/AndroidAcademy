package ru.profapp.androidacademy.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import ru.profapp.androidacademy.Models.NewsItem
import ru.profapp.androidacademy.R
import java.text.SimpleDateFormat
import java.util.*


class NewsRecyclerViewAdapter(private val context: Context, private val news: List<NewsItem>, private val clickListener: OnItemClickListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_ITEM -> (holder as ItemViewHolder).bind(news[position])
            VIEW_TYPE_ITEM_WITHOUT_IMAGE -> (holder as WithoutImageViewHolder).bind(news[position])
        }

    }


    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val VIEW_TYPE_ITEM_WITHOUT_IMAGE = 2


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val imageOption = RequestOptions()
            .placeholder(R.drawable.ic_error_outline_black_24dp)
            .error(R.drawable.ic_error_outline_black_24dp)
            .centerCrop()
    private val imageLoader: RequestManager = Glide.with(context).applyDefaultRequestOptions(imageOption);

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> ItemViewHolder(inflater.inflate(R.layout.item_news, parent, false), clickListener)
            VIEW_TYPE_ITEM_WITHOUT_IMAGE -> WithoutImageViewHolder(inflater.inflate(R.layout.item_news_without_image, parent, false), clickListener)
            VIEW_TYPE_LOADING -> LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false))
            else -> LoadingViewHolder(inflater.inflate(R.layout.item_loading, parent, false))
        }

    }

    override fun getItemViewType(position: Int): Int {
        // We can check image on null
        return position % 3
    }

    override fun getItemCount(): Int {
        return news.size
    }


    interface OnItemClickListener {
        fun onItemClick(item: NewsItem)
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