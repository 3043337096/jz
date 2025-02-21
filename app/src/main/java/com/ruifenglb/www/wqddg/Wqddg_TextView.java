package com.ruifenglb.www.wqddg;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

public class Wqddg_TextView extends androidx.appcompat.widget.AppCompatTextView {
    public Wqddg_TextView(Context context) {
        super(context);
    }

    public Wqddg_TextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
