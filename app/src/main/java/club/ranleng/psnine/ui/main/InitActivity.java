package club.ranleng.psnine.ui.main;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;

import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.utils.LCache;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class InitActivity extends BaseActivity {

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_init);
    }

    @Override
    public void find_setup_Views() {

    }

    @Override
    public void getData() {
        Key.getSetting();
        LCache.init();
        ApiManager.getDefault();
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                String ip = NetworkUtils.getDomainAddress(ApiManager.domain);
                e.onNext(NetworkUtils.isAvailableByPing(ip));
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean connected) throws Exception {
                if (connected) {
                    ActivityUtils.startActivity(MainActivity.class);
                } else {
                    ToastUtils.showShort("服务端连接失败.");
                }
                finish();
            }
        });

    }
}
