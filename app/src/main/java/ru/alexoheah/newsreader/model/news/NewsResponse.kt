package ru.alexoheah.newsreader.model.news

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import java.io.Serializable

@Root(name = "rss", strict = false)
data class NewsResponse @JvmOverloads constructor(
    @field:Element(name = "channel")
    var channel: RssChannel? = null
): Serializable

@Root(name = "channel", strict = false)
data class RssChannel @JvmOverloads constructor(
    @field:ElementList(
        name = "item",
        inline = true,
        required = false
    ) var items: MutableList<Item>? = null
): Serializable

@Root(name = "item", strict = false)
data class Item @JvmOverloads constructor(
    @field:Element(name = "title") var title: String = "",
    @field:Element(name = "description") var description: String = "",
    @field:Element(name = "link") var link: String = "",
    @field:Element(name = "pubDate") var publishedOn: String = "",
    @field:Element(name = "author") var author: String = ""
    //    ,
    //    @Embedded(prefix = "enclosure")
    //    @SerializedName("enclosure")
    //    @field:ElementList(name = "enclosure", required = false)
    //    var enclosure: String = ""
    ): Serializable
