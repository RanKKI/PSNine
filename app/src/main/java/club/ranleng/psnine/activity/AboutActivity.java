package club.ranleng.psnine.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import club.ranleng.psnine.R;
import club.ranleng.psnine.widget.KEY;
import me.drakeet.multitype.Items;
import me.drakeet.support.about.AbsAboutActivity;
import me.drakeet.support.about.Card;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.License;
import me.drakeet.support.about.Line;

public class AboutActivity extends AbsAboutActivity {

    @Override
    protected void onCreateHeader(ImageView icon, TextView slogan, TextView version) {
        setHeaderContentColor(getResources().getColor(R.color.primary));
        setNavigationIcon(R.drawable.back);
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText(R.string.about_header);
        slogan.setTextColor(Color.parseColor("#000000"));
        version.setText(getVersion());
        version.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onItemsCreated(@NonNull Items items) {
        items.add(new Category("介绍与帮助"));
        items.add(new Card(getString(R.string.about_content), "站内贴"));

        items.add(new Line());

        items.add(new Category("Developers"));
        items.add(new Contributor(R.drawable.icon, "RanKKI", "Developer & designer", "https://github.com/RanKKI"));

        items.add(new Line());

        items.add(new Category("Open Source Licenses"));
        items.add(new License("Retrofit", "Square", License.APACHE_2,
                "https://github.com/square/retrofit"));

        items.add(new License("Glide", "SamJudd", "BSD, part MIT and Apache 2.0",
                "https://github.com/bumptech/glide"));


        items.add(new License("RxJava", "ReactiveX", License.APACHE_2,
                "https://github.com/ReactiveX/RxJava"));

        items.add(new License("RxAndroid", "ReactiveX", License.APACHE_2,
                "https://github.com/ReactiveX/RxAndroid"));


        items.add(new License("PersistentCookieJar", "franmontiel", License.APACHE_2,
                "https://github.com/franmontiel/PersistentCookieJar"));

        items.add(new License("MultiType", "Drakeet", License.APACHE_2,
                "https://github.com/drakeet/MultiType"));

        items.add(new License("about-page", "Drakeet", License.APACHE_2,
                "https://github.com/drakeet/about-page"));

        items.add(new License("AndroidUtilCode", "Blankj", License.APACHE_2,
                "https://github.com/Blankj/AndroidUtilCode"));


        items.add(new License("EventBus", "greenrobot", License.APACHE_2,
                "https://github.com/greenrobot/EventBus"));

    }

    @Override
    protected void onActionClick(View action) {
        Intent intent = new Intent(this, ArticleActivity.class);
        intent.putExtra("type", KEY.TYPE_GENE);
        intent.putExtra("id", "21233");
        startActivity(intent);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
