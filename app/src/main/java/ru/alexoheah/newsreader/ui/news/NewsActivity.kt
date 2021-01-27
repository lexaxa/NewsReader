package ru.alexoheah.newsreader.ui.news

import androidx.fragment.app.Fragment
import ru.alexoheah.newsreader.common.SingleFragmentActivity

class NewsActivity : SingleFragmentActivity() {

    override fun getFragment(): Fragment = NewsFragment.newInstance()


}