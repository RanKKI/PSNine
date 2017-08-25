package club.ranleng.psnine.module.newtopic.Topic;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class newTopicFragment extends Fragment implements newTopicContract.View {

    @BindView(R.id.swipelayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.new_topic_content) EditText content;
    @BindView(R.id.new_topic_title) EditText title;

    @BindView(R.id.new_topic_waning_top) TextView waning_top;
    @BindView(R.id.new_topic_submit) Button submit;

    private newTopicContract.Presenter mPresenter;

    private boolean edit;
    private String topic_id;
    private String[] magic_dialog_value = {"[b]", "", "", "[url]", "[flash]", "[quote]", "", "[mark]", "[s]", "[title]"};
    private String[] magic_dialog = {"加粗", "彩色字体", "居中", "链接", "Flash", "引用", "图片", "刮刮卡", "删除线", "分页"};
    private String[] magic_font_color = {"red", "orange", "green", "rown", "blue", "deeppink"};
    private String[] magic_font_color_zh = {"红", "橙", "绿", "棕", "蓝", "粉"};
    private String[] magic_photo = {"图库", "URL"};
    private AlertDialog.Builder builder;
    /**
     * 0 mode
     * 1 magic
     * 2 Choose color
     * 3 Choose photo
     */
    private int type;

    public newTopicFragment() {
        new newTopicPresenter(this);
    }

    public static newTopicFragment newInstance(boolean edit, String topic_id) {
        newTopicFragment fragment = new newTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("edit", edit);
        bundle.putString("topic_id", topic_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_topic, container, false);
        ButterKnife.bind(this, view);
        mPresenter.start();
        edit = getArguments().getBoolean("edit");
        topic_id = getArguments().getString("topic_id");
        builder = new AlertDialog.Builder(getActivity());
        return view;
    }

    @Override
    public void setPresenter(newTopicContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setup() {
        getActivity().setTitle("发贴");
        String waning_a = "提问题请发到「<font color='blue'>问答</font>」板块，闲聊请发到「<font color='blue'>机因</font>」，否则将被关闭+<font color='red'>扣分处理</font>";
        waning_top.setText(Html.fromHtml(waning_a));
    }

    @Override
    public void finish() {
        getActivity().finish();
    }

    @Override
    public FormBody getData() {
        FormBody.Builder body = new FormBody.Builder();
        body.add("open", "mode")//TODO
                .add("title", TextUtils.toS(title))
                .add("content", TextUtils.toS(content))
                .add("node", "talk");
        if (edit) {
            body.add("topicid", topic_id)
                    .add("edittopic", "");
        } else {
            body.add("addtopic", "");
        }
        return body.build();
    }

    @OnClick(R.id.new_topic_submit)
    public void submit() {
        mPresenter.submit();
    }

    @OnClick(R.id.new_topic_magic)
    public void magic() {
        builder.setItems(magic_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                content.requestFocus();
                switch (which) {
                    case 1:
                        showDialog(magic_font_color_zh, 2);
                        break;
                    case 2:
                        content.setText(content.getText() + "[align=center][/align]");
                        content.setSelection(content.length() - 8);
                        break;
                    case 6:
                        showDialog(magic_photo, 3);
                        break;
                    default:
                        CAA(magic_dialog_value[which]);

                }
            }
        }).create().show();
    }

    private void showDialog(String[] s, int t) {
        builder.setItems(s, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (type == 2) {
                    content.requestFocus();
                    content.append(String.format("[color=%s][/color]", magic_font_color[which]));
                    content.setSelection(content.length() - 8);
                } else if (type == 3) {
                    content.requestFocus();
                    if (which == 0) {
                        startActivityForResult(new Intent(getActivity(), PhotoGalleryActivity.class), KEY.REQUEST_PICKIMG);
                    } else {
                        CAA("[img]");
                    }
                }
            }
        });
        type = t;
        builder.create().show();
    }

    private void CAA(String params) {
        String param = params + params.replace("[", "[/");
        content.setText(content.getText() + param);
        content.setSelection(content.length() - params.length() - 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == KEY.REQUEST_PICKIMG) {
            ArrayList<String> temp = data.getStringArrayListExtra("photo_list");
            if(temp == null){
                return;
            }
            for (String i : temp) {
                content.setText(content.getText() + "[img]http://ww4.sinaimg.cn/large/" + i + ".jpg[/img]\n");
            }
            content.setSelection(content.length());
        }
    }
}
