package com.linchpin.periodtracker.utlity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;

public class TransparentProgressDailog extends Dialog {

	private ImageView iv;
	private TextView textView;

	public TransparentProgressDailog(Context context, int resourceIdOfImage) {
		super(context, R.style.TransparentProgressDialog);
		WindowManager.LayoutParams wlmp = getWindow().getAttributes();
		wlmp.gravity = Gravity.CENTER_HORIZONTAL;
		getWindow().setAttributes(wlmp);

		setTitle(null);
		setCancelable(false);
		setOnCancelListener(null);
		LinearLayout layout = new LinearLayout(context);
		
		layout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		layout.setPadding(10, 10, 10, 10);
		layout.setGravity(Gravity.CENTER);
		params.setMargins(50, 0, 50, 0);
		layout.setLayoutParams(params);
		
		layout.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.progresbg));
		iv = new ImageView(context);

		textView = new TextView(context);
		iv.setImageDrawable(context.getResources().getDrawable(R.drawable.progressbg));
		textView.setText(context.getResources().getString(R.string.loading));
		textView.setTextColor(Color.parseColor(context.getString(R.color.white)));
		textView.setTextSize(18);
		layout.addView(iv, params);
		layout.addView(textView, params);
		addContentView(layout, params);
	}

	@Override
	public void show() {
		super.show();
		RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f,
				Animation.RELATIVE_TO_SELF, .5f);
		anim.setInterpolator(new LinearInterpolator());
		anim.setRepeatCount(Animation.INFINITE);
		anim.setDuration(3000);
		iv.setAnimation(anim);
		iv.startAnimation(anim);
	}

}
