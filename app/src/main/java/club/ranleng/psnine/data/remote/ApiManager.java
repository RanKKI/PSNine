package club.ranleng.psnine.data.remote;

import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.concurrent.TimeUnit;

import club.ranleng.psnine.common.KeyGetter;
import club.ranleng.psnine.common.RxBus;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.data.interceptor.NetworkInterceptor;
import club.ranleng.psnine.data.interceptor.RequestInterceptor;
import club.ranleng.psnine.data.module.Callback;
import club.ranleng.psnine.data.module.TopicCommentCallback;
import club.ranleng.psnine.model.HttpRequest;
import club.ranleng.psnine.model.TopicComment;
import club.ranleng.psnine.model.UserInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.ghui.fruit.Fruit;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ApiManager {

    private static ClearableCookieJar cookieJar;
    private static ApiService apiService;

    private static volatile ApiManager defaultInstance;

    private ApiManager() {
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

    public ApiService getApiService() {
        return apiService;
    }

    public void logout() {
        cookieJar.clear();
        cookieJar.clearSession();
        UserState.setLogin(false);
        UserState.setUserInfo(null);
        RxBus.getDefault().send(new UserInfo());
    }

    public Observable<TopicComment> getTopicComment(int type, String id, int page) {
        return apiService.getTopicComment(KeyGetter.getPath(type), id, page)
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

    public void Login(final Callback callback, String username, String password) {
        FormBody body = new FormBody.Builder()
                .add("psnid", username)
                .add("pass", password)
                .add("jump", "http://psnine.com/")
                .build();
        apiService.Login(body)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, HttpRequest>() {
                    @Override
                    public HttpRequest apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        responseBody.close();
                        int code = 211;
                        if (result.equals("success")) {
                            code = 200;
                        }
                        return new HttpRequest(result, code);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpRequest>() {
                    @Override
                    public void accept(HttpRequest httpRequest) throws Exception {
                        if (httpRequest.getCode() == 200) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure();
                            ToastUtils.showShort(httpRequest.getMessage());
                        }
                    }
                });
    }

    public void Signin() {
        apiService.Signin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        ToastUtils.showShort("签到成功");
                    }
                });
    }

    public void setReply(final TopicCommentCallback callback, int type, String id, String content) {
        FormBody.Builder body = new FormBody.Builder();
        body.add("type", KeyGetter.getGeneOrTopic(type))
                .add("param", id)
                .add("old", "yes")
                .add("com", "")
                .add("content", content);
        apiService.Reply(body.build())
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, HttpRequest>() {
                    @Override
                    public HttpRequest apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        responseBody.close();
                        int code = 211;
                        if (result.contains("div")) {
                            code = 200;
                        }
                        return new HttpRequest(result, code);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HttpRequest>() {
                    @Override
                    public void accept(HttpRequest httpRequest) throws Exception {
                        if (httpRequest.getCode() == 200) {
                            callback.onSuccess(new Fruit().fromHtml(httpRequest.getMessage(), TopicComment.Comment.class));
                        } else {
                            callback.onFailure();
                            ToastUtils.showShort(httpRequest.getMessage());
                        }
                    }
                });

    }
}
