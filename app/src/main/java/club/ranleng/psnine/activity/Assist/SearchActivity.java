package club.ranleng.psnine.activity.Assist;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import club.ranleng.psnine.fragments.ArticleListFragment;
import club.ranleng.psnine.fragments.SettingFragment;

public class SearchActivity extends AppCompatActivity implements ArticleListFragment.FinishLoadListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment f = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", getIntent().getStringExtra("type"));
        bundle.putBoolean("search",true);
        bundle.putString("key", getIntent().getStringExtra("key"));
        f.setArguments(bundle);

        setTitle("关键词:" + getIntent().getStringExtra("key"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, f)
                .commit();
    }

    @Override
    public void onFinish() {

    }
}
