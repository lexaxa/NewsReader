package ru.alexoheah.newsreader.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import ru.alexoheah.newsreader.AppDelegate
import ru.alexoheah.newsreader.R
import ru.alexoheah.newsreader.model.Storage

abstract class SingleFragmentActivity: AppCompatActivity(), Storage.StorageOwner,
    SwipeRefreshLayout.OnRefreshListener, RefreshOwner {

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_swipe_container)
        mSwipeRefreshLayout = findViewById(R.id.refresher)
        mSwipeRefreshLayout.setOnRefreshListener(this)

        if (savedInstanceState == null) {
            changeFragment(getFragment())
        }
    }

    protected fun getLayout(): Int = R.layout.container

    protected abstract fun getFragment(): Fragment


    fun changeFragment(fragment: Fragment) {
        val addToBackStack = supportFragmentManager.findFragmentById(R.id.fragmentContainer) != null
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)

        if (addToBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }

        transaction.commit()

    }

    override fun onRefresh() {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment is Refreshable) {
            fragment.onRefreshData()
        } else {
            setRefreshState(false)
        }
    }

    override fun obtainStorage(): Storage? {
        return (applicationContext as AppDelegate).getStorage()
    }

    override fun setRefreshState(refreshing: Boolean) {
        mSwipeRefreshLayout.post { mSwipeRefreshLayout.isRefreshing = refreshing }
    }
}
