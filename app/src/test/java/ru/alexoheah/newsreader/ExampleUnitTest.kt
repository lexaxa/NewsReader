package ru.alexoheah.newsreader

import org.junit.Assert.*
import org.junit.Test
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import ru.alexoheah.newsreader.model.news.News
import java.text.SimpleDateFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

}



