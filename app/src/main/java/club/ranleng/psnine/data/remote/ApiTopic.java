package club.ranleng.psnine.data.remote;

import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.common.KeyGetter;
import club.ranleng.psnine.common.RxBus;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.model.UserInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.ghui.fruit.Fruit;
import okhttp3.ResponseBody;

public class ApiTopic<T> {

    public Observable<T> getTopic(int type, String id, final Class<T> tClass) {
        return ApiManager.getDefault().getApiService().getTopic(KeyGetter.getPath(type), id)
                .subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        responseBody.close();
                        return new Fruit().fromHtml(result, tClass);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<T> getTopics(int type, int page, final Class<T> tClass) {
        Observable<ResponseBody> observable;
        if (type == Key.TOPIC || type == Key.GENE || type == Key.QA) {
            observable = ApiManager.getDefault().getApiService().getTopics(KeyGetter.getKEY(type), page);
        } else {
            observable = ApiManager.getDefault().getApiService().getTopicsWithNode(KeyGetter.getKEY(type), page);
        }
        return observable.subscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        responseBody.close();
                        if (!UserState.isLogin()) {
                            RxBus.getDefault().send(new Fruit().fromHtml(result, UserInfo.class));
                        }
                        return new Fruit().fromHtml(result, tClass);
                    }
                }).observeOn(AndroidSchedulers.mainThread());
    }
}
