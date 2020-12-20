package com.example.battleship.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.battleship.Adapters.MatrixAdapter;
import com.example.battleship.Adapters.OnCellClickListener;
import com.example.battleship.Models.Matrix;
import com.example.battleship.R;

public class FieldFragment extends Fragment {
    OnFieldChangedListener mOnFieldChangedListener;
    RecyclerView recyclerView;
    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        mOnFieldChangedListener = (OnFieldChangedListener) context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field, container, false);
        recyclerView = view.findViewById(R.id.playerRecyclerView);

        Matrix matrix = new Matrix();
        matrix.GenerateMatrix();

        mOnFieldChangedListener.OnFieldChanged(matrix);
        MatrixAdapter adapter = new MatrixAdapter(getContext(), matrix,false, null, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),10));
        return view;
    }
    public interface OnFieldChangedListener{
        void OnFieldChanged(Matrix matrix);
    }
}