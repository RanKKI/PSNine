package club.ranleng.psnine.ui.newTopic;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseFragment;
import club.ranleng.psnine.common.Key;
import club.ranleng.psnine.ui.imageGallery.ImagesGalleryActivity;
import club.ranleng.psnine.utils.TextUtils;
import okhttp3.FormBody;

import static android.app.Activity.RESULT_OK;

public class newTopicGeneFragment extends BaseFragment implements newTopicContact.View {

    private final int selectPhotos = 91;
    private final String selectedImagesText = "%d 张";

    @BindView(R.id.new_gene_waning) TextView waning;
    @BindView(R.id.new_gene_selected_img) TextView selected_img;
    @BindView(R.id.new_gene_main_edittext) EditText content;
    @BindView(R.id.new_gene_ele) EditText ele;
    @BindView(R.id.new_gene_video_url) EditText video_url;
    @BindView(R.id.new_gene_url) EditText url;
    @BindView(R.id.swipelayout) SwipeRefreshLayout swipeRefreshLayout;

    private newTopicContact.Presenter presenter;
    private ArrayList<String> photo_list = new ArrayList<>();


    public static newTopicGeneFragment newInstance() {
        return new newTopicGeneFragment();
    }
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.layout_new_gene, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        newTopicPresenter.newInstance(this);
        presenter.start();
    }

    @OnClick(R.id.new_gene_submit)
    public void newGeneSubmit() {
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        bodyBuilder.add("content", TextUtils.toString(content))
                .add("element", TextUtils.toString(ele))
                .add("video", TextUtils.toString(video_url))
                .add("url", TextUtils.toString(url))
                .add("photo", TextUtils.toStringPhoto(photo_list))
                .add("addgene","");
        presenter.post(bodyBuilder.build());
    }

    @OnClick(R.id.new_gene_select_img)
    public void SelectImg() {
        Intent intent = new Intent(getActivity(), ImagesGalleryActivity.class);
        intent.putExtra("photos", photo_list);
        startActivityForResult(intent, selectPhotos);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == selectPhotos) {
            List<String> list = data.getStringArrayListExtra("photos");
            if (list != null) {
                photo_list.clear();
                photo_list.addAll(list);
            }
            loadImagesSize();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void setPresenter(newTopicContact.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setup() {
        String text = "提问题请发到「<font color='blue'>问答</font>」板块,否则将被<font color='red'>关闭处理</font>";
        waning.setText(Html.fromHtml(text));
        swipeRefreshLayout.setEnabled(false);
        getActivity().setTitle("发基因");
    }

    @Override
    public int getType() {
        return Key.GENE;
    }

    @Override
    public void finishPosted() {
        getActivity().finish();
    }

    @Override
    public Context getCtx() {
        return mContext;
    }

    private void loadImagesSize() {
        selected_img.setText(String.format(Locale.CHINA, selectedImagesText, photo_list.size()));
    }
}
