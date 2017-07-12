package club.ranleng.psnine.activity.Assist;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.fragments.ArticleListFragment;

public class SearchActivity extends AppCompatActivity implements ArticleListFragment.FinishLoadListener{

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toolbar_framelayout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Fragment f = new ArticleListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", getIntent().getStringExtra("type"));
        bundle.putBoolean("search",true);
        bundle.putString("key", getIntent().getStringExtra("key"));
        f.setArguments(bundle);

        setTitle(getIntent().getStringExtra("key"));



        getSupportFragmentManager().beginTransaction()
                .replace(R.id.framelayout, f)
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
