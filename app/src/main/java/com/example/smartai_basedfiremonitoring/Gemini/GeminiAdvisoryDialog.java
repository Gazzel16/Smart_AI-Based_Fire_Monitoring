package com.example.smartai_basedfiremonitoring.Gemini;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartai_basedfiremonitoring.R;

import java.util.ArrayList;

public class GeminiAdvisoryDialog extends DialogFragment {

    private RecyclerView recyclerView;
    private GeminiAdvisoryAdapter adapter;
    private ArrayList<GeminiAdvisoryModel> advisoryList = new ArrayList<>();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.gemini_advisory_dialog, null);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GeminiAdvisoryAdapter(advisoryList);
        recyclerView.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Gemini Fire Advisory")
                .setPositiveButton("Close", (dialog, which) -> dismiss());

        return builder.create();
    }

    // Call this to update RecyclerView when new advisory comes
    public void addAdvisory(GeminiAdvisoryModel advisory) {
        advisoryList.add(advisory);
        if (adapter != null) {
            adapter.notifyItemInserted(advisoryList.size() - 1);
        }
    }

}
