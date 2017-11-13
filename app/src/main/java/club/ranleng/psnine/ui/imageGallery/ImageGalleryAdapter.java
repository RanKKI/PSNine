package club.ranleng.psnine.ui.imageGallery;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.model.Images;

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.ViewHolder> {

    private Images images;
    private OnClick onClick;
    private Context context;
    private List<String> photos = new ArrayList<>();

    ImageGalleryAdapter(Context context, List<String> list, OnClick onClick) {
        this.onClick = onClick;
        this.context = context;
        if (list != null) {
            photos.addAll(list);
        }
    }

    void delete(int pos) {
        images.getItems().remove(pos);
        notifyItemRemoved(pos);
    }

    void update(Images images) {
        this.images = images;
        notifyItemRangeChanged(0, getItemCount());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_image_gallery_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        String urlKey = images.getItems().get(position).getUrlKey();
        holder.root.setTag(R.id.TAG_IMAGE_GALLERY_URL, urlKey);
        holder.root.setTag(R.id.TAG_IMAGE_GALLERY_ID, images.getItems().get(position).getId());
        if (photos != null && photos.contains(urlKey)) {
            holder.click.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(images.getItems().get(position).getUrl())
                .thumbnail(0.1f)
                .into(holder.root);
    }

    @Override
    public int getItemCount() {
        if (images == null) {
            return 0;
        }
        return images.getItems() == null ? 0 : images.getItems().size();
    }

    public interface OnClick {
        void onClick(View v, View root, String url);

        void onLongClick(View v, String id, int pos, String url);
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
                onClick.onClick(click, root, (String) v.getTag(R.id.TAG_IMAGE_GALLERY_URL));
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onClick != null) {
                onClick.onLongClick(v, (String) v.getTag(R.id.TAG_IMAGE_GALLERY_ID), getAdapterPosition(), (String) v.getTag(R.id.TAG_IMAGE_GALLERY_URL));
                return true;
            }
            return false;
        }
    }
}
