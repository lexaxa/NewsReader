package ru.alexoheah.newsreader.ui.news

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.alexoheah.newsreader.R
import ru.alexoheah.newsreader.common.RefreshOwner
import ru.alexoheah.newsreader.common.Refreshable
import ru.alexoheah.newsreader.model.Storage
import ru.alexoheah.newsreader.model.news.News
import ru.alexoheah.newsreader.model.news.toStorage
import ru.alexoheah.newsreader.ui.newscontent.NewsContentActivity
import ru.alexoheah.newsreader.ui.newscontent.NewsContentFragment
import ru.alexoheah.newsreader.utils.ApiUtils

class NewsFragment: Fragment(), Refreshable, NewsAdapter.OnItemClickListener {

    lateinit var mRecyclerView: RecyclerView
    lateinit var mRefreshOwner: RefreshOwner
    lateinit var mErrorView: View
    lateinit var mStorage: Storage
    lateinit var mNewsAdapter: NewsAdapter
    lateinit var mDisposable: Disposable

    companion object {
        fun newInstance(): NewsFragment {
            val args = Bundle()

            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Storage.StorageOwner) {
            mStorage = context.obtainStorage()!!
        }

        if (context is RefreshOwner) {
            mRefreshOwner = context
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fr_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRecyclerView = view.findViewById<RecyclerView>(R.id.recycler)
        mErrorView = view.findViewById<View>(R.id.errorView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.title = "News"

        mNewsAdapter = NewsAdapter(this)
        mRecyclerView.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = mNewsAdapter
        }

        onRefreshData();
    }

    override fun onItemClick(newsId: Int) {
        val intent = Intent(activity, NewsContentActivity::class.java)
        val args = Bundle()
        args.putInt(NewsContentFragment.NEWSCONTENT_KEY, newsId)
        intent.putExtra(NewsContentActivity.NEWS_KEY, args)
        startActivity(intent)
    }

    override fun onDetach() {
        mDisposable.dispose()
        super.onDetach()
    }

    override fun onRefreshData() {
        getNews()
    }

    private fun getNews() {
        mDisposable = ApiUtils.getApiService().getNews()
            .doOnSuccess { response ->
                mStorage.insertNews(response)
            }
            .onErrorReturn { throwable ->
                if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.javaClass)) mStorage.getNews() else null
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { disposable -> mRefreshOwner.setRefreshState(true) }
            .doFinally { mRefreshOwner.setRefreshState(false) }
            .subscribe(
                { response ->
                    mErrorView.visibility = View.GONE
                    mRecyclerView.visibility = View.VISIBLE
                    response.channel?.items?.map { it.toStorage() }.let { mNewsAdapter.addData(it as MutableList<News>, true) }
                }
            ) { throwable ->
                mErrorView.visibility = View.VISIBLE
                mRecyclerView.visibility = View.GONE
            }
    }
}
