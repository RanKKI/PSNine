package club.ranleng.psnine.common.multitype.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
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
import club.ranleng.psnine.common.multitype.model.ArticleReply;
import club.ranleng.psnine.utils.FeedBackUtils;
import club.ranleng.psnine.utils.HTML.cHtml;
import me.drakeet.multitype.ItemViewBinder;


public class ArticleReplyBinder extends ItemViewBinder<ArticleReply, ArticleReplyBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleReply item) {

        cHtml.convert(holder.itemView.getContext(), holder.title, item.title);
        holder.itemView.setTag(item.editable);
        holder.name.setText(item.username);
        holder.time.setText(item.time);
        Glide.with(holder.itemView.getContext()).load(item.icon).into(holder.icon);
        FeedBackUtils.setOnclickfeedBack(0xFFFAFAFA,0xFFE2E2E2,holder.itemView);

    }


    class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {

        @BindView(R.id.adapter_list_title) RecyclerView title;
        @BindView(R.id.adapter_list_name) TextView name;
        @BindView(R.id.adapter_list_time) TextView time;
        @BindView(R.id.adapter_list_icon) ImageView icon;
        @BindView(R.id.adapter_list_reply) TextView reply;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnCreateContextMenuListener(this);
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            LogUtils.d(getAdapterPosition() + ":" + getLayoutPosition());
//            if (((Boolean) itemView.getTag())) {
//                menu.add(getAdapterPosition(), R.id.adapter_article_menu_edit, 0, "修改");//groupId, itemId, order, title
//            }
//            menu.add(getAdapterPosition(), R.id.adapter_article_menu_reply, 0, "回复");
//            menu.add(getAdapterPosition(), R.id.adapter_article_menu_up, 0, "顶");
//            menu.add(getAdapterPosition(), R.id.adapter_article_menu_user, 0, "查看用户");
        }
    }
}
