package ru.agrass.silenttimer.view.custom.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import ru.agrass.silenttimer.R;

public class CheckBoxDay extends android.support.v7.widget.AppCompatButton implements OnClickListener {
    private static final String TAG = CheckBoxDay.class.getSimpleName();

    private boolean isCheck;
    private OnClickListener listener;
    private String dayName;


    public CheckBoxDay(Context context) {
        super(context);
        init();
    }

    public CheckBoxDay(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initAttrs(context, attributeSet);
        init();
    }

    public CheckBoxDay(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        initAttrs(context, attributeSet);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.CheckBoxDay,
                0, 0
        );

        try {
            dayName = a.getString(R.styleable.CheckBoxDay_dayName);
        } finally {
            a.recycle();
        }
    }

    private void init() {
        super.setOnClickListener(this);
        setCheck(false);
    }

    public void setCheck(boolean check) {
        isCheck = check;
        if (isCheck()) {
            setBackgroundResource(R.drawable.checkbox_checked);
            setTextColor(Color.BLACK);
        } else {
            setBackgroundResource(R.drawable.checkbox_unchecked);
            setTextColor(Color.WHITE);
        }
    }

    public String getDayName() {
        return dayName;
    }

    private int invertColor(int color) {
//        TODO: Add inverting color or new attribute for checked checkbox textColor
        return color;
    }

    public boolean isCheck() {
        return isCheck;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.listener = l;
    }

    @Override
    public void onClick(View view) {
        setCheck(!isCheck());
        if (listener == null) return;
        listener.onClick(view);
    }
}
