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
    OnCellClickListener mOnCellClickListener;
    boolean isOpponentMatrix;
    boolean clickAllowed;

    public void ShowShipsAfterDefeat(){
        this.isOpponentMatrix = false;
        notifyDataSetChanged();
    }

    public void UpdateMatrix(Matrix matrix){
        this.matrix = matrix;
        notifyDataSetChanged();
    }

    public void SetClickable(boolean clickable){
        this.clickAllowed = clickable;
        notifyDataSetChanged();
    }

    public MatrixAdapter(Context context, Matrix matrix, boolean isOpponentMatrix,
                         OnCellClickListener onCellClickListener, boolean clickAllowed){
        this.isOpponentMatrix = isOpponentMatrix;
        this.matrix = matrix;
        this.context = context;
        this.mOnCellClickListener = onCellClickListener;
        this.clickAllowed = clickAllowed;
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
        int row = position / 10;
        int column = position % 10;

        holder.cellImageView.setImageResource(R.drawable.square);
        View.OnClickListener onCellClicked = v -> {
            if (matrix.matrix[row][column].type == Constants.SHIP_CELL) {
                holder.cellImageView.setImageResource(R.drawable.hit);
                mOnCellClickListener.onCellClicked(row, column, Constants.RESULT_HIT);
                matrix.matrix[row][column].type = Constants.HIT_CELL;
            } else if (matrix.matrix[row][column].type == Constants.NEARBY_CELL ||
                    matrix.matrix[row][column].type == Constants.EMPTY_CELL) {
                holder.cellImageView.setImageResource(R.drawable.miss);
                mOnCellClickListener.onCellClicked(row, column, Constants.RESULT_MISS);
            }

        };

        if (!isOpponentMatrix) {
            if (matrix.matrix[row][column].type == Constants.SHIP_CELL)
                holder.cellImageView.setBackgroundColor(R.color.black);
        }
        else {
            if (clickAllowed)
                holder.cellImageView.setOnClickListener(onCellClicked);
        }

        if (matrix.matrix[row][column].type == Constants.HIT_CELL) {
            holder.cellImageView.setImageResource(R.drawable.hit);
        } else if (matrix.matrix[row][column].type == Constants.CHECKED_CELL) {
            holder.cellImageView.setImageResource(R.drawable.miss);
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
        }

    }
}

