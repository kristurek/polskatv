package com.kristurek.polskatv.ui.searchepgs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.similarepgs.SimilarEpgsDialogFragment;

public class SearchEpgsDialogFragment extends DialogFragment {

    public static SearchEpgsDialogFragment newInstance() {
        SearchEpgsDialogFragment frag = new SearchEpgsDialogFragment();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_epgs_dialog_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button searchEpgsBtn = view.findViewById(R.id.search_epgs_btn);
        EditText searchEpgsInput = view.findViewById(R.id.search_epgs_pattern);

        searchEpgsBtn.setOnClickListener(v -> {
            if (!searchEpgsInput.getText().toString().trim().isEmpty()) {
                dismiss();
                SimilarEpgsDialogFragment dialog = SimilarEpgsDialogFragment.newInstanceManyChannels(searchEpgsInput.getText().toString());
                dialog.show(getFragmentManager(), "SimilarEpgsDialogFragment");
            }
        });

        searchEpgsInput.requestFocusFromTouch();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
}