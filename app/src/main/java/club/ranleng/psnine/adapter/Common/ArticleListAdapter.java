package club.ranleng.psnine.adapter.Common;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Common.ArticleList;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleListAdapter extends ItemViewBinder<ArticleList, ArticleListAdapter.ViewHolder> {

    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public ArticleListAdapter(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_articlelist_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleList item) {
        Map<String, Object> map = item.data;
        holder.title.setText((String) map.get("title"));
        holder.name.setText((String) map.get("username"));
        holder.time.setText((String) map.get("time"));
        holder.reply.setText((String) map.get("reply"));
        Glide.with(holder.itemView.getContext()).load(map.get("icon")).into(holder.icon);
        holder.itemView.setTag(map.get("id"));
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.adapter_list_title) TextView title;
        @BindView(R.id.adapter_list_name) TextView name;
        @BindView(R.id.adapter_list_time) TextView time;
        @BindView(R.id.adapter_list_reply) TextView reply;
        @BindView(R.id.adapter_list_icon) ImageView icon;
        @BindView(R.id.adapter_list_root) LinearLayout root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }

    }
}
