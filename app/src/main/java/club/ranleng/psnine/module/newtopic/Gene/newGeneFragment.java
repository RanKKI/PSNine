package club.ranleng.psnine.module.newtopic.Gene;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.module.photo.PhotoGalleryActivity;
import club.ranleng.psnine.utils.TextUtils;
import okhttp3.FormBody;

import static android.app.Activity.RESULT_OK;

public class newGeneFragment extends Fragment implements newGeneContract.View {

    @BindView(R.id.new_gene_waning) TextView waning;
    @BindView(R.id.new_gene_selected_img) TextView selected_img;
    //Data
    @BindView(R.id.new_gene_main_edittext) EditText main_edit;
    @BindView(R.id.new_gene_ele) EditText ele;
    @BindView(R.id.new_gene_video_url) EditText video_url;
    @BindView(R.id.new_gene_url) EditText url;

    @BindView(R.id.swipelayout) SwipeRefreshLayout swipeRefreshLayout;

    private newGeneContract.Presenter mPresenter;
    private ArrayList<String> photo_list = new ArrayList<>();

    private boolean edit;
    private String topic_id;

    public newGeneFragment() {
        new newGenePresenter(this);
    }

    public static newGeneFragment newInstance(boolean edit, String topic_id) {
        newGeneFragment fragment = new newGeneFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("edit", edit);
        bundle.putString("topic_id", topic_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_gene, container, false);
        ButterKnife.bind(this, view);
        mPresenter.start();
        edit = getArguments().getBoolean("edit");
        topic_id = getArguments().getString("topic_id");
        return view;
    }

    @Override
    public void setPresenter(newGeneContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setup() {
        String text = "提问题请发到「<font color='blue' >问答</font>」板块，否则将被<font color='red' >关闭处理</font>";
        waning.setText(Html.fromHtml(text));
        swipeRefreshLayout.setEnabled(false);
        getActivity().setTitle("发基因");
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public FormBody getData() {
        FormBody.Builder body = new FormBody.Builder();

        body.add("content", TextUtils.toS(main_edit))
                .add("element", TextUtils.toS(ele))
                .add("video", TextUtils.toS(video_url))
                .add("url", TextUtils.toS(url));

        String p = "";
        for (String i : photo_list) {
            p += i + ",";
        }

        if (!p.contentEquals("")) {
            p = p.substring(0, p.length() - 1);
        }

        body.add("photo", p);

        if (edit) {
            body.add("geneid", topic_id)
                    .add("editgene", "");
        } else {
            body.add("addgene", "");
        }
        return body.build();
    }


    @OnClick(R.id.new_gene_submit)
    public void submit() {
        mPresenter.submit();
    }

    @OnClick(R.id.new_gene_select_img)
    public void select_img() {
        Intent intent = new Intent(getActivity(), PhotoGalleryActivity.class);
        intent.putExtra("photo_list", photo_list);
        startActivityForResult(intent, KEY.REQUEST_PICKIMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == KEY.REQUEST_PICKIMG) {
            photo_list = data.getExtras().getStringArrayList("photo_list");
            if (photo_list == null) {
                return;
            }
            String selected = photo_list.size() + " 张";
            selected_img.setText(selected);
        }
    }
}
