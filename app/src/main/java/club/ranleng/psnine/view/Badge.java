package club.ranleng.psnine.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Badge extends View {

    private Paint paint;

    public Badge(Context context) {
        super(context);
    }

    public Badge(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Badge(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(getResources().getColor(android.R.color.holo_red_light));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth() / 2;
        int h = getHeight() / 2;
        int r = Math.max(w, h);
        canvas.drawCircle(w, h, r, paint);
    }
}
