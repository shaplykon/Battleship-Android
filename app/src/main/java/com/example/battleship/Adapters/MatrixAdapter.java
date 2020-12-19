package com.example.battleship.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleship.Models.Matrix;
import com.example.battleship.R;
import com.example.battleship.Utils.Constants;


public class MatrixAdapter extends RecyclerView.Adapter<MatrixAdapter.ViewHolder> {

    Matrix matrix;
    Context context;
    private LayoutInflater inflater;
    boolean isOpponentMatrix;

    public void set(){
        notifyDataSetChanged();
    }

    public MatrixAdapter(Context context, Matrix matrix, boolean isOpponentMatrix){
        this.isOpponentMatrix = isOpponentMatrix;
        this.matrix = matrix;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MatrixAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_item, parent, false);
        int width = parent.getWidth() / 10;
        int height = parent.getHeight() / 10;
        view.setLayoutParams(new ViewGroup.LayoutParams(width, height));
        return new ViewHolder(view, isOpponentMatrix);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MatrixAdapter.ViewHolder holder, int position) {
        int i = position / 10;
        int j = position % 10;

        holder.cellImageView.setImageResource(R.drawable.square);

        View.OnClickListener onCellClicked = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matrix.matrix[i][j].type == Constants.SHIP_CELL)
                    Toast.makeText(context, "Korabl", Toast.LENGTH_LONG).show();
            }
        };



        holder.cellImageView.setOnClickListener(onCellClicked);

        if (!isOpponentMatrix) {
            if (matrix.matrix[i][j].type == Constants.SHIP_CELL) {
                if (matrix.matrix[i][j].isHead) {
                    holder.cellImageView.setBackgroundColor(R.color.black);
                    //  holder.cellImageView.setImageResource(R.drawable.head);
                } else {
                    holder.cellImageView.setBackgroundColor(R.color.black);
                }
            }
            if (matrix.matrix[i][j].type == Constants.NEARBY_CELL) {
                //holder.cellImageView.setBackgroundColor(R.color.green);
            }

        }


    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cellImageView;

        ViewHolder(View view, boolean isOpponentMatrix) {
            super(view);
            cellImageView = view.findViewById(R.id.cell_image_view);

            view.setClickable(isOpponentMatrix);

            cellImageView.setOnClickListener(v -> {
                //v.setBackgroundColor(R.color.colorAccent);
            });



        }
    }
}

interface OnCellClickListener{
    void onCellClicked();
}