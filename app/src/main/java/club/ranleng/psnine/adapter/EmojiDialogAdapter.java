package club.ranleng.psnine.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;

public class EmojiDialogAdapter extends RecyclerView.Adapter<EmojiDialogAdapter.ViewHolder> {

    String[] name = {"委屈","无视","撒娇","害羞","石化","流泪","闭嘴","囧","抽烟","捂嘴","晕菜","喝茶","+1",
            "卖萌","认真","哭","吃屎","大神","墨镜","冒光","口水","鼻血","瞎","吃瘪","眼镜","气愤","中箭",
            "DOGE","大笑","坏笑","XD","NB","渣","憨笑","调皮","喜欢","流汗","犯困","大汗","惊","虚汗","委屈",
            "叉","方块","三角","圆圈","上","下","左","右","D_PAD","Lone","Ltwo","Lthree","Rone","Rtwo",
            "Rthree","SELECT","START","PS","OPTION","SHARE","T_PAD","LS","RS","LS_上","LS_右上","LS_右",
            "LS_右下","LS_下","LS_左下","LS_左","LS_左上","RS_上","RS_右上","RS_右","RS_右下","RS_下",
            "RS_左下","RS_左","RS_左上","阿鲁憨笑","阿鲁皱眉","阿鲁不开心","阿鲁阴笑","阿鲁吃惊","阿鲁懵逼",
            "阿鲁委屈","阿鲁茫然","阿鲁XD","阿鲁崇拜","阿鲁淫笑","阿鲁獠牙","阿鲁哭","阿鲁茫茫然","阿鲁脸红",
            "阿鲁亲亲","阿鲁出汗","阿鲁瞌睡","阿鲁墨镜","阿鲁抠鼻","阿鲁吃糖","阿鲁出血","阿鲁口水","阿鲁吐了",
            "阿鲁鼻涕","阿鲁绷带","阿鲁吐舌","阿鲁闭嘴","阿鲁扶镜","阿鲁打码","阿鲁吐血","阿鲁冒火","阿鲁冻结",
            "阿鲁挂了","阿鲁点赞","阿鲁异议","阿鲁无奈","阿鲁开森","阿鲁捂脸","阿鲁害羞","阿鲁脸疼","阿鲁琢磨",
            "阿鲁鼓掌","阿鲁DOGE"};

    Integer[] key = { R.raw.weiqu,R.raw.wushi,R.raw.sajiao,R.raw.haixiu,R.raw.shihua,R.raw.liulei,
            R.raw.bizui,R.raw.jiong,R.raw.chouyan,R.raw.wuzui,R.raw.yuncai,R.raw.hecha,R.raw.plusone,
            R.raw.maimeng,R.raw.renzhen,R.raw.ku,R.raw.chishi,R.raw.dashen,R.raw.mojing,R.raw.maoguang,
            R.raw.koushui,R.raw.bixue,R.raw.xia,R.raw.chibie,R.raw.yanjing,R.raw.qifen,R.raw.zhongjian,
            R.raw.doge,R.raw.daxiao,R.raw.huaixiao,R.raw.xd,R.raw.nb,R.raw.zha,R.raw.hanxiao,R.raw.tiaopi,
            R.raw.xihuan,R.raw.liuhan,R.raw.fankun,R.raw.dahan,R.raw.jing,R.raw.xuhan,R.raw.weiqu,
            R.raw.pscha,R.raw.psfangkuai,R.raw.pssanjiao,R.raw.psyuanquan,R.raw.psshang,R.raw.psxia,
            R.raw.pszuo,R.raw.psyou,R.raw.psdpad,R.raw.pslone,R.raw.psltwo,R.raw.pslthree,R.raw.psrone,
            R.raw.psrtwo,R.raw.psrthree,R.raw.psselect,R.raw.psstart,R.raw.psps,R.raw.psoption,
            R.raw.psshare,R.raw.pstpad,R.raw.psls,R.raw.psrs,R.raw.pslsshang,R.raw.pslsyoushang,
            R.raw.pslsyou,R.raw.pslsyouxia,R.raw.pslsxia,R.raw.pslszuoxia,R.raw.pslszuo,R.raw.pslszuoshang,
            R.raw.psrsshang,R.raw.psrsyoushang,R.raw.psrsyou,R.raw.psrsyouxia,R.raw.psrsxia,R.raw.psrszuoxia,
            R.raw.psrszuo,R.raw.psrszuoshang,R.raw.aluhanxiao,R.raw.aluzhoumei,R.raw.alubukaixin,
            R.raw.aluyinxiao,R.raw.aluchijing,R.raw.alumengbi,R.raw.aluweiqu,R.raw.alumangran,R.raw.aluxd,
            R.raw.aluchongbai,R.raw.aluyinxiao,R.raw.aluliaoya,R.raw.aluku,R.raw.alumangmangran,
            R.raw.alulianhong,R.raw.aluqinqin,R.raw.aluchuhan,R.raw.alukeshui,R.raw.alumojing,
            R.raw.alukoubi,R.raw.aluchitang,R.raw.aluchuxue,R.raw.alukoushui,R.raw.alutule,R.raw.alubiti,
            R.raw.alubengdai,R.raw.alutushe,R.raw.alubizui,R.raw.alufujing,R.raw.aludama,R.raw.alutuxue,
            R.raw.alumaohuo,R.raw.aludongjie,R.raw.aluguale,R.raw.aludianzan,R.raw.aluyiyi,R.raw.aluwunai,
            R.raw.alukaisen,R.raw.aluwulian,R.raw.aluhaixiu,R.raw.alulianteng,R.raw.aluzuomo,R.raw.aluguzhang,R.raw.aludoge};

    public EmojiDialogAdapter() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_emoji_dialog_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.icon.setImageDrawable(holder.itemView.getResources().getDrawable(key[position],null));
    }

    @Override
    public int getItemCount() {
        return (key == null) ? 0 : key.length;
    }

    public interface onEmojiClick{
        void onClick(String name);
    }

    public void setClickListener(onEmojiClick clickListener) {
        this.onEmojiClick = clickListener;
    }
    private onEmojiClick onEmojiClick;

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.emoji_image_view) ImageView icon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onEmojiClick != null){
                onEmojiClick.onClick(name[getAdapterPosition()]);
            }
        }
    }
}
