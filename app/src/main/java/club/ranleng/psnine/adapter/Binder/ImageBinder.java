package club.ranleng.psnine.adapter.Binder;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.activity.FragActivity;
import club.ranleng.psnine.model.Image;
import club.ranleng.psnine.model.Image_Gene;
import club.ranleng.psnine.utils.ViewGroupUtils;
import club.ranleng.psnine.widget.KEY;
import me.drakeet.multitype.ItemViewBinder;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;


public class ImageBinder extends ItemViewBinder<Image_Gene, ImageBinder.ViewHolder> {


    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.view_recycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, @NonNull final Image_Gene item) {
        Items items = new Items();
        ArrayList<String> photo_list = new ArrayList<>();
        MultiTypeAdapter adapter = new MultiTypeAdapter();
        adapter.register(Image.class, new ImageGalleryBinder(new ImageGalleryBinder.OnClick() {
            @Override
            public void onClick(View v, String url) {
                Intent intent = new Intent(v.getContext(), FragActivity.class);
                intent.putExtra("key", KEY.IMAGE);
                intent.putExtra("url", url);
                v.getContext().startActivity(intent);
            }

            @Override
            public void onLongClick(View v, String id, int pos) {

            }
        }, photo_list));

        for (String url : item.imgs) {
            items.add(new Image(url.replace("large", "square"), null));
        }

        adapter.setItems(items);
        holder.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
        holder.recyclerView.setAdapter(adapter);
        int row = item.imgs.size() / 4;
        LogUtils.d(item.imgs.size());
        if (row != 0 && item.imgs.size() % 4 != 0) {
            row += 1;
        }
        holder.swipeRefreshLayout.getLayoutParams().height = ViewGroupUtils.dpToPx(row * 105);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recyclerview) RecyclerView recyclerView;
        @BindView(R.id.swiperefresh) SwipeRefreshLayout swipeRefreshLayout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            swipeRefreshLayout.setEnabled(false);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), FragActivity.class);
            intent.putExtra("key", KEY.IMAGE);
            intent.putExtra("url", (String) v.getTag(R.id.TAG_IMAGE_URL));
            v.getContext().startActivity(intent);
        }
    }
}
