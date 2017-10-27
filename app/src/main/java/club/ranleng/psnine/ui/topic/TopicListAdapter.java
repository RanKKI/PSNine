package club.ranleng.psnine.ui.topic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.UrlClickListener;

import java.util.ArrayList;
import java.util.List;

import club.ranleng.psnine.R;
import club.ranleng.psnine.base.BaseTopic;
import club.ranleng.psnine.model.TopicComment;
import club.ranleng.psnine.ui.ImageViewActivity;
import club.ranleng.psnine.utils.html.HtmlImageGetter;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private static final int HeaderView = 1;
    private LayoutInflater layoutInflater;
    private List<TopicComment.Comment> comments = new ArrayList<>();
    private BaseTopic baseTopic;
    private Context context;
    private UrlClick urlClick;

    TopicListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        urlClick = new UrlClick();
    }

    void setHeaderView(BaseTopic baseTopic) {
        this.baseTopic = baseTopic;
        notifyItemChanged(0);
    }

    void addComments(TopicComment topicComment) {
        int start = getItemCount();
        comments.addAll(topicComment.getComments());
        notifyItemRangeInserted(start, getItemCount());
    }

    void clearComments() {
        int end = comments.size();
        comments.clear();
        notifyItemRangeRemoved(1, end);
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
        if (position == 0) {
            ((HeaderViewHolder) holder).bind();
            return;
        }
        if (holder != null) {
            holder.bind(position - 1);
        }
    }

    @Override
    public int getItemCount() {
        if (baseTopic == null) {
            return 0;
        }
        return comments == null ? 1 : comments.size() + 1;
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
        HtmlTextView content;
        TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.adapter_topic_comment_avatar);
            username = itemView.findViewById(R.id.adapter_topic_comment_username);
            content = itemView.findViewById(R.id.adapter_topic_comment_content);
            time = itemView.findViewById(R.id.adapter_topic_comment_time);
        }

        void bind(int position) {
            TopicComment.Comment comment = comments.get(position);
            time.setText(comment.getTime());
            username.setText(comment.getUsername());
            content.setHtml(comment.getContent(), new HtmlImageGetter(context, content));
            content.setUrlClickListener(urlClick);
            Glide.with(context).load(comment.getAvatar()).into(avatar);

        }

    }

    class HeaderViewHolder extends ViewHolder {

        HtmlTextView content;
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
            content.setUrlClickListener(urlClick);
            String con;
            if (baseTopic.getTitle() != null) {
                con = "<p>" + baseTopic.getTitle() + "</p><br>" + baseTopic.getContent();
            } else {
                con = baseTopic.getContent();
            }
            content.setHtml(con, new HtmlImageGetter(context, content));
            username.setText(baseTopic.getAuthor());
            time.setText(baseTopic.getTime());
            replies.setText(baseTopic.getComments());
            Glide.with(context).load(baseTopic.getAvatar()).into(icon);
        }
    }

    class UrlClick implements UrlClickListener {

        @Override
        public void OnClick(String url) {
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            ActivityUtils.startActivity(bundle, ImageViewActivity.class);
        }
    }

}
