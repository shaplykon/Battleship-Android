package com.example.battleship.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.battleship.Adapters.MatrixAdapter;
import com.example.battleship.Models.Game;
import com.example.battleship.Models.Matrix;
import com.example.battleship.R;
import com.example.battleship.Utils.Constants;
import com.example.battleship.ViewModels.GameViewModel;
import com.example.battleship.ViewModels.GameViewModelFactory;
import com.facebook.drawee.view.SimpleDraweeView;

public class GameActivity extends AppCompatActivity {

    TextView playerNicknameText;
    TextView opponentNicknameText;

    SimpleDraweeView playerImage;
    SimpleDraweeView opponentImage;

    RecyclerView playerRecyclerView;
    RecyclerView opponentRecyclerView;
    MatrixAdapter  playerAdapter;
    MatrixAdapter  opponentAdapter;
    GameViewModel gameViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO change GameActivity background image
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        // TODO fix empty opponent matrix
        Game game = (Game)intent.getSerializableExtra(Constants.GAME_EXTRA);
        gameViewModel = new ViewModelProvider(this, new GameViewModelFactory(game)).get(GameViewModel.class);

        playerNicknameText = findViewById(R.id.playerNicknameText);
        opponentNicknameText = findViewById(R.id.opponentNicknameText);

        playerImage = findViewById(R.id.playerImage);
        opponentImage = findViewById(R.id.opponentImage);


        playerNicknameText.setText("Player");
      //  opponentNicknameText.setText("Opponent");
        opponentNicknameText.setText(gameViewModel.hostId.getValue());
        playerImage.setImageURI("https://sun9-54.userapi.com/impg/cD1xpJQyXmsYGesvVRSJiKhoglB2UFGYBE6G5w/Qc5b-0dNKZo.jpg?size=1604x2160&quality=96&proxy=1&sign=774901725beeb7ac63ff2b7912c45aa1&type=album");
        opponentImage.setImageURI("https://sun9-54.userapi.com/impg/cD1xpJQyXmsYGesvVRSJiKhoglB2UFGYBE6G5w/Qc5b-0dNKZo.jpg?size=1604x2160&quality=96&proxy=1&sign=774901725beeb7ac63ff2b7912c45aa1&type=album");

        playerRecyclerView = findViewById(R.id.playerRecyclerView);
        opponentRecyclerView = findViewById(R.id.opponentRecyclerView);


        playerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));
        opponentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));

        Matrix playerMatrix = gameViewModel.playerMatrix.getValue();
        playerAdapter = new MatrixAdapter(this, playerMatrix);
        playerRecyclerView.setAdapter( playerAdapter);

        Matrix opponentMatrix = gameViewModel.opponentMatrix.getValue();
        opponentAdapter = new MatrixAdapter(this, opponentMatrix);
        opponentRecyclerView.setAdapter(opponentAdapter);

    }
}