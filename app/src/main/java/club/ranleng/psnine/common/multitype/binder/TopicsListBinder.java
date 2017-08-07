package club.ranleng.psnine.common.multitype.binder;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.model.TopicsBean;
import club.ranleng.psnine.module.topic.TopicActivity;
import club.ranleng.psnine.utils.FeedBackUtils;
import club.ranleng.psnine.utils.HTML.cHtml;
import me.drakeet.multitype.ItemViewBinder;

public class TopicsListBinder extends ItemViewBinder<TopicsBean, TopicsListBinder.ViewHolder> {

    public interface TopicsListListener{
        void onClick(int type, int topic_id);
    }
    private TopicsListListener listener;

    public TopicsListBinder(TopicsListListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_topics_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TopicsBean item) {
        Context context = holder.itemView.getContext().getApplicationContext();
        holder.itemView.setTag(R.id.TAG_TOPIC_TYPE, item.getType());
        holder.itemView.setTag(R.id.TAG_TOPIC_ID, item.getId());
//        holder.title.setSingleLine(KEY.PREF_SINGLELINE);
        holder.title.setText(cHtml.returnHtml(context, item.getTitle()));
        holder.name.setText(item.getUsername());
        holder.time.setText(item.getTime());
        holder.reply.setText(item.getReply());
        Glide.with(context).load(item.getIcon()).into(holder.icon);
        FeedBackUtils.setOnclickfeedBack(holder.itemView);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.adapter_list_title) TextView title;
        @BindView(R.id.adapter_list_name) TextView name;
        @BindView(R.id.adapter_list_time) TextView time;
        @BindView(R.id.adapter_list_reply) TextView reply;
        @BindView(R.id.adapter_list_icon) ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = (int) v.getTag(R.id.TAG_TOPIC_ID);
            int type = (int) v.getTag(R.id.TAG_TOPIC_TYPE);
            listener.onClick(type,id);
        }

    }

}
