package com.example.battleship.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleship.Models.Matrix;
import com.example.battleship.R;
import com.example.battleship.Utils.Constants;


public class MatrixAdapter extends RecyclerView.Adapter<MatrixAdapter.ViewHolder> {

    Matrix matrix;
    Context context;
    private LayoutInflater inflater;

    public void set(){
        notifyDataSetChanged();
    }

    public MatrixAdapter(Context context, Matrix matrix){
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

        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MatrixAdapter.ViewHolder holder, int position) {
        int i = position / 10;
        int j = position % 10;
        holder.cellImageView.setImageResource(R.drawable.square);
        if(matrix.matrix[i][j].type == Constants.SHIP_CELL){
            if(matrix.matrix[i][j].isHead){
                holder.cellImageView.setImageResource(R.drawable.head);
            }
            else{
                holder.cellImageView.setBackgroundColor(R.color.black);
            }
        }
        if(matrix.matrix[i][j].type == Constants.NEARBY_CELL){
            holder.cellImageView.setBackgroundColor(R.color.green);
        }


    }

    @Override
    public int getItemCount() {
        return 100;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cellImageView;
        ViewHolder(View view){
            super(view);
            cellImageView  = view.findViewById(R.id.cell_image_view);


            cellImageView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    v.setBackgroundColor(R.color.colorAccent);
                }
            });
        }
    }
}
