package ru.alexoheah.newsreader.ui.newscontent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.alexoheah.newsreader.R
import ru.alexoheah.newsreader.common.RefreshOwner
import ru.alexoheah.newsreader.common.Refreshable
import ru.alexoheah.newsreader.model.Storage
import ru.alexoheah.newsreader.model.news.News
import ru.alexoheah.newsreader.model.news.toStorage
import ru.alexoheah.newsreader.utils.ApiUtils
import ru.alexoheah.newsreader.utils.format
import java.util.*

class NewsContentFragment: Fragment(), Refreshable {

    private var mNewsId: Int = 0
    private var mStorage: Storage? = null
    private var mErrorView: View? = null
    private var mDisposable: Disposable? = null
    private var mRefreshOwner: RefreshOwner? = null

    private var mNewsContentView: View? = null
    private var mNewsContentLink: TextView? = null
    private var mNewsContentTitle: TextView? = null
    private var mNewsContentAuthor: TextView? = null
    private var mNewsContentPublishedOn: TextView? = null
    private var mNewsContentDescription: TextView? = null

    companion object {
        val NEWSCONTENT_KEY = "NEWSCONTENT_KEY"

        fun newInstance(args: Bundle): NewsContentFragment {
            val fragment = NewsContentFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mStorage = if (context is Storage.StorageOwner) (context as Storage.StorageOwner).obtainStorage() else null
        mRefreshOwner = if (context is RefreshOwner) context else null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_newscontent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mErrorView = view.findViewById(R.id.errorView)
        mNewsContentView = view.findViewById(R.id.view_newscontent)
        mNewsContentAuthor = view.findViewById(R.id.tv_author_details)
        mNewsContentPublishedOn = view.findViewById(R.id.tv_published_details)
        mNewsContentDescription = view.findViewById(R.id.tv_description_details)
        mNewsContentTitle = view.findViewById(R.id.tv_title_details)
        mNewsContentLink = view.findViewById(R.id.tv_link_details)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mNewsId = arguments?.getInt(NEWSCONTENT_KEY) ?: 0
        activity?.title = "News"

        mNewsContentView?.visibility = View.VISIBLE

        onRefreshData();
    }

    override fun onRefreshData() {
        getNewsContent()
    }

    private fun getNewsContent() {
        mDisposable = ApiUtils
            .getApiService()
            .getNews()
            .subscribeOn(Schedulers.io())
            .doOnSuccess { response -> mStorage?.insertNews(response) }
            .onErrorReturn { throwable ->
                if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.javaClass)) mStorage?.getNews(

                ) else null
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { disposable -> mRefreshOwner!!.setRefreshState(true) }
            .doFinally { mRefreshOwner!!.setRefreshState(false) }
            .subscribe(
                { response ->
                    mErrorView!!.visibility = View.GONE
                    mNewsContentView!!.visibility = View.VISIBLE
                    response.channel?.items?.find { it.hashCode() == mNewsId }?.toStorage()?.let { bind(it) }
                }
            ) { throwable ->
                mErrorView!!.visibility = View.VISIBLE
                mNewsContentView!!.visibility = View.GONE
            }
    }

    private fun bind(news: News) {
        mNewsContentAuthor?.text = news.author
        mNewsContentTitle?.text = news.title
        mNewsContentLink?.text = news.link
        mNewsContentPublishedOn?.text = Date(news.publishedOn).time.format()
        mNewsContentDescription?.text = news.description
    }

    override fun onDetach() {
        mStorage = null
        mRefreshOwner = null
        mDisposable?.dispose()
        super.onDetach()
    }

}
