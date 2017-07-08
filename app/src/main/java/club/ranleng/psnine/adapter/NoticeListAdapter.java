package club.ranleng.psnine.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.widget.HTML.CmHtml;

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.ViewHolder> {

    private ArrayList<Map<String, Object>> mData;
    private OnItemClickListener clickListener;
    private Context context;

    public NoticeListAdapter(Context context,ArrayList<Map<String, Object>> data) {
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_articlelist_item, parent, false);
        return new ViewHolder(v);
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> map = mData.get(position);
        CmHtml.convert(holder.itemView.getContext(),holder.title, (String) map.get("title"));
        holder.title.setSingleLine(false);
        holder.name.setText((String) map.get("username"));
        holder.time.setText((String) map.get("time"));
        holder.itemView.setTag(map.get("url"));
        holder.reply.setVisibility(View.INVISIBLE);
        Glide.with(context).load(map.get("user_icon")).into(holder.icon);
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.adapter_list_title) TextView title;
        @BindView(R.id.adapter_list_name) TextView name;
        @BindView(R.id.adapter_list_time) TextView time;
        @BindView(R.id.adapter_list_reply) TextView reply;
        @BindView(R.id.adapter_list_icon) ImageView icon;
        @BindView(R.id.adapter_list_root) LinearLayout root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            root.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }

    }
}
