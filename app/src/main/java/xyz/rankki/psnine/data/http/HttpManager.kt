package xyz.rankki.psnine.data.http

import com.blankj.utilcode.util.Utils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.ghui.fruit.Fruit
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import xyz.rankki.psnine.base.BaseModel
import xyz.rankki.psnine.data.http.interceptor.RequestInterceptor
import java.util.concurrent.TimeUnit


class HttpManager {

    companion object {
        private const val baseDomain: String = "192.168.0.100:5000"
        //        private const val baseDomain: String = "psnine.com"
        const val baseUrl: String = "http://$baseDomain"

        fun get(): HttpManager = Inner.instance
        fun init() {
            Inner.instance = HttpManager()
        }
    }

    private object Inner {
        lateinit var instance: HttpManager
    }

    interface Service {

        @GET("{path}")
        fun getWebPage(@Path("path") path: String): Observable<ResponseBody>

    }

    private var service: Service


    init {
        val cookieJar = PersistentCookieJar(SetCookieCache(),
                SharedPrefsCookiePersistor(Utils.getApp()))
        val okHttpClient: OkHttpClient = OkHttpClient()
                .newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(29, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .addInterceptor(RequestInterceptor())
//                .addNetworkInterceptor(NetworkInterceptor())
                .build()
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        service = retrofit.create(Service::class.java)
    }

    private fun <T> getWebPage(path: String, clz: Class<T>): Observable<T> {
        return service.getWebPage(path)
                .subscribeOn(Schedulers.io())
                .map { return@map it.string() }
                .map { return@map Fruit().fromHtml(it, clz) }
                .observeOn(AndroidSchedulers.mainThread())

    }

    fun <T> getTopics(path: String, clz: Class<T>): Observable<ArrayList<*>> {
        return getWebPage(path, clz)
                .subscribeOn(Schedulers.io())
                .map { return@map (it as BaseModel<*>).getItems() }
                .observeOn(AndroidSchedulers.mainThread())
    }

}