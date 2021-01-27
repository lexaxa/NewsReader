package ru.alexoheah.newsreader.model.news

fun News.toRemote(): Item {
    return Item(
        author = author,
        title = title,
        description = description,
        publishedOn = publishedOn,
        link = link
    )
}

fun Item.toStorage(): News {
    return News(
        author = author,
        title = title,
        description = description,
        publishedOn = publishedOn,
        link = link
    )
}
