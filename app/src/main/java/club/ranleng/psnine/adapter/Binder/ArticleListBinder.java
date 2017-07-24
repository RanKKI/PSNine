package club.ranleng.psnine.adapter.Binder;

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
import club.ranleng.psnine.activity.ArticleActivity;
import club.ranleng.psnine.model.ArticleListModel;
import club.ranleng.psnine.utils.FeedBackUtils;
import club.ranleng.psnine.widget.HTML.CmHtml;
import club.ranleng.psnine.widget.KEY;
import me.drakeet.multitype.ItemViewBinder;

public class ArticleListBinder extends ItemViewBinder<ArticleListModel, ArticleListBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_article_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleListModel item) {
        holder.itemView.setTag(R.id.TAG_ARTICLE_LIST_BINDER_TYPE, item.type);
        holder.itemView.setTag(R.id.TAG_ARTICLE_LIST_BINDER_ID, item.id);
        holder.title.setSingleLine(KEY.PREF_SINGLELINE);
        holder.title.setText(CmHtml.returnHtml(holder.itemView.getContext(),item.title));
        holder.name.setText(item.username);
        holder.time.setText(item.time);
        holder.reply.setText(item.reply);
        Glide.with(holder.itemView.getContext()).load(item.icon).into(holder.icon);
        FeedBackUtils.setOnclickfeedBack(0xFFFAFAFA,0xFFE2E2E2,holder.itemView);
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
            int id = (int) v.getTag(R.id.TAG_ARTICLE_LIST_BINDER_ID);
            int type = (int) v.getTag(R.id.TAG_ARTICLE_LIST_BINDER_TYPE);
            Intent intent = new Intent(v.getContext(), ArticleActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("id", id);
            v.getContext().startActivity(intent);
        }

    }

}
