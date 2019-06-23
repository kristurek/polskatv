package com.kristurek.polskatv.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.tabs.TabLayout;
import com.kristurek.polskatv.util.DateTimeHelper;
import com.kristurek.polskatv.util.FontHelper;

import org.joda.time.LocalDate;

import java.util.List;

import static com.google.android.material.tabs.TabLayout.Tab.INVALID_POSITION;

public class XTabLayout extends TabLayout {

    private List<LocalDate> mDays;
    private int lastSelectedPosition = INVALID_POSITION;
    private BaseOnTabSelectedListener onTabSelectedListener;

    public XTabLayout(Context context) {
        super(context);

        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    public XTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    public XTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    @Override
    public void addOnTabSelectedListener(@NonNull TabLayout.BaseOnTabSelectedListener listener) {
        onTabSelectedListener = listener;
        super.addOnTabSelectedListener(listener);
    }

    public void selectWithoutTriggerListeners(LocalDate selectedDay) {
        removeOnTabSelectedListener(onTabSelectedListener);
        select(selectedDay);
        addOnTabSelectedListener(onTabSelectedListener);
    }

    public void select(LocalDate selectedDay) {
        select(mDays.indexOf(selectedDay));
    }

    private void select(@Nullable final int position) {
        if (position >= 0 && position < getTabCount())
            select(getTabAt(position));
    }

    private void select(@Nullable final Tab tab) {
        tab.select();

        lastSelectedPosition = getSelectedTabPosition();

        post(() -> setScrollPosition(tab.getPosition(), 0f, true));
    }

    public void addTabs(List<LocalDate> days) {
        mDays = days;
        for (LocalDate day : days) {
            Tab tab = newTab();

            TextView customView = new TextView(getContext());
            customView.setText(Html.fromHtml("<b>" + day.toString(DateTimeHelper.EEE) + "</b>" + "<br />" + "<small>" + day.toString(DateTimeHelper.ddMM) + "</small>"));
            customView.setGravity(Gravity.CENTER);
            FontHelper.setFont(getContext(), customView, FontHelper.Header.H_1);

            tab.setCustomView(customView);

            addTab(tab, false);
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (gainFocus)
            if (lastSelectedPosition != INVALID_POSITION) {
                getView(lastSelectedPosition).requestFocus();
                setScrollPosition(lastSelectedPosition, 0f, true);
            }

        if (!gainFocus)
            lastSelectedPosition = getSelectedTabPosition();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isFocused(0) &&
                (KeyEvent.KEYCODE_DPAD_LEFT == event.getKeyCode() || KeyEvent.KEYCODE_DPAD_DOWN_LEFT == event.getKeyCode() || KeyEvent.KEYCODE_DPAD_UP_LEFT == event.getKeyCode()))
            return true;

        if (isFocused(getTabCount() - 1) &&
                (KeyEvent.KEYCODE_DPAD_RIGHT == event.getKeyCode() || KeyEvent.KEYCODE_DPAD_DOWN_RIGHT == event.getKeyCode() || KeyEvent.KEYCODE_DPAD_UP_RIGHT == event.getKeyCode()))
            return true;

        return super.dispatchKeyEvent(event);
    }

    private boolean isFocused(int position) {
        if (getView(position) != null)
            return getView(position).isFocused();
        return false;
    }

    private View getView(int position) {
        if (getViewGroup() != null)
            return getViewGroup().getChildAt(position);
        return null;
    }

    private ViewGroup getViewGroup() {
        return ((ViewGroup) getChildAt(0));
    }
}