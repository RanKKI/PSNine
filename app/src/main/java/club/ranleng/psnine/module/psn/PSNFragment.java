package club.ranleng.psnine.module.psn;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.ranleng.psnine.R;
import club.ranleng.psnine.common.UserState;
import club.ranleng.psnine.view.RoundImageView;

public class PSNFragment extends Fragment implements PSNContract.View {

    @BindView(R.id.psn_root) CoordinatorLayout root;
    @BindView(R.id.app_bar_image) ImageView app_bar_image;
    @BindView(R.id.appbar) AppBarLayout appBarLayout;
    @BindView(R.id.icon) RoundImageView icon;
    @BindView(R.id.region) RoundImageView region;
    @BindView(R.id.plus) RoundImageView plus;
    @BindView(R.id.auth) RoundImageView auth;

    private PSNContract.Presenter mPresenter;
    private String psnid;

    public static PSNFragment newInstance(String psnid) {
        PSNFragment fragment = new PSNFragment();
        Bundle bundle = new Bundle();
        bundle.putString("psnid", psnid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PSNPresenter(this);
        psnid = getArguments().getString("psnid");
        getActivity().setTitle(psnid);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_psn, container, false);
        ButterKnife.bind(this, view);
        appBarLayout.setExpanded(false);
        mPresenter.start();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.psn, menu);
        menu.findItem(R.id.activity_psn_block).setVisible(UserState.isLogin());
        menu.findItem(R.id.activity_psn_fav).setVisible(UserState.isLogin());
        menu.findItem(R.id.activity_psn_up).setVisible(UserState.isLogin());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return mPresenter.MenuSelect(item.getItemId());
    }

    private void show_snackBar(String s) {
        Snackbar.make(root, s, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(PSNContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.inside_frame, fragment).commit();
    }

    @Override
    public void Sncak_Success() {
        show_snackBar(getString(R.string.success));
    }

    @Override
    public void confirm(final int type, String message, final PSNFragment.DialogClickListener listener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.onCick(type, true);
                    }
                })
                .setNegativeButton("取消", null).create().show();
    }

    @Override
    public void setIcon(Map<String, String> map) {
        Glide.with(this).load(map.get("bg")).asBitmap().into(app_bar_image);
        Glide.with(this).load(map.get("icon")).asBitmap().into(icon);
        Glide.with(this).load(map.get("region")).asBitmap().into(region);
        Glide.with(this).load(map.get("auth")).asBitmap().into(auth);
        Glide.with(this).load(map.get("plus")).asBitmap().into(plus);
    }

    @Override
    public String getPSNID() {
        return psnid;
    }


    public interface DialogClickListener {
        void onCick(int type, Boolean b);
    }
}
