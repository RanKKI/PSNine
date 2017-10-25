package club.ranleng.psnine.data.remote;

import com.blankj.utilcode.util.Utils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.KeyGetter;
import club.ranleng.psnine.common.RxBus;
import club.ranleng.psnine.data.interceptor.NetworkInterceptor;
import club.ranleng.psnine.data.interceptor.RequestInterceptor;
import club.ranleng.psnine.model.Topic;
import club.ranleng.psnine.model.TopicComment;
import club.ranleng.psnine.model.TopicsNormal;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.ghui.fruit.Fruit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class ApiManager {

    private static ClearableCookieJar cookieJar;
    private static ApiService apiService;

    private static volatile ApiManager defaultInstance;

    public ApiManager() {
        cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(Utils.getApp()));

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.cookieJar(cookieJar);
        builder.addInterceptor(new RequestInterceptor());
        builder.addNetworkInterceptor(new NetworkInterceptor());
        OkHttpClient okHttpClient = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://psnine.com/")
//                .baseUrl("http://192.168.0.4:5000/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiManager getDefault() {
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ApiManager();
                }
            }
        }
        return defaultInstance;
    }

    public Observable<TopicsNormal> getTopics(int type, int page) {
        Observable<ResponseBody> observable;
        if (type == Key.GENE || type == Key.QA) {
            throw new IllegalArgumentException("not support Gene and QA for now");
        }
        if (type == Key.TOPIC) {
            observable = apiService.getTopics(KeyGetter.getKEY(type), page);
        } else {
            observable = apiService.getTopicsWithNode(KeyGetter.getKEY(type), page);
        }
        return observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, TopicsNormal>() {
                    @Override
                    public TopicsNormal apply(ResponseBody responseBody) throws Exception {
                        TopicsNormal topicsNormal = new Fruit().fromHtml(responseBody.string(), TopicsNormal.class);
                        responseBody.close();
                        return topicsNormal;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Topic> getTopic(String id) {
        return apiService.getTopic(id)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Topic>() {
                    @Override
                    public Topic apply(ResponseBody responseBody) throws Exception {
                        Topic topic = new Fruit().fromHtml(responseBody.string(), Topic.class);
                        responseBody.close();
                        return topic;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<TopicComment> getTopicComment(String id, int page) {
        return apiService.getTopicComment(id, page)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, TopicComment>() {
                    @Override
                    public TopicComment apply(ResponseBody responseBody) throws Exception {
                        TopicComment comment = new Fruit().fromHtml(responseBody.string(), TopicComment.class);
                        responseBody.close();
                        return comment;
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
}
