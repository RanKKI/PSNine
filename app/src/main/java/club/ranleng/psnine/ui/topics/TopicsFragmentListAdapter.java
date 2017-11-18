package club.ranleng.psnine.ui.topics;

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
import club.ranleng.psnine.base.model.BaseTopics;
import club.ranleng.psnine.ui.topic.TopicActivity;

public class TopicsFragmentListAdapter<T extends BaseTopics.BaseItem>
        extends RecyclerView.Adapter<TopicsFragmentListAdapter<T>.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private List<T> Items = new ArrayList<>();
    private Fragment fragment;

    TopicsFragmentListAdapter(Fragment fragment) {
        this.fragment = fragment;
        mLayoutInflater = LayoutInflater.from(Utils.getApp());
    }

    void add(BaseTopics<T> baseTopics, int page) {
        final List<T> newItems = baseTopics.getItems();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return Items.size();
            }

            @Override
            public int getNewListSize() {
                return newItems.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return true;
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return Items.get(oldItemPosition).getContent().equals(newItems.get(newItemPosition).getContent());
            }
        });
        if (page == 1) {
            Items = newItems;
        } else {
            Items.addAll(newItems);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.adapter_topics_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseTopics.BaseItem item = Items.get(position);
        holder.content.setText(item.getContent());
        holder.username.setText(item.getUsername());
        holder.reply.setText(item.getReply());
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
            BaseTopics.BaseItem item = Items.get(getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putString("url", item.getUrl());
            bundle.putString("content", item.getContent());
            ActivityUtils.startActivity(bundle, TopicActivity.class);
        }
    }
}
