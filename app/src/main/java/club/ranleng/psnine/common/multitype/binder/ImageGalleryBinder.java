package club.ranleng.psnine.common.multitype.binder;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.model.Image;
import me.drakeet.multitype.ItemViewBinder;


public class ImageGalleryBinder extends ItemViewBinder<Image, ImageGalleryBinder.ViewHolder> {

    private OnClick onClick;
    private ArrayList<String> data;

    public ImageGalleryBinder(OnClick onClick, ArrayList<String> data) {
        this.onClick = onClick;
        this.data = data;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_images, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Image item) {
        Context context = holder.itemView.getContext();
        String p_url = item.url.replace("http://ww4.sinaimg.cn/thumb150/", "").replace(".jpg", "");
        holder.root.setTag(R.id.TAG_IMAGE_GALLERY_URL, p_url);
        holder.root.setTag(R.id.TAG_IMAGE_GALLERY_ID, item.id);
        if (data != null && data.contains(p_url)) {
            holder.click.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(item.url)
                .crossFade()
                .thumbnail(0.1f)
                .into(holder.root);
    }

    public interface OnClick {
        void onClick(View v, String url);

        void onLongClick(View v, String id, int pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.view_image_root) ImageView root;
        @BindView(R.id.view_image_click_green) ImageView click;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            root.setOnClickListener(this);
            root.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClick != null) {
                onClick.onClick(click, (String) v.getTag(R.id.TAG_IMAGE_GALLERY_URL));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onClick != null) {
                onClick.onLongClick(v, (String) v.getTag(R.id.TAG_IMAGE_GALLERY_ID), getAdapterPosition());
            }
            return false;
        }
    }
}
