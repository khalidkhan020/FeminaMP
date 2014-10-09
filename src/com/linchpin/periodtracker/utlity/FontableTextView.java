package com.linchpin.periodtracker.utlity;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.linchpin.myperiodtracker.R;

public class FontableTextView extends TextView {
	public FontableTextView(Context paramContext) {
		super(paramContext);
	}

	public FontableTextView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		UiUtil.setCustomFont(this, paramContext, paramAttributeSet,
				R.styleable.com_linchpin_periodtracker_utlity_FontableTextView, 0);
	}

	public FontableTextView(Context paramContext,
			AttributeSet paramAttributeSet, int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		UiUtil.setCustomFont(this, paramContext, paramAttributeSet,
				R.styleable.com_linchpin_periodtracker_utlity_FontableTextView, 0);
	}

}