package com.alley.lock.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alley.lock.R;
import com.alley.lock.widget.GestureIndicator;
import com.alley.lock.widget.GestureLine;
import com.alley.lock.widget.GestureView;

/**
 * 手势密码设置界面
 *
 * @author Phoenix
 * @date 2017/8/31 9:17
 */
public class GestureSettingActivity extends AppCompatActivity {
	private TextView tvTip;
	private TextView tvReset;
	private FrameLayout flContainer;
	private GestureView gestureView;
	private GestureIndicator gestureIndicator;

	private boolean firstDraw = true;
	private String firstDrawPsw = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_setting);

		initView();

		setSubView();

		initEvent();
	}

	private void initView() {
		tvTip = (TextView) findViewById(R.id.tv_gesture_setting_tip);
		tvReset = (TextView) findViewById(R.id.tv_gesture_setting_reset);
		flContainer = (FrameLayout) findViewById(R.id.fl_gesture_setting_container);
		gestureIndicator = (GestureIndicator) findViewById(R.id.LockIndicator_gesture_setting_preview);
	}

	private void setSubView() {
		tvReset.setClickable(false);

		// 初始化一个显示各个点的viewGroup
		gestureView = new GestureView(this, "");
		//把手势密码放入容器
		gestureView.setParentView(flContainer);
		gestureIndicator.setPath("");
	}

	private void initEvent() {
		tvReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				firstDraw = true;
				gestureIndicator.setPath("");
				tvTip.setText(getString(R.string.gesture_setting_tip));
			}
		});

		gestureView.addOnGestureDrawListener(new GestureLine.OnGestureDrawListener() {
			@Override
			public void onDraw(String psw) {
				if (!verifyPsw(psw)) {
					tvTip.setText(Html.fromHtml("<font color='#ef5a50'>请至少连接4个点</font>"));
					gestureView.removeLineDelayed(0L);
					shakeAnimation();
					return;
				}

				if (firstDraw) {
					firstDrawPsw(psw);
				} else {
					secondDrawPsw(psw);
				}
				firstDraw = false;
			}
		});
	}

	/**
	 * 验证密码是否有效
	 *
	 * @param psw
	 * @return
	 */
	private boolean verifyPsw(String psw) {
		if (TextUtils.isEmpty(psw) || psw.length() < 4) {
			return false;
		}
		return true;
	}

	/**
	 * 第一次绘制
	 *
	 * @param psw
	 */
	private void firstDrawPsw(String psw) {
		firstDrawPsw = psw;
		gestureIndicator.setPath(psw);
		gestureView.removeLineDelayed(0L);

		tvReset.setClickable(true);
		tvReset.setText(getString(R.string.gesture_setting_reset));
		tvTip.setText(getString(R.string.gesture_setting_tip_confirm));
	}

	/**
	 * 第二次绘制密码验证
	 *
	 * @param psw
	 */
	private void secondDrawPsw(String psw) {
		if (psw.equals(firstDrawPsw)) {
			gestureView.removeLineDelayed(0L);
			finish();

			// TODO: 2017/5/12 手势密码设置成功，保存密码
		} else {
			shakeAnimation();
			// 1.5秒后清除绘制的线
			gestureView.removeLineDelayed(1500L);
			tvTip.setText(Html.fromHtml("<font color='#ef5a50'>与上次绘制不一致</font>"));
		}
	}

	/**
	 * 两次绘制密码不一致是动画提示
	 */
	private void shakeAnimation() {
		// 左右移动动画
		Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.translate_shake_horizontal);
		tvTip.startAnimation(shakeAnimation);
	}
}
