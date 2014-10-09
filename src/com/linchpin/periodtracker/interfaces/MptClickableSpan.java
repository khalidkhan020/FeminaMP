package com.linchpin.periodtracker.interfaces;

import android.text.style.ClickableSpan;

public abstract class MptClickableSpan extends ClickableSpan
{
	
	private String	mUri;
	
	public String getUri()
	{
		return mUri;
	}
	
	public void setUri(String mUri)
	{
		this.mUri = mUri;
	}
	
}