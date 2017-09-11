package club.ranleng.psnine.base;

import android.app.Instrumentation;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Transition;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;

public abstract class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            Transition fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }
        init();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    Instrumentation inst = new Instrumentation();
                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                }
            }.start();
        }

        return super.onOptionsItemSelected(item);
    }

    public void init() {
        setContentView();
        find_setup_Views();
        getData();
    }

    public abstract void setContentView();

    public abstract void find_setup_Views();

    public abstract void getData();


}
