package ru.alexoheah.newsreader.ui.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.alexoheah.newsreader.R
import ru.alexoheah.newsreader.model.news.Item
import ru.alexoheah.newsreader.model.news.News
import ru.alexoheah.newsreader.model.news.toRemote
import ru.alexoheah.newsreader.model.news.toStorage

class NewsAdapter(var onItemClickListener: OnItemClickListener):RecyclerView.Adapter<NewsHolder>() {

    var mNews: MutableList<News> = ArrayList<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.li_news, parent, false)
        return NewsHolder(view)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val news: Item = mNews[position].toRemote()
        holder.bind(news.toStorage(), onItemClickListener)
    }

    override fun getItemCount(): Int = mNews.size

    fun addData(data: MutableList<News>, isRefreshed: Boolean) {
        if (isRefreshed) {
            mNews.clear()
        }

        if (data != null) {
            mNews.addAll(data)
        }
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onItemClick(newsId: Int)
    }
}
