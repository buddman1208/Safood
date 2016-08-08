package kr.edcan.safood.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import kr.edcan.safood.R;

/**
 * Created by JunseokOh on 2016. 8. 6..
 */
public class CartaTagView extends TextView {
    boolean fullMode = false;
    int color = Color.BLACK;
    int height, width;

    private Paint innerPaint, bgPaint;

    public CartaTagView(Context context) {
        super(context);
    }

    public CartaTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(attrs);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CartaTagView);
        setTypedArray(array);
    }

    private void setTypedArray(TypedArray array) {
        fullMode = array.getBoolean(R.styleable.CartaTagView_fullMode, false);
        color = array.getColor(R.styleable.CartaTagView_themeColor, Color.BLACK);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setView() {
        setTextColor((fullMode) ? Color.WHITE : color);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setView();
        bgPaint = new Paint();
        bgPaint.setColor(color);
        bgPaint.setStyle(Paint.Style.FILL);
        innerPaint = new Paint();
        innerPaint.setColor(Color.WHITE);
        innerPaint.setStyle(Paint.Style.FILL);
        Point center = new Point(width / 2, height / 2);
        int strokeWidth = getResources().getDimensionPixelSize(R.dimen.stroke_width);
        int innerH = height - strokeWidth;
        int innerW = width - strokeWidth;

        int left = center.x - (innerW / 2);
        int top = center.y - (innerH / 2);
        int right = center.x + (innerW / 2);
        int bottom = center.y + (innerH / 2);

        RectF bgRect = new RectF(0.0f, 0.0f, width, height);
        RectF innerRect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(bgRect, height / 2, height / 2, bgPaint);
        if (!fullMode)
            canvas.drawRoundRect(innerRect, innerH / 2, innerH / 2, innerPaint);
        super.onDraw(canvas);
    }

    public void setShapeStyle(boolean fullMode, int color) {
        this.color = color;
        this.fullMode = fullMode;
        requestLayout();
    }


    public void setFullMode(boolean fullMode) {
        this.fullMode = fullMode;
        requestLayout();
    }

    public void setShapeStyle(boolean fullMode, String colorStr) {
        this.color = Color.parseColor(colorStr);
        requestLayout();
    }

}
