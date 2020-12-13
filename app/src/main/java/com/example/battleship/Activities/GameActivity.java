package com.example.battleship.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.battleship.Adapters.MatrixAdapter;
import com.example.battleship.Models.Matrix;
import com.example.battleship.R;
import com.facebook.drawee.view.SimpleDraweeView;

public class GameActivity extends AppCompatActivity {

    TextView playerNicknameText;
    TextView opponentNicknameText;

    SimpleDraweeView playerImage;
    SimpleDraweeView opponentImage;

    RecyclerView playerRecyclerView;
    RecyclerView opponentRecyclerView;
    MatrixAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        playerNicknameText = findViewById(R.id.playerNicknameText);
        opponentNicknameText = findViewById(R.id.opponentNicknameText);

        playerImage = findViewById(R.id.playerImage);
        opponentImage = findViewById(R.id.opponentImage);


        playerNicknameText.setText("Player");
        opponentNicknameText.setText("Opponent");

        playerImage.setImageURI("https://sun9-54.userapi.com/impg/cD1xpJQyXmsYGesvVRSJiKhoglB2UFGYBE6G5w/Qc5b-0dNKZo.jpg?size=1604x2160&quality=96&proxy=1&sign=774901725beeb7ac63ff2b7912c45aa1&type=album");
        opponentImage.setImageURI("https://sun9-54.userapi.com/impg/cD1xpJQyXmsYGesvVRSJiKhoglB2UFGYBE6G5w/Qc5b-0dNKZo.jpg?size=1604x2160&quality=96&proxy=1&sign=774901725beeb7ac63ff2b7912c45aa1&type=album");

        playerRecyclerView = findViewById(R.id.playerRecyclerView);
        opponentRecyclerView = findViewById(R.id.opponentRecyclerView);


        playerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));
        opponentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));

        Matrix matrix = new Matrix();
        adapter = new MatrixAdapter(this, matrix);
        playerRecyclerView.setAdapter(adapter);
        opponentRecyclerView.setAdapter(adapter);

    }
}