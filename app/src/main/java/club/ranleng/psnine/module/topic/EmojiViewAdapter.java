package club.ranleng.psnine.module.topic;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.Utils;

import java.util.List;

import club.ranleng.psnine.utils.EmojiUtils;
import cn.dreamtobe.kpswitch.util.KeyboardUtil;

public class EmojiViewAdapter extends RecyclerView.Adapter<EmojiViewAdapter.ViewHolder> {

    public onClick onEmojiClick;
    private List<Integer> emoji_list = EmojiUtils.getEmojiID();

    public EmojiViewAdapter(onClick onClick) {
        this.onEmojiClick = onClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setPadding(6, 6, 6, 6);
        int size = KeyboardUtil.getKeyboardHeight(Utils.getContext()) / 6;
        imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drawable drawable;
        int res_id = emoji_list.get(position);
        Resources resources = holder.itemView.getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = resources.getDrawable(res_id, null);
        } else {
            drawable = ResourcesCompat.getDrawable(resources, res_id, null);
        }
        holder.itemView.setTag(res_id);
        ((ImageView) holder.itemView).setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        return (emoji_list == null) ? 0 : emoji_list.size();
    }

    public interface onClick {
        void click(String emoji_name);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (onEmojiClick != null) {
                onEmojiClick.click(EmojiUtils.getEmojiName((int) v.getTag()));
            }
        }
    }
}
