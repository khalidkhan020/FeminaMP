package com.linchpin.periodtracker.interfaces;

import android.os.Bundle;

public interface PeriodMessanger
{
	public void bind(int id,PeriodMessanger messanger);
public void  sendMessage(int id,Bundle bundle);
}
