package club.ranleng.psnine.topic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Topic;
import club.ranleng.psnine.model.TopicComment;
import club.ranleng.psnine.utils.html.HtmlImageGetter;
import club.ranleng.psnine.utils.html.HtmlTagHandler;
import club.ranleng.psnine.utils.html.mHtml;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private static final int HeaderView = 1;
    private LayoutInflater layoutInflater;
    private List<TopicComment.Comment> Comments = new ArrayList<>();
    private Topic topic;
    private Context context;

    TopicListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    void setHeaderView(Topic topic) {
        this.topic = topic;
        notifyItemChanged(0);
    }

    void addComments(TopicComment topicComment) {
        Comments.addAll(topicComment.getComments());
        notifyItemRangeInserted(1, getItemCount());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HeaderView) {
            View header = layoutInflater.inflate(R.layout.adapter_topic_header, parent, false);
            return new HeaderViewHolder(header);
        }
        View view = layoutInflater.inflate(R.layout.adapter_topic_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0 && holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).bind();
            return;
        }
        if (holder != null) {
            holder.bind(position - 1);
        }
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

        ImageView avatar;
        TextView username;
        TextView content;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.adapter_topic_comment_avatar);
            username = itemView.findViewById(R.id.adapter_topic_comment_username);
            content = itemView.findViewById(R.id.adapter_topic_comment_content);
            time = itemView.findViewById(R.id.adapter_topic_comment_time);
        }

        void bind(int position) {
            TopicComment.Comment comment = Comments.get(position);
            time.setText(comment.getTime());
            username.setText(comment.getUsername());
            content.setText(mHtml.fromHtml(comment.getContent(), new HtmlImageGetter(context, content), new HtmlTagHandler()));
            Glide.with(context).load(comment.getAvatar()).into(avatar);
        }
    }

    class HeaderViewHolder extends ViewHolder {

        TextView content;
        TextView time;
        TextView replies;
        TextView username;
        ImageView icon;

        HeaderViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.header_content);
            time = itemView.findViewById(R.id.header_time);
            replies = itemView.findViewById(R.id.header_replies);
            username = itemView.findViewById(R.id.header_username);
            icon = itemView.findViewById(R.id.header_icon);
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
