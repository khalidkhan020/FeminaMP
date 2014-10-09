package com.linchpin.periodtracker.view;

import android.os.Bundle;

public class TabInfo {
    public final String tag;
    public final Class<?> clss;
    public final Bundle args;

 public TabInfo(String _tag, Class<?> _class, Bundle _args) {
        tag = _tag;
        clss = _class;
        args = _args;
    }
}
