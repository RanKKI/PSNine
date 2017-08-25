package club.ranleng.psnine.module.newtopic.Gene;

import club.ranleng.psnine.data.moudle.SimpleCallBack;
import club.ranleng.psnine.data.remote.ApiManager;

public class newGenePresenter implements newGeneContract.Presenter {

    private newGeneContract.View mNewGeneView;

    public newGenePresenter(newGeneContract.View view) {
        this.mNewGeneView = view;
        this.mNewGeneView.setPresenter(this);
    }

    @Override
    public void start() {
        mNewGeneView.setup();
    }

    @Override
    public void submit() {
        ApiManager.getDefault().newGene(new SimpleCallBack() {
            @Override
            public void Success() {
                mNewGeneView.finish();
            }

            @Override
            public void Failed() {

            }
        }, mNewGeneView.getData());
    }
}
