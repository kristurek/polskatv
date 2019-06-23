package com.kristurek.polskatv.ui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ListView;

public class XListView extends ListView {

    public enum SELECTION_TYPE {SELECT, CHECK}

    private SELECTION_TYPE type = SELECTION_TYPE.CHECK;
    private int lastSelectedPosition = INVALID_POSITION;

    public XListView(Context context) {
        super(context);
    }

    public XListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public XListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setRestoreSelectionType(SELECTION_TYPE type) {
        this.type = type;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (type.equals(SELECTION_TYPE.SELECT)) {
            if (gainFocus)
                setSelection(lastSelectedPosition);

            if (!gainFocus)
                lastSelectedPosition = getSelectedItemPosition();
        } else {
            if (gainFocus)
                if (getCheckedItemPosition() != INVALID_POSITION)
                    setSelection(getCheckedItemPosition());
                else
                    setSelection(lastSelectedPosition);

            if (!gainFocus)
                lastSelectedPosition = getSelectedItemPosition();
        }
    }
}
