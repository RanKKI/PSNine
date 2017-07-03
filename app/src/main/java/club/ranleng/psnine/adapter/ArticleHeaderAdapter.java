package club.ranleng.psnine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
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
import club.ranleng.psnine.model.ArticleHeader;
import club.ranleng.psnine.widget.HtmlImageGetter;
import club.ranleng.psnine.widget.HtmlTagHandler;
import me.drakeet.multitype.ItemViewBinder;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

/**
 * Created by ran on 01/07/2017.
 */

public class ArticleHeaderAdapter extends ItemViewBinder<ArticleHeader, ArticleHeaderAdapter.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adapter_article_header,parent,false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ArticleHeader item) {
        Context context = holder.itemView.getContext();
        Spanned spanned;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(item.content, FROM_HTML_MODE_LEGACY, new HtmlImageGetter(context, holder.content), new HtmlTagHandler(context));
        } else {
            spanned = Html.fromHtml(item.content, new HtmlImageGetter(context, holder.content), new HtmlTagHandler(context));
        }
        holder.content.setText(spanned);
        holder.content.setMovementMethod(LinkMovementMethod.getInstance());
        holder.time.setText(item.time);
        holder.replies.setText(item.replies);
        holder.username.setText(item.username);
        Glide.with(context).load(item.icon).into(holder.icon);
        holder.imgs_layout.removeAllViews();
        for(String i : item.img){
            ImageView imageView = new ImageView(context);
            Glide.with(context).load(i).into(imageView);
            holder.imgs_layout.addView(imageView);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.header_content) TextView content;
        @BindView(R.id.header_time) TextView time;
        @BindView(R.id.header_replies) TextView replies;
        @BindView(R.id.header_username) TextView username;
        @BindView(R.id.header_icon) ImageView icon;
        @BindView(R.id.header_images_layout) LinearLayout imgs_layout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
