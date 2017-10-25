package club.ranleng.psnine.topics;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.TopicsNormal;
import club.ranleng.psnine.topic.TopicActivity;

public class TopicsListAdapter extends RecyclerView.Adapter<TopicsListAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<TopicsNormal.Item> Items = new ArrayList<>();
    private Fragment fragment;

    TopicsListAdapter(Fragment fragment) {
        this.fragment = fragment;
        mLayoutInflater = LayoutInflater.from(Utils.getApp());
    }

    void add(TopicsNormal topicsNormal) {
        List<TopicsNormal.Item> newItems = topicsNormal.getItems();
        if (getItemCount() == 0) {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new TopicsDiffCallback(Items, newItems));
            result.dispatchUpdatesTo(this);
            Items.addAll(newItems);
        } else {
            int start = getItemCount();
            Items.addAll(newItems);
            notifyItemRangeInserted(start, getItemCount());
        }
    }

    void clear() {
        int end = getItemCount();
        Items.clear();
        this.notifyItemRangeRemoved(0, end);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.adapter_topics_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopicsNormal.Item item = Items.get(position);
        holder.content.setText(item.getContent());
        holder.username.setText(item.getUsername());
        String reply = "评论 " + item.getReply();
        holder.reply.setText(reply);
        holder.time.setText(item.getTime());
        Glide.with(fragment).load(item.getAvatar()).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return Items == null ? 0 : Items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.adapter_list_content) TextView content;
        @BindView(R.id.adapter_list_name) TextView username;
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
            TopicsNormal.Item item = Items.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("url", item.getUrl());
            bundle.putString("content", item.getContent());
            ActivityUtils.startActivity(bundle, TopicActivity.class);
        }
    }

    class TopicsDiffCallback extends DiffUtil.Callback {

        private List<TopicsNormal.Item> oldItems;
        private List<TopicsNormal.Item> newItems;

        TopicsDiffCallback(List<TopicsNormal.Item> oldItems, List<TopicsNormal.Item> newItems) {
            this.oldItems = oldItems;
            this.newItems = newItems;
        }

        @Override
        public int getOldListSize() {
            return oldItems.size();
        }

        @Override
        public int getNewListSize() {
            return newItems.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).getUrl().equals(newItems.get(newItemPosition).getUrl());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldItems.get(oldItemPosition).getContent().equals(newItems.get(newItemPosition).getContent());
        }
    }
}
