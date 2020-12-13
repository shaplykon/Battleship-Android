package com.example.battleship.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.battleship.Adapters.MatrixAdapter;
import com.example.battleship.Models.Matrix;
import com.example.battleship.R;

public class GameActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MatrixAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));

        Matrix matrix = new Matrix();
        adapter = new MatrixAdapter(this, matrix);
        recyclerView.setAdapter(adapter);
        adapter.set();
    }
}