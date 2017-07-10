package club.ranleng.psnine.adapter.Article;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Article.ArticleHeader;
import club.ranleng.psnine.util.LogUtils;
import club.ranleng.psnine.widget.HTML.CmHtml;
import me.drakeet.multitype.ItemViewBinder;

import static android.text.Html.fromHtml;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleHeaderAdapter extends ItemViewBinder<ArticleHeader, ArticleHeaderAdapter.ViewHolder> {

    public ArticleHeaderAdapter (OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_article_header,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleHeader item) {
        Context context = holder.itemView.getContext();
        CmHtml.convert(context,holder.content,item.content);
        holder.time.setText(item.time);
        holder.replies.setText(item.replies);
        holder.username.setText(item.username);
        Glide.with(context).load(item.icon).into(holder.icon);
        holder.itemView.setTag(R.id.tag_header_editable,item.editable);
        if(holder.imgs_layout.getTag() == null){
            holder.imgs_layout.setTag(R.id.tag_header_imgs,"");
            for(String i : item.img){
                ImageView imageView = new ImageView(context);
                Glide.with(context).load(i).into(imageView);
                holder.imgs_layout.addView(imageView);
            }
        }
    }

    private OnItemClickListener clickListener;
    public interface OnItemClickListener {
        void onHeaderClick(View view);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.header_content) RecyclerView content;
        @BindView(R.id.header_time) TextView time;
        @BindView(R.id.header_replies) TextView replies;
        @BindView(R.id.header_username) TextView username;
        @BindView(R.id.header_icon) ImageView icon;
        @BindView(R.id.article_header_dot) ImageView dot;
        @BindView(R.id.header_images_layout) LinearLayout imgs_layout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            dot.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.onHeaderClick(itemView);
            }
        }
    }
}
