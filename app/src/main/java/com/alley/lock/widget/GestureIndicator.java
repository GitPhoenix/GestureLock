package com.alley.lock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.alley.lock.R;


/**
 * 手势密码图案提示
 */
public class GestureIndicator extends View {
    private int numRow = 3;    // 行
    private int numColumn = 3; // 列
    private int patternWidth = 40;
    private int patternHeight = 40;
    private int f = 5;
    private int g = 5;
    private int strokeWidth = 3;
    private Paint paint = null;
    private Drawable patternNoraml = null;
    private Drawable patternPressed = null;
    private String lockPassStr; // 手势密码

    public GestureIndicator(Context paramContext) {
        super(paramContext);
    }

    public GestureIndicator(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet, 0);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        patternNoraml = getResources().getDrawable(R.drawable.img_gesture_indicator_unchecked);
        patternPressed = getResources().getDrawable(R.drawable.img_gesture_indicator_checked);
        if (patternPressed != null) {
            patternWidth = patternPressed.getIntrinsicWidth();
            patternHeight = patternPressed.getIntrinsicHeight();
            this.f = (patternWidth / 4);
            this.g = (patternHeight / 4);
            patternPressed.setBounds(0, 0, patternWidth, patternHeight);
            patternNoraml.setBounds(0, 0, patternWidth, patternHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((patternPressed == null) || (patternNoraml == null)) {
            return;
        }
        // 绘制3*3的图标
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numColumn; j++) {
                paint.setColor(-16777216);
                int i1 = j * patternHeight + j * this.g;
                int i2 = i * patternWidth + i * this.f;
                canvas.save();
                canvas.translate(i1, i2);
                String curNum = String.valueOf(numColumn * i + (j + 1));
                if (!TextUtils.isEmpty(lockPassStr)) {
                    if (lockPassStr.indexOf(curNum) == -1) {
                        // 未选中
                        patternNoraml.draw(canvas);
                    } else {
                        // 被选中
                        patternPressed.draw(canvas);
                    }
                } else {
                    // 重置状态
                    patternNoraml.draw(canvas);
                }
                canvas.restore();
            }
        }
    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        if (patternPressed != null) {
            setMeasuredDimension(numColumn * patternHeight + this.g * (-1 + numColumn), numRow * patternWidth + this.f * (-1 + numRow));
        }
    }

    /**
     * 请求重新绘制
     *
     * @param paramString 手势密码字符序列
     */
    public void setPath(String paramString) {
        lockPassStr = paramString;
        invalidate();
    }
}