package ru.alexoheah.newsreader.ui.newscontent

import androidx.fragment.app.Fragment
import ru.alexoheah.newsreader.common.SingleFragmentActivity
import java.lang.IllegalStateException

class NewsContentActivity: SingleFragmentActivity() {

    companion object {
        val NEWS_KEY = "NEWS_KEY"
    }

    override fun getFragment(): Fragment {
        if(intent != null) {
            return NewsContentFragment.newInstance(intent.getBundleExtra(NEWS_KEY)!!)
        }
        throw IllegalStateException("getIntent cannot be null")
    }
}