package ru.alexoheah.newsreader.common

interface RefreshOwner {
    fun setRefreshState(refreshing: Boolean)
}