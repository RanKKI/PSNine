package club.ranleng.psnine.common.multitype.binder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.multitype.model.PSNGameUser;
import me.drakeet.multitype.ItemViewBinder;


public class PSNGameUserBinder extends ItemViewBinder<PSNGameUser, PSNGameUserBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.adatper_psngame_user_item,null);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull PSNGameUser item) {
        holder.username.setText(item.username);
        holder.percentage.setText(item.percentage);
        holder.ft.setText(item.ft);
        holder.lt.setText(item.lt);
        holder.tt.setText(item.tt);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.psngame_user_username) TextView username;
        @BindView(R.id.psngame_user_percentage) TextView percentage;
        @BindView(R.id.psngame_user_ft) TextView ft;
        @BindView(R.id.psngame_user_lt) TextView lt;
        @BindView(R.id.psngame_user_tt) TextView tt;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
