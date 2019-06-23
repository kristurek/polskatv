package com.kristurek.polskatv.ui.menuepgs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.kristurek.polskatv.R;
import com.kristurek.polskatv.ui.searchepgs.SearchEpgsDialogFragment;
import com.kristurek.polskatv.ui.similarepgs.SimilarEpgsDialogFragment;

public class MenuEpgsDialogFragment extends DialogFragment {

    public static MenuEpgsDialogFragment newInstance(Integer id, String name, String description, String title) {
        MenuEpgsDialogFragment frag = new MenuEpgsDialogFragment();

        Bundle args = new Bundle();
        args.putSerializable("id", id);
        args.putSerializable("name", name);
        args.putSerializable("description", description);
        args.putSerializable("title", title);

        frag.setArguments(args);

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_epgs_dialog_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button descriptionBtn = view.findViewById(R.id.menu_epgs_description);
        Button findAllSimilarInOneChannelBtn = view.findViewById(R.id.menu_epgs_find_in_one_channel);
        Button findAllSimilarInManyChannelsBtn = view.findViewById(R.id.menu_epgs_find_in_many_channels);
        Button findAllSimilarByInputTextBtn = view.findViewById(R.id.menu_epgs_find_by_input_text);

        Integer id = (Integer) getArguments().getSerializable("id");
        String name = (String) getArguments().getSerializable("name");
        String description = (String) getArguments().getSerializable("description");
        String title = (String) getArguments().getSerializable("title");

        findAllSimilarInOneChannelBtn.setText(getString(R.string.msg_15, name));

        descriptionBtn.setOnClickListener(v -> {
            dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(title);
            builder.setMessage(description);
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        });

        findAllSimilarInOneChannelBtn.setOnClickListener(v -> {
            dismiss();
            SimilarEpgsDialogFragment dialog = SimilarEpgsDialogFragment.newInstanceOneChannel(title, id);
            dialog.show(getFragmentManager(), "SimilarEpgsDialogFragment");
        });

        findAllSimilarInManyChannelsBtn.setOnClickListener(v -> {
            dismiss();
            SimilarEpgsDialogFragment dialog = SimilarEpgsDialogFragment.newInstanceManyChannels(title);
            dialog.show(getFragmentManager(), "SimilarEpgsDialogFragment");
        });

        findAllSimilarByInputTextBtn.setOnClickListener(v -> {
            dismiss();
            SearchEpgsDialogFragment dialog = SearchEpgsDialogFragment.newInstance();
            dialog.show(getFragmentManager(), "SearchEpgsDialogFragment");
        });

        descriptionBtn.requestFocusFromTouch();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }
}