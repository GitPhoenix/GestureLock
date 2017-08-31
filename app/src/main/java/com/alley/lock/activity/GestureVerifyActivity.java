package com.alley.lock.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alley.lock.R;
import com.alley.lock.widget.GestureLine;
import com.alley.lock.widget.GestureView;


/**
 * 手势绘制/校验界面
 *
 * @author Phoenix
 * @date 2017/8/31 9:18
 */
public class GestureVerifyActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView ivLogo;
    private TextView tvAccount;
    private TextView tvTip;
    private FrameLayout flContainer;
    private GestureView gestureView;
    private TextView tvForgotPsw;
    private TextView tvLogin;

    private int errorCount = 0;
    private int permitErrorCount = 5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_verify);

        initView();

        setSubView();

        initEvent();
    }

    private void initView() {
        ivLogo = (ImageView) findViewById(R.id.iv_gesture_verify_logo);
        tvAccount = (TextView) findViewById(R.id.tv_gesture_verify_account);
        tvTip = (TextView) findViewById(R.id.tv_gesture_verify_tip);
        flContainer = (FrameLayout) findViewById(R.id.fl_gesture_verify_container);
        tvForgotPsw = (TextView) findViewById(R.id.tv_gesture_verify_forget);
        tvLogin = (TextView) findViewById(R.id.tv_gesture_verify_login);
    }

    private void setSubView() {
        tvAccount.setText("152****8888");

        // 初始化一个显示各个点的viewGroup
        gestureView = new GestureView(this, "1235789");
        //把手势密码放入容器
        gestureView.setParentView(flContainer);
    }

    /**
     * 密码错误验证
     */
    private void shakeAnimation() {
        // 左右移动动画
        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_shake_horizontal);
        tvTip.startAnimation(shakeAnimation);
    }

    private void initEvent() {
        tvForgotPsw.setOnClickListener(this);
        tvLogin.setOnClickListener(this);

        gestureView.setOnGestureVerifyListener(new GestureLine.OnGestureVerifyListener() {
            @Override
            public void onVerifySuccess() {
                gestureView.removeLineDelayed(0L);
                // TODO: 2017/5/12 密码正确，进入首页
                Toast.makeText(GestureVerifyActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
                GestureVerifyActivity.this.finish();
            }

            @Override
            public void onVerifyFailure() {
                gestureView.removeLineDelayed(1500L);
                shakeAnimation();

                errorCount++;
                if (errorCount < permitErrorCount) {
                    tvTip.setVisibility(View.VISIBLE);
                    tvTip.setText("密码错误，还有" + (permitErrorCount - errorCount) + "次机会 ");
                } else {
                    tvTip.setText("");
                    // TODO: 2017/5/12 密码错误达上限，跳转登录
                    Toast.makeText(GestureVerifyActivity.this, "请重新登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_gesture_verify_forget:
                Toast.makeText(GestureVerifyActivity.this, "忘记密码", Toast.LENGTH_SHORT).show();
                break;

            case R.id.tv_gesture_verify_login:
                // TODO: 2017/5/12 切换账户登录，清除手势密码
                Toast.makeText(GestureVerifyActivity.this, "切换账户登录", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }
}
