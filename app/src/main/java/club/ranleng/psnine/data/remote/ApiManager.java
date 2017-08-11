package club.ranleng.psnine.data.remote;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.RxBus;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.data.moudle.SimpleCallBack;
import club.ranleng.psnine.data.moudle.SimpleReturn;
import club.ranleng.psnine.data.moudle.SimpleSubCallBack;
import club.ranleng.psnine.data.moudle.SimpleSubscriber;
import club.ranleng.psnine.utils.HTML.ConvertHtml;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiManager {

    private static Retrofit retrofit;
    private static ClearableCookieJar cookieJar;
    private static ApiService apiService;

    private static volatile ApiManager defaultInstance;

    public ApiManager() {
        cookieJar = new PersistentCookieJar(new SetCookieCache(),
                new SharedPrefsCookiePersistor(Utils.getContext()));

        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.connectTimeout(29, TimeUnit.SECONDS);
        builder.cookieJar(cookieJar);
        OkHttpClient okHttpClient = builder.build();

        retrofit = new Retrofit.Builder()
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

    public static void clear() {
        cookieJar.clear();
        cookieJar.clearSession();
        defaultInstance = null;
    }

    public void getTopics(SimpleSubCallBack<Map<String, Object>> callBack, final int type, String search, String ele, int page) {
        Observable<ResponseBody> observable = null;
        if (type == KEY.TOPIC || type == KEY.GENE || type == KEY.QA) {
            observable = apiService.getTopic(KEY.getTypeName(type), KEY.PREF_OB, search, ele, page);
        } else if (type == KEY.GUIDE || type == KEY.PLUS || type == KEY.OPENBOX) {
            observable = apiService.getNode(KEY.getTypeName(type), KEY.PREF_OB, search, page);
        } else if (type == KEY.NOTICE) {
            observable = apiService.getNotice();
        } else if (type == KEY.GENE_FAV || type == KEY.TOPIC_FAV) {
            observable = apiService.getFav(KEY.TopicOrGene(type));
        }

        assert observable != null;
        observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ConvertHtml.getTopics(responseBody.string(), type);
                    }
                })
                .flatMapIterable(new Function<ArrayList<Map<String, Object>>, Iterable<Map<String, Object>>>() {
                    @Override
                    public Iterable<Map<String, Object>> apply(@NonNull ArrayList<Map<String, Object>> maps) throws Exception {
                        return maps;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<>(callBack));
    }

    public void getTopic(SimpleSubCallBack<Map<String, Object>> callBack, final int type, int topic_id, int page) {
        Observable<ResponseBody> call;
        if (type == KEY.GENE) {
            call = apiService.getGene(topic_id);
        } else if (type == KEY.QA) {
            call = apiService.getQA(topic_id);
        } else {
            call = apiService.getArticle(topic_id, page);
        }
        call.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {

                        return ConvertHtml.parseArticle(responseBody.string(), type);
                    }
                })
                .flatMapIterable(new Function<ArrayList<Map<String, Object>>, Iterable<Map<String, Object>>>() {
                    @Override
                    public Iterable<Map<String, Object>> apply(@NonNull ArrayList<Map<String, Object>> maps) throws Exception {
                        return maps;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<>(callBack));
    }

    public void Login(final SimpleCallBack callBack, String username, String password) {
        FormBody body = new FormBody.Builder()
                .add("psnid", username)
                .add("pass", password)
                .add("signin", "")
                .build();

        apiService.Login(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    UserState.Check(response.body().string());
                    if (UserState.isLogin()) {
                        callBack.Success();
                    } else {
                        callBack.Failed();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callBack.Failed();
            }
        });
    }

    public void signIn() {
        apiService.signIn().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    LogUtils.d("签到成功");
                } else {
                    LogUtils.d("签到失败");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void getElement(SimpleSubCallBack<String> callBack) {
        apiService.getElement()
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, List<String>>() {
                    @Override
                    public List<String> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ConvertHtml.parseElement(responseBody.string());
                    }
                })
                .flatMapIterable(new Function<List<String>, Iterable<String>>() {
                    @Override
                    public Iterable<String> apply(@NonNull List<String> strings) throws Exception {
                        return strings;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<>(callBack));
    }

    public void getPhotos(SimpleSubCallBack<Map<String, Object>> callBack) {
        apiService.getPhoto()
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ConvertHtml.parsePhoto(responseBody.string());
                    }
                })
                .flatMapIterable(new Function<ArrayList<Map<String, Object>>, Iterable<Map<String, Object>>>() {
                    @Override
                    public Iterable<Map<String, Object>> apply(@NonNull ArrayList<Map<String, Object>> maps) throws Exception {
                        return maps;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<>(callBack));
    }

    public void uploadPhoto(final SimpleCallBack callBack, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("upimg", file.getName(), requestFile);
        apiService.upload(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                callBack.Success();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callBack.Failed();
            }
        });
    }

    public void delPhoto(String id) {
        FormBody body = new FormBody.Builder()
                .add("delimg", id).build();
        apiService.del(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    public void getPSN(SimpleSubCallBack<Map<String, Object>> callBack, String psnid, final int type) {
        Observable<ResponseBody> call = null;
        if (type == KEY.PSN_GAME) {
            call = apiService.getGame(psnid);
        } else if (type == KEY.PSN_MSG) {
            call = apiService.getMsg(psnid);
        } else if (type == KEY.TOPIC) {
            call = apiService.getTopic(psnid);
        } else if (type == KEY.GENE) {
            call = apiService.getGene(psnid);
        }
        assert call != null;
        call.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, ArrayList<Map<String, Object>>>() {
                    @Override
                    public ArrayList<Map<String, Object>> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ConvertHtml.parsePSN(responseBody.string(), type);
                    }
                })
                .flatMapIterable(new Function<ArrayList<Map<String, Object>>, Iterable<Map<String, Object>>>() {
                    @Override
                    public Iterable<Map<String, Object>> apply(@NonNull ArrayList<Map<String, Object>> maps) throws Exception {
                        return maps;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SimpleSubscriber<>(callBack));
    }

    public void getPSNINFO(final SimpleReturn<Map<String, String>> simpleReturn, String psnid) {
        apiService.getPSNINFO(psnid)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Map<String, String>>() {
                    @Override
                    public Map<String, String> apply(@NonNull ResponseBody responseBody) throws Exception {
                        return ConvertHtml.parsePSNINFO(responseBody.string());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Map<String, String>>() {
                    @Override
                    public void accept(@NonNull Map<String, String> map) throws Exception {
                        simpleReturn.accept(map);
                    }
                });

    }

    public void psnAction(final SimpleCallBack callBack, int type, String psnid) {
        Call<ResponseBody> call = null;
        FormBody.Builder body = new FormBody.Builder();

        switch (type) {
            case R.id.activity_psn_block:
                body.add("type", "psnid")
                        .add("param", psnid);
                call = apiService.Block(body.build());
                break;
            case R.id.activity_psn_fav:
                body.add("type", "psnid")
                        .add("param", psnid)
                        .add("updown", "up");
                call = apiService.Fav(body.build());
                break;
            case R.id.activity_psn_up:
                body.add("type", "psnid")
                        .add("param", psnid)
                        .add("updown", "up");
                call = apiService.Up(body.build());
                break;
            case R.id.activity_psn_upbase:
                call = apiService.upBase(psnid);
                break;
            case R.id.activity_psn_upgame:
                call = apiService.upGame(psnid);
                break;
        }
        assert call != null;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callBack.Success();
                } else {
                    callBack.Failed();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                callBack.Failed();
            }
        });
    }

    public void Reply(final SimpleCallBack callBack, String content, String id, String type) {
        FormBody.Builder body = new FormBody.Builder();
        body.add("type", type)
                .add("param", id)
                .add("old", "yes")
                .add("com", "")
                .add("content", content);
        apiService.Reply(body.build()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    callBack.Success();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
