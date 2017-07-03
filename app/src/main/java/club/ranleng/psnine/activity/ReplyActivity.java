package club.ranleng.psnine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseActivity;
import cn.dreamtobe.kpswitch.util.KPSwitchConflictUtil;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;
import cn.dreamtobe.kpswitch.widget.KPSwitchPanelLinearLayout;

public class ReplyActivity extends BaseActivity {

    @BindView(R.id.reply_activity_button) Button reply_button;
    @BindView(R.id.reply_activity_edittext) EditText editText;
    @BindView(R.id.reply_activity_emoji) ImageView emoji;
    @BindView(R.id.reply_activity_image) ImageView imageView;
    @BindView(R.id.panel_root) KPSwitchPanelLinearLayout mPanelLayout;
    private Context context;
    private ArrayList<String> photo_list = new ArrayList<>();

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_reply);
    }

    @Override
    public void findViews() {
        ButterKnife.bind(this);
    }

    @Override
    public void setupViews() {
        context = this;
        reply_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(context, PickImgActivity.class),1);
            }
        });

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        /**
         * 这个Util主要是监控键盘的状态: 显示与否 以及 键盘的高度
         * 这里也有提供给外界监听 键盘显示/隐藏 的监听器，具体参看
         * 这个接口 {@Link KeyboardUtil#attach(Activity, IPanelHeightTarget, OnKeyboardShowingListener)}
         */
        KeyboardUtil.attach(this, mPanelLayout);

        /**
         * 这个Util主要是协助处理一些面板与键盘相关的事件。
         * 这个方法主要是对一些相关事件进行注册，如切换面板与键盘等，具体参看源码，比较简单。
         * 里面还提供了一些已经处理了冲突的工具方法: 显示面板；显示键盘；键盘面板切换；隐藏键盘与面板；
         *
         * 如果有多个面板的需求，可以参看: KPSwitchConflictUtil.attach(panelLayout, focusView, )
         *
         * @param panelRoot 面板的布局。
         * @param switchPanelKeyboardBtn 用于触发切换面板与键盘的按钮。
         * @param focusView 键盘弹起时会给这个View focus，收回时这个View会失去focus，通常是发送的EditText。
         */
        KPSwitchConflictUtil.attach(mPanelLayout, emoji, editText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        photo_list = data.getExtras().getStringArrayList("result");

    }


    @Override
    public void getData() {

    }

    @Override
    public void showContent() {

    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if (event.getAction() == KeyEvent.ACTION_UP &&
                event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (mPanelLayout.getVisibility() == View.VISIBLE) {
                KPSwitchConflictUtil.hidePanelAndKeyboard(mPanelLayout);
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

}
