package club.ranleng.psnine.topic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Topic;
import club.ranleng.psnine.model.TopicComment;
import club.ranleng.psnine.utils.html.HtmlImageGetter;
import club.ranleng.psnine.utils.html.HtmlTagHandler;
import club.ranleng.psnine.utils.html.mHtml;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private static final int HeaderView = 1;
    public LayoutInflater layoutInflater;
    private List<TopicComment.Comment> Comments = new ArrayList<>();
    private Topic topic;
    private Context context;

    public TopicListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setHeaderView(Topic topic) {
        this.topic = topic;
        notifyItemChanged(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HeaderView) {
            View header = layoutInflater.inflate(R.layout.adapter_topic_header, parent, false);
            return new HeaderViewHolder(header);
        }
        View view = layoutInflater.inflate(R.layout.adapter_topic_header, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0) {
            ((HeaderViewHolder) holder).bind();
            return;
        }
        //do something
        LogUtils.d("normal holder");
    }

    @Override
    public int getItemCount() {
        if (topic == null) {
            return 0;
        }
        return Comments == null ? 1 : Comments.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HeaderView;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    class HeaderViewHolder extends ViewHolder {

        @BindView(R.id.header_content) TextView content;
        @BindView(R.id.header_time) TextView time;
        @BindView(R.id.header_replies) TextView replies;
        @BindView(R.id.header_username) TextView username;
        @BindView(R.id.header_icon) ImageView icon;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind() {
            String con = "<p>" + topic.getTitle() + "</p><br>" + topic.getContent();
            content.setText(mHtml.fromHtml(con, new HtmlImageGetter(context, content), new HtmlTagHandler()));
            username.setText(topic.getAuthor());
            time.setText(topic.getTime());
            replies.setText(topic.getComments());
            Glide.with(context).load(topic.getAvatar()).into(icon);
        }
    }
}
