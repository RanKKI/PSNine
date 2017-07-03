package club.ranleng.psnine.activity.Assist;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import club.ranleng.psnine.R;
import me.drakeet.multitype.Items;
import me.drakeet.support.about.AbsAboutActivity;
import me.drakeet.support.about.BuildConfig;
import me.drakeet.support.about.Card;
import me.drakeet.support.about.Category;
import me.drakeet.support.about.Contributor;
import me.drakeet.support.about.License;
import me.drakeet.support.about.Line;

public class AboutActivity extends AbsAboutActivity {

    @Override
    protected void onCreateHeader(ImageView icon, TextView slogan, TextView version) {
        setHeaderContentColor(getResources().getColor(R.color.colorPrimary));
        setNavigationIcon(R.drawable.ic_action_back);
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText(R.string.about_header);
        slogan.setTextColor(Color.parseColor("#000000"));
        version.setText(getVersion());
        version.setTextColor(Color.parseColor("#000000"));
    }

    @Override
    protected void onItemsCreated(@NonNull Items items) {
        items.add(new Category("Developers"));
        items.add(new Contributor(R.drawable.emoji_icon, "RanKKI", "Developer & designer", "http://weibo.com/drak11t"));

        items.add(new Line());

        items.add(new Category("Open Source Licenses"));
        items.add(new License("Okhttp", "Square", License.APACHE_2, "https://github.com/square/okhttp"));
        items.add(new License("Glide", "Sam Judd", "BSD, part MIT and Apache 2.0", "https://github.com/bumptech/glide"));
        items.add(new License("PersistentCookieJar", "franmontiel", License.APACHE_2, "https://github.com/franmontiel/PersistentCookieJar"));
        items.add(new License("JKeyboardPanelSwitch", "Jacks gong.", License.APACHE_2, "https://github.com/drakeet/MultiType"));
        items.add(new License("MultiType", "Drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"));
        items.add(new License("about-page", "Drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"));
    }
    /**
     * 获取版本号
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
