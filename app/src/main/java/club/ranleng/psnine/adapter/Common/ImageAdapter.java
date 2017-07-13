package club.ranleng.psnine.adapter.Common;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.Assist.FragActivity;
import club.ranleng.psnine.model.Common.Image;
import club.ranleng.psnine.model.KEY;
import club.ranleng.psnine.widget.GlideInToImageView;
import me.drakeet.multitype.ItemViewBinder;

/**
 * Created by ran on 10/07/2017.
 */

public class ImageAdapter extends ItemViewBinder<Image, ImageAdapter.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Image item) {
        holder.itemView.setTag(item.url);
        final Context context = holder.itemView.getContext();
//            Glide.with(context).load(item.url).into(holder.root);
        new GlideInToImageView(context, item.url, holder.root);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FragActivity.class);
                intent.putExtra("key", KEY.IMAGE);
                intent.putExtra("url", (String) v.getTag());
                context.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_image_root) ImageView root;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
