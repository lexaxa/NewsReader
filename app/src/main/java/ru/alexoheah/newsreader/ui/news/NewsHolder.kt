package ru.alexoheah.newsreader.ui.news

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.alexoheah.newsreader.R
import ru.alexoheah.newsreader.model.news.News
import ru.alexoheah.newsreader.utils.format
import java.util.*

class NewsHolder(var itemView: View): RecyclerView.ViewHolder(itemView) {

    var mTitle: TextView = itemView.findViewById(R.id.tv_title)
    var mAuthor: TextView = itemView.findViewById(R.id.tv_author)
    var mPublishedOn: TextView = itemView.findViewById(R.id.tv_published)

    fun bind(news: News, onItemClickListener: NewsAdapter.OnItemClickListener) {

        mTitle.text = news.title
        mAuthor.text = news.author
        mPublishedOn.text = Date(news.publishedOn).time.format()

        itemView.setOnClickListener { v ->
            onItemClickListener.onItemClick(
                news.hashCode()
            )
        }
    }
}
