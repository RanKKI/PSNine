package club.ranleng.psnine.helper;

import android.app.Activity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.KeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.utils.ViewUtils;

public class ReplyFormHelper implements TextWatcher {

    private final View mRootView;
    private final OnReplyListener mListener;
    private final Activity mActivity;
    @BindView(R.id.content) EditText mContent;
    @BindView(R.id.submit) ImageButton submit;
    private boolean isShown;

    public ReplyFormHelper(Activity activity, ViewStub viewStub, OnReplyListener listener) {
        mActivity = activity;
        mRootView = viewStub.inflate();
        mListener = listener;
        ButterKnife.bind(this, mRootView);
        mRootView.setAnimation(AnimationUtils.makeInChildBottomAnimation(mActivity));

        mContent.addTextChangedListener(this);
        afterTextChanged(mContent.getText());
        isShown = false;
    }

    @OnClick(R.id.emoji)
    public void emoji() {
        mListener.onEmojiClick();
    }

    @OnClick(R.id.image)
    public void image() {
        mListener.onImageClick();
    }

    @OnClick(R.id.submit)
    public void submit() {
        mListener.onReply(mContent.getText().toString());
    }

    public void toggle() {
        isShown = !isShown;
        setVisibility(isShown);
    }

    public void show() {
        setVisibility(true);
    }


    public void hide() {
        setVisibility(false);
    }

    public boolean getVisibility() {
        return isShown;
    }

    public void setVisibility(boolean visible) {
        isShown = visible;
        mRootView.setVisibility(visible ? View.VISIBLE : View.GONE);

        if (!isShown) {
            ViewUtils.hideInputMethod(mRootView);
        }

    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        submit.setClickable(!TextUtils.isEmpty(s));
    }

    public void addContent(CharSequence content) {
        setContent(mContent.getText().toString() + content);
    }

    public void appContent(CharSequence content) {
        setContent( mContent.getText().toString() + "\n" + content);
    }

    public void at(CharSequence username) {
        String content;
        if (mContent.getText().toString().isEmpty()) {
            content = "@" + username + " ";
        } else {
            content = "\n@" + username + " ";
        }
        setContent(mContent.getText().toString() + content);
    }

    public void setContent(CharSequence content) {
        mContent.setText(content);
        mContent.setSelection(mContent.length());
        requestFocus();
        KeyboardUtils.showSoftInput(mContent);
        KeyboardUtils.showSoftInput(mRootView);
    }


    public Editable getContent() {
        return mContent.getText();
    }

    public void requestFocus() {
        mContent.requestFocus();
    }

    public interface OnReplyListener {
        void onReply(CharSequence content);
        void onEmojiClick();
        void onImageClick();
    }

}
