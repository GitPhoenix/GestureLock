package com.alley.lock.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alley.lock.R;
import com.alley.lock.model.GestureDotModel;
import com.alley.lock.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 手势密码容器类
 */
public class GestureView extends ViewGroup {
    private Context context;

    private int baseNum = 6;
    private int[] screenDisplay;

    /**
     * 每个点区域的宽度
     */
    private int blockWidth;
    /**
     * 声明一个集合用来封装坐标集合
     */
    private List<GestureDotModel> dotlist;
    private GestureLine gestureLine;

    /**
     * 包含9个ImageView的容器，初始化
     *
     * @param context
     * @param passWord 用户传入密码
     */
    public GestureView(Context context, String passWord) {
        super(context);
        this.dotlist = new ArrayList<>();
        this.context = context;

        // 初始化一个可以画线的view
        gestureLine = new GestureLine(context, dotlist, passWord);
    }

    public void setParentView(ViewGroup parent) {
        screenDisplay = ScreenUtils.getScreenValue(context);
        blockWidth = (screenDisplay[0] - parent.getPaddingRight() - parent.getPaddingLeft()) / 3;
        // 添加9个图标
        addChild();

        LayoutParams layoutParams = new LayoutParams(screenDisplay[0], screenDisplay[0]);
        this.setLayoutParams(layoutParams);
        gestureLine.setLayoutParams(layoutParams);
        parent.addView(gestureLine);
        parent.addView(this);
    }

    private void addChild() {
        for (int i = 0; i < 9; i++) {
            ImageView image = new ImageView(context);
            image.setImageResource(R.drawable.img_gesture_normal);
            this.addView(image);
            invalidate();
            // 第几行
            int row = i / 3;
            // 第几列
            int col = i % 3;
            // 定义点的每个属性
            int leftX = col * blockWidth + blockWidth / baseNum;
            int topY = row * blockWidth + blockWidth / baseNum;
            int rightX = col * blockWidth + blockWidth - blockWidth / baseNum;
            int bottomY = row * blockWidth + blockWidth - blockWidth / baseNum;
            GestureDotModel p = new GestureDotModel(leftX, rightX, topY, bottomY, image, i + 1);
            this.dotlist.add(p);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            //第几行
            int row = i / 3;
            //第几列
            int col = i % 3;
            View v = getChildAt(i);
            v.layout(col * blockWidth + blockWidth / baseNum, row * blockWidth + blockWidth / baseNum, col * blockWidth + blockWidth - blockWidth / baseNum, row * blockWidth + blockWidth - blockWidth / baseNum);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 遍历设置每个子view的大小
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * 保留路径delayTime时间长
     *
     * @param delayTime
     */
    public void removeLineDelayed(long delayTime) {
        gestureLine.removeLineDelayed(delayTime);
    }

    /**
     * 对外提供的方法， 注册验证正确性监听接口
     *
     * @param verifyListener
     */
    public void setOnGestureVerifyListener(GestureLine.OnGestureVerifyListener verifyListener) {
        gestureLine.setOnGestureVerifyListener(verifyListener);
    }

    /**
     * 对外提供的方法, 注册绘制过程监听接口
     *
     * @param drawListener
     */
    public void addOnGestureDrawListener(GestureLine.OnGestureDrawListener drawListener) {
        gestureLine.addOnGestureDrawListener(drawListener);
    }
}
