package club.ranleng.psnine.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.util.LogUtil;

/**
 * Created by ran on 02/07/2017.
 */

public class PickImgAdapter extends RecyclerView.Adapter<PickImgAdapter.ViewHolder>{

    private ArrayList<Map<String, Object>> mDatas;


    public PickImgAdapter(ArrayList<Map<String, Object>> data){
        mDatas = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_photo_item,parent,false);
        return new ViewHolder(view);
    }

    public void rmData(int p){
        mDatas.remove(p);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> map = mDatas.get(position);
        Glide.with(holder.itemView.getContext()).load(map.get("url")).into(holder.photoview);
        String rel = map.get("url").toString().replace("http://ww4.sinaimg.cn/thumb150/","").replace(".jpg","");

        holder.photoview.setTag(R.id.tag_pick_img_first,map.get("id"));
        holder.photoview.setTag(R.id.tag_pick_img_second,rel);
    }
    private OnItemClickListener clickListener;
    public interface OnItemClickListener {
        void onClick(View view, View root,int position);
        void onLongClick(View view, int position);
    }
    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return (mDatas == null) ? 0 : mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        @BindView(R.id.photoview) ImageView photoview;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            photoview.setOnClickListener(this);
            photoview.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onClick(photoview,itemView, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (clickListener != null) {
                clickListener.onLongClick(photoview, getAdapterPosition());
            }
            return true;
        }
    }
}
