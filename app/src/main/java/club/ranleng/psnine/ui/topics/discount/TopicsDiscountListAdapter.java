package club.ranleng.psnine.ui.topics.discount;

import android.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Topics.TopicsDiscount;

public class TopicsDiscountListAdapter extends RecyclerView.Adapter<TopicsDiscountListAdapter.ViewHolder> {

    private final LayoutInflater mLayoutInflater;
    private Fragment fragment;
    private List<TopicsDiscount.Item> items = new ArrayList<>();

    TopicsDiscountListAdapter(Fragment fragment) {
        this.fragment = fragment;
        mLayoutInflater = LayoutInflater.from(Utils.getApp());
    }

    void add(TopicsDiscount discount, int page) {
        final List<TopicsDiscount.Item> newItems = discount.getItems();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return items.size();
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
                boolean name = items.get(oldItemPosition).getName().equals(newItems.get(newItemPosition).getName());
                boolean activity = items.get(oldItemPosition).getActivity().equals(newItems.get(newItemPosition).getActivity());
                return name && activity;
            }
        });
        if (page == 1) {
            items = newItems;
        } else {
            items.addAll(newItems);
        }
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.adapter_topics_discount_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopicsDiscount.Item item = items.get(position);
        holder.activity.setText(item.getActivity());
        holder.des.setText(item.getDes());
        holder.name.setText(item.getName());
        holder.time.setText(item.getTime());
        Glide.with(fragment).load(item.getIcon()).into(holder.icon);

    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.discountItemIcon) ImageView icon;
        @BindView(R.id.discountItemActivity) TextView activity;
        @BindView(R.id.discountItemDes) TextView des;
        @BindView(R.id.discountItemName) TextView name;
        @BindView(R.id.discountItemTime) TextView time;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
