package club.ranleng.psnine.module.photo;

import android.content.pm.PackageManager;
import android.view.View;

import java.io.File;
import java.util.Map;

import club.ranleng.psnine.R;
import club.ranleng.psnine.common.KEY;
import club.ranleng.psnine.common.multitype.binder.ImageGalleryBinder;
import club.ranleng.psnine.common.multitype.model.Image;
import club.ranleng.psnine.data.moudle.SimpleCallBack;
import club.ranleng.psnine.data.remote.ApiManager;
import club.ranleng.psnine.data.moudle.SimpleSubCallBack;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class PhotoGalleryPresenter implements PhotoGalleryContract.Presenter,
        SimpleSubCallBack<Map<String, Object>>, ImageGalleryBinder.OnClick {

    private PhotoGalleryContract.View mPhotoView;
    private MultiTypeAdapter adapter = new MultiTypeAdapter();
    private Items items = new Items();

    public PhotoGalleryPresenter(PhotoGalleryContract.View view) {
        this.mPhotoView = view;
        this.mPhotoView.setPresenter(this);

        adapter.register(Image.class, new ImageGalleryBinder(this, mPhotoView.getPhotos()));
        adapter.setItems(items);
    }

    @Override
    public void start() {
        load();
    }

    @Override
    public void upload(File file) {
        ApiManager.getDefault().uploadPhoto(new SimpleCallBack() {
            @Override
            public void Success() {
                load();
            }

            @Override
            public void Failed() {

            }
        },file);
    }

    @Override
    public void menuSelected(int id) {
        if (id == R.id.action_upload_local) {
            if(mPhotoView.getPermissions()){
                mPhotoView.openGallery();
            }
        } else if (id == R.id.action_upload_camera) {
            mPhotoView.openCamera();
        }
    }

    @Override
    public void load() {
        items.clear();
        adapter.notifyItemRangeRemoved(0, items.size() - 1);
        ApiManager.getDefault().getPhotos(this);
    }

    @Override
    public void delPhoto(String id, int pos) {
        ApiManager.getDefault().delPhoto(id);
        items.remove(pos);
        adapter.notifyItemRemoved(pos);
    }

    @Override
    public void requestPermissions(int requestCode, int[] grantResults) {
        if(requestCode == KEY.REQUEST_PERMISSION_READ_EXTERNAL){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPhotoView.openGallery();
            }
        }
    }

    @Override
    public void onStart() {
        mPhotoView.setLoading(true);
    }

    @Override
    public void onNext(Map<String, Object> map) {
        items.add(new Image((String) map.get("url"), (String) map.get("id")));
    }

    @Override
    public void onComplete() {
        mPhotoView.setLoading(false);
        mPhotoView.setPhotoGallery(adapter);
    }

    @Override
    public void onClick(View v, String url) {
        mPhotoView.onClick(v, url);
    }

    @Override
    public void onLongClick(View v, String id, int pos) {
        mPhotoView.onLongClick(id, pos);
    }
}
