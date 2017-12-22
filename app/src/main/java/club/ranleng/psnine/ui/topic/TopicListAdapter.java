package club.ranleng.psnine.ui.topic;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.sufficientlysecure.htmltextview.ImageClick;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.base.model.BaseTopic;
import club.ranleng.psnine.model.Topic.TopicComment;
import club.ranleng.psnine.model.Topic.TopicGame;
import club.ranleng.psnine.ui.ImageViewActivity;
import club.ranleng.psnine.ui.psn.PSNActivity;
import club.ranleng.psnine.utils.HtmlImageGetter;
import club.ranleng.psnine.utils.Parse;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private static final int HeaderView = 10;
    private static final int FooterView = 11;
    private static final int GameView = 12;
    private LayoutInflater layoutInflater;
    private List<TopicComment.Comment> comments = new ArrayList<>();
    private BaseTopic baseTopic;
    private List<TopicGame> topicGame;
    private Context context;
    private ImageClick imageClick;
    private int gamesSize;

    TopicListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.context = context;
        imageClick = new ImageClick(new ImageClick.UrlClickListener() {
            @Override
            public void OnClick(String url) {
                Bundle bundle = new Bundle();
                bundle.putString("url", url);
                ActivityUtils.startActivity(bundle, ImageViewActivity.class);
            }
        }, Parse.sinaimg_pattern);
    }

    void setHeaderView(BaseTopic baseTopic) {
        this.baseTopic = baseTopic;
        notifyItemChanged(0);
    }

    void setTopicGame(List<TopicGame> topicGame) {
        this.topicGame = topicGame;
    }

    void addComments(TopicComment topicComment) {
        int start = getItemCount();
        comments.addAll(topicComment.getComments());
        notifyItemRangeInserted(start, getItemCount());
    }

    void addComment(TopicComment.Comment comment) {
        int pos = getItemCount() - 1;
        comments.add(comment);
        notifyItemRangeChanged(pos, getItemCount());
    }

    void clear() {
//        int end = comments.size();
        notifyItemRangeRemoved(0, getItemCount());
        comments.clear();
        baseTopic = null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HeaderView) {
            View header = layoutInflater.inflate(R.layout.adapter_topic_header, parent, false);
            return new HeaderViewHolder(header);
        } else if (viewType == FooterView) {
            View footer = layoutInflater.inflate(R.layout.adapter_topic_footer, parent, false);
            return new FooterViewHolder(footer);
        } else if(viewType == GameView){
            View game = layoutInflater.inflate(R.layout.adapter_topics_psngame_item, parent, false);
            return new GameViewHolder(game);
        }
        View view = layoutInflater.inflate(R.layout.adapter_topic_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder && position == 0) {
            ((HeaderViewHolder) holder).bindHeader();
            return;
        }
        if (holder instanceof FooterViewHolder && position == getItemCount() - 1) {
            return;
        }

        if (holder instanceof GameViewHolder && position <= gamesSize) {
            ((GameViewHolder) holder).bindGame(position - 1);
            return;
        }

        if (holder != null) {
            holder.bind(position - 1 - gamesSize);
        }
    }

    @Override
    public int getItemCount() {
        if (baseTopic == null) {
            return 0;
        }
        gamesSize = topicGame == null ? 0 : topicGame.size();
        int size = 2;
        size += comments.size();
        size += gamesSize;
        return size;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HeaderView;
        } else if (position == getItemCount() - 1) {
            return FooterView;
        } else if (position <= gamesSize) {
            return GameView;
        }
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            content.setImageClick(imageClick);
            content.setHtml(Parse.parseHtml(comment.getContent()), new HtmlImageGetter(context, content));
            time.setText(comment.getTime());
            username.setText(comment.getUsername());
            Glide.with(context).load(comment.getAvatar()).into(avatar);
            avatar.setOnClickListener(this);
            username.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            lookupPSN(comments.get(getLayoutPosition() - 1).getUsername());
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

        void bindHeader() {
            content.setImageClick(imageClick);
            content.setHtml(Parse.parseHtml(baseTopic.getFormattedContent()), new HtmlImageGetter(context, content));
            username.setText(baseTopic.getAuthor());
            time.setText(baseTopic.getTime());
            replies.setText(baseTopic.getComments());
            Glide.with(context).load(baseTopic.getAvatar()).into(icon);
            icon.setOnClickListener(this);
            username.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            lookupPSN(baseTopic.getAuthor());
        }
    }

    class FooterViewHolder extends ViewHolder {

        FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    class GameViewHolder extends ViewHolder{

        @BindView(R.id.PSNGameItemIcon) ImageView icon;
        @BindView(R.id.PSNGameItemName) TextView name;
        @BindView(R.id.PSNGameItemTrophy) TextView trophy;
        @BindView(R.id.PSNGameItemTime) TextView perfect;
        @BindView(R.id.PSNGameItemProgress) ProgressBar progressBar;

        GameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindGame(int pos){
            TopicGame game =  topicGame.get(pos);
            Glide.with(context).load(game.getIcon()).into(icon);
            name.setText(game.getName());
            trophy.setText(game.getTrophy());
            perfect.setText(game.getMode());
            progressBar.setProgress(game.getPerfect());
        }
    }

    private void lookupPSN(String username) {
        Bundle bundle = new Bundle();
        bundle.putString("psnid", username);
        ActivityUtils.startActivity(bundle, PSNActivity.class);
    }
}
