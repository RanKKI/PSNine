package club.ranleng.psnine.module.photo;

import java.io.File;
import java.util.List;

import club.ranleng.psnine.base.BasePresenter;
import club.ranleng.psnine.base.BaseView;
import me.drakeet.multitype.MultiTypeAdapter;

public interface PhotoGalleryContract {


    interface View extends BaseView<Presenter> {

        void setPhotoGallery(MultiTypeAdapter adapter);

        void setLoading(Boolean b);

        List<String> getPhotos();

        Boolean getPermissions();

        //Local Photo upload
        void openGallery();

        //Take photo and upload
        void openCamera();

        void onClick(android.view.View view, String url);

        void onLongClick(String id, int pos);
    }

    interface Presenter extends BasePresenter {


        void upload(File file);

        void menuSelected(int id);

        void load();

        void delPhoto(String id, int pos);

        void requestPermissions(int requestCode, int[] grantResults);

    }

}
