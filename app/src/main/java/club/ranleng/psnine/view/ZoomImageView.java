package club.ranleng.psnine.view;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;

@SuppressLint("NewApi")
public class ZoomImageView extends android.support.v7.widget.AppCompatImageView implements OnGlobalLayoutListener,
        OnScaleGestureListener, OnTouchListener {

    /**
     * 自由的放大 和 缩小 放大 可以 自由的 移动 处理 和viewpager 事件冲突
     *
     *
     * 1.Matrix 2.ScaleGestureDetector 3.GestureDetector 4.事件分发机制
     *
     *
     * 1.实现
     *
     */

    // 第一次运行 初始化
    private boolean isOnce = false;
    // 缩放比例
    private float initScale;
    private float minScale;
    private float maxScale;

    // 缩放实现
    private Matrix matrix;

    // 多点触控
    private ScaleGestureDetector scaleGestureDetector;

    // 自由移动的比较值
    private int touchSlop;

    // 双击 放大缩小
    private GestureDetector gestureDetector;

    // 判断双击中
    private boolean isDoubletag = false;

    /**
     * 重写 3个 构造方法
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //
        matrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        // 初始化操作写在 3个参数的 构造函数里

        scaleGestureDetector = new ScaleGestureDetector(context, this);
        setOnTouchListener(this);

        // 初始化 比较值
        touchSlop = ViewConfiguration.get(context).getScaledDoubleTapSlop();

        // 双击放大缩小
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        // 双击事件
                        if (isDoubletag) {
                            return isDoubletag;
                        }
                        float x = e.getX();
                        float y = e.getY();
                        if (getScale() < minScale) {

                            postDelayed(new AutoScaleRunnable(minScale, x, y),
                                    16);
                            isDoubletag = true;
                        } else {
                            postDelayed(new AutoScaleRunnable(initScale, x, y),
                                    16);
                            isDoubletag = true;
                        }

                        return true;
                    }
                });
    }

    /**
     * 自动放大 缩小 缓慢的缩放
     *
     * @author yuan
     *
     */
    private class AutoScaleRunnable implements Runnable {

        private float mTargetScale;
        private float x;
        private float y;

        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;
        private float tmpScale;

        /**
         * 实现构造方法
         *
         * @param mTargetScale
         * @param x
         * @param y
         */
        public AutoScaleRunnable(float mTargetScale, float x, float y) {
            this.mTargetScale = mTargetScale;
            this.x = x;
            this.y = y;
            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            }
            if (getScale() > mTargetScale) {
                tmpScale = SMALL;
            }
        }

        @Override
        public void run() {
            //
            matrix.postScale(tmpScale, tmpScale, x, y);
            checkBorderAndCenterWhenScale();
            setImageMatrix(matrix);

            float currentScale = getScale();
            if ((tmpScale > 1.0f && currentScale < mTargetScale)
                    || (tmpScale < 1.0f && currentScale > mTargetScale)) {
                postDelayed(this, 16);
            } else {
                float scale = mTargetScale / currentScale;
                matrix.postScale(scale, scale, x, y);
                checkBorderAndCenterWhenScale();
                setImageMatrix(matrix);
                isDoubletag = false;
            }
        }

    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context) {
        this(context, null);
    }

    @Override
    protected void onAttachedToWindow() {
        // 注册 GlobalListener

        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onDetachedFromWindow() {
        // 移除 GlobalListener
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        // 初始化
        if (!isOnce) {
            // 控件的宽和高
            int width = getWidth();
            int height = getHeight();
            // 获得图片的宽和高
            Drawable d = getDrawable();
            if (d == null) {
                return;
            }

            int imgWidth = d.getIntrinsicWidth();
            int imgHeight = d.getIntrinsicHeight();

            // 缩放比例
            float scale = 1.0f;
            if (imgWidth > width && imgHeight < height) {

                scale = width * 1.0f / imgWidth;

            } else if (imgHeight > height && imgWidth < width) {

                scale = height * 1.0f / imgHeight;

            } else if ((imgWidth > width && imgHeight > height)
                    || (imgWidth < width && imgHeight < height)) {
                scale = Math.min(width * 1.0f / imgWidth, height * 1.0f
                        / imgHeight);
            }

            /**
             * 初始化 缩放比例
             */
            initScale = scale;
            maxScale = scale * 4;
            minScale = scale * 2;

            Log.i("TAG", scale + "");
            /**
             * 将图片移动到 图片中心
             *
             */
            int mx = width / 2 - imgWidth / 2;
            int my = height / 2 - imgHeight / 2;

            /**
             * Matrix 3*3 矩阵 xScale xskew xTrans yScale yskew yTrans 0 0 0
             *
             * 使用 post 操作
             */
            matrix.postTranslate(mx, my);
            matrix.postScale(initScale, initScale, width / 2, height / 2);
            setImageMatrix(matrix);

            isOnce = true;
        }

    }

    /**
     * 拿到当前图片的缩放值
     *
     * @return
     */
    public float getScale() {
        float[] values = new float[9];
        matrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 缩放比例 initScale maxScale
        float scaleFactor = detector.getScaleFactor();

        if (getDrawable() == null) {
            return true;
        }
        float scale = getScale();
        // 缩放控制
        if ((scale < maxScale && scaleFactor > 1.0f)
                || (scale > initScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < initScale) {
                scaleFactor = initScale / scale;
            } else if (scale * scaleFactor > maxScale) {
                scale = maxScale / scale;
            }
            matrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(),
                    detector.getFocusY());

            checkBorderAndCenterWhenScale();
            setImageMatrix(matrix);
        }

        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // 返回true
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        // TODO Auto-generated method stub

    }

    // ---------------------------------------------------------自由移动
    // 存储最后的位置
    private int lastPointCount;
    private float Lx;
    private float Ly;
    private boolean isDrag;
    private boolean isCheckLeftAndRight, isCheckTopAndBottom = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.i("TAG","onTouch"+1);
        // 双击的时候不让其 移动
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }

        // 设置 onTouch 事件
        scaleGestureDetector.onTouchEvent(event);

        // 自由移动实现
        float x = 0, y = 0;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        Log.i("TEST","pointerCount:"+pointerCount+"X:"+x+"Y:"+y);
        x /= pointerCount;
        y /= pointerCount;

        if (lastPointCount != pointerCount) {
            isDrag = false;
            Lx = x;
            Ly = y;

        }
        lastPointCount = pointerCount;

        RectF f = getMatrixRectF();

        // 处理事件冲突问题！！

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                Log.i("TAG","onTouch"+2);
                // 解决事件冲突
                // 当图片的 高度和宽度 大于屏幕的寬高度的时候，为图片的事件，否则为viewPager
                if (ischong(f)) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("TAG","onTouch"+3);
                // 解决事件冲突
                if (ischong(f)) {
                    if (getParent() instanceof ViewPager) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                // 正在移动
                float dx = x - Lx;
                float dy = y - Ly;
                if (!isDrag) {
                    isDrag = isMoveAction(dx, dy);
                }
                if (isDrag) {

                    if (getDrawable() != null) {
                        isCheckLeftAndRight = isCheckTopAndBottom = true;
                        // 如果高度 小于控件宽度，不允许横向移动
                        if (f.width() < getWidth()) {
                            isCheckLeftAndRight = false;
                            dx = 0;
                        }
                        if (f.height() < getHeight()) {
                            isCheckTopAndBottom = false;
                            dy = 0;
                        }
                        matrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(matrix);
                        setFocusable(true);
                    }
                }
                Ly = y;
                Lx = x;
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.i("TAG","onTouch"+4);
                lastPointCount = 0;
                break;
        }

        return true;
    }

    /**
     * 解决 事件冲突
     *
     * @param f
     * @return
     */
    private boolean ischong(RectF f) {
        return (f.width() > getWidth() + 0.01)
                || (f.height() > getHeight() + 0.01);
    }

    /**
     * 移动 判断边界
     */

    private void checkBorderWhenTranslate() {
        RectF rectf = getMatrixRectF();
        float dx = 0;
        float dy = 0;
        int width = getWidth();
        int height = getHeight();

        if (rectf.top > 0 && isCheckTopAndBottom) {
            dy = -rectf.top;
        }
        if (rectf.bottom < height && isCheckTopAndBottom) {
            dy = height - rectf.bottom;
        }

        if (rectf.right < width && isCheckLeftAndRight) {
            dx = width - rectf.right;
        }
        if (rectf.left > 0 && isCheckLeftAndRight) {
            dx = -rectf.left;
        }
        matrix.postTranslate(dx, dy);
    }

    /**
     * 判断是否移动
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isMoveAction(float dx, float dy) {

        return Math.sqrt(dx * dx + dy * dy) > touchSlop;
    }

    // ------------------------------------------------比例缩放
    /**
     * 获得 图片放大缩小 以后的宽和高
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix smatrix = matrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();

        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            smatrix.mapRect(rectF);
        }
        return rectF;
    }

    /**
     * 防止缩放时，出现白边
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rectf = getMatrixRectF();
        float dx = 0;
        float dy = 0;
        int width = getWidth();
        int height = getHeight();
        // 防止出现 白边
        if (rectf.width() >= width) {
            if (rectf.left > 0) {
                dx = -rectf.left;
            }
            if (rectf.right < width) {
                dx = width - rectf.right;
            }

        }
        if (rectf.height() >= height) {
            if (rectf.top > 0) {
                dy = -rectf.top;
            }
            if (rectf.bottom < height) {
                dy = height - rectf.bottom;
            }
        }

        // 如果 宽度和高度 小于 控件宽度和高度，则让其 居中；
        if (rectf.width() < width) {
            dx = width / 2 - rectf.right + rectf.width() / 2;
        }

        if (rectf.height() < height) {
            dy = height / 2 - rectf.bottom + rectf.height() / 2;
        }

        matrix.postTranslate(dx, dy);
    }

}