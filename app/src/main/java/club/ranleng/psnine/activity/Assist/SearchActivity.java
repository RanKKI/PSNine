package club.ranleng.psnine.activity.Assist;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.io.File;

import club.ranleng.psnine.R;
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

        setTitle(getIntent().getStringExtra("key"));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, f)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinish() {

    }
}
