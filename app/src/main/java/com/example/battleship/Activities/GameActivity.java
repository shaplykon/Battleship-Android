package com.example.battleship.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleship.Adapters.MatrixAdapter;
import com.example.battleship.Adapters.OnCellClickListener;
import com.example.battleship.Fragments.StatisticsFragment;
import com.example.battleship.Models.Cell;
import com.example.battleship.Models.Game;
import com.example.battleship.Models.Ship;
import com.example.battleship.Models.Statistic;
import com.example.battleship.Models.User;
import com.example.battleship.R;
import com.example.battleship.Utils.Constants;
import com.example.battleship.ViewModels.GameViewModel;
import com.example.battleship.ViewModels.GameViewModelFactory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GameActivity extends AppCompatActivity implements OnCellClickListener {
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    TextView playerNicknameText;
    TextView opponentNicknameText;

    SimpleDraweeView playerImage;
    SimpleDraweeView opponentImage;

    RecyclerView playerRecyclerView;
    RecyclerView opponentRecyclerView;

    ImageView playerStepImage;
    ImageView opponentStepImage;

    MatrixAdapter  playerAdapter;
    MatrixAdapter  opponentAdapter;

    GameViewModel gameViewModel;

    String opponentMatrixPath;
    String playerMatrixPath;

    String opponentShipsPath;
    String playerShipsPath;

    boolean isHost;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO change GameActivity background image
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        Objects.requireNonNull(getSupportActionBar()).hide();

        Intent intent = getIntent();
        Game game = (Game) intent.getSerializableExtra(Constants.GAME_EXTRA);
        gameViewModel = new ViewModelProvider(this, new GameViewModelFactory(game)).get(GameViewModel.class);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
            isHost = Objects.equals(currentUser.getDisplayName(), game.getHostUser().getUsername());

        playerStepImage = findViewById(R.id.playerStepImage);
        opponentStepImage = findViewById(R.id.opponentStepImage);

        opponentMatrixPath = isHost ? "connectedMatrix" : "hostMatrix";
        playerMatrixPath = !isHost ? "connectedMatrix" : "hostMatrix";

        opponentShipsPath = isHost ? "connectedShips" : "hostShips";
        playerShipsPath = !isHost ? "connectedShips" : "hostShips";

        playerNicknameText = findViewById(R.id.playerNicknameText);
        opponentNicknameText = findViewById(R.id.opponentNicknameText);

        playerImage = findViewById(R.id.playerImage);
        opponentImage = findViewById(R.id.opponentImage);

        InitializeDisplaying();

        playerRecyclerView = findViewById(R.id.playerRecyclerView);
        opponentRecyclerView = findViewById(R.id.opponentRecyclerView);

        playerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));
        opponentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));

        playerAdapter = new MatrixAdapter(this,
                Objects.requireNonNull(gameViewModel.opponentMatrix.getValue()).matrix,
                false, this, false);
        playerRecyclerView.setAdapter(playerAdapter);

        gameViewModel.ObserveOpponentMatrix(opponentMatrixPath);
        gameViewModel.ObservePlayerMatrix(playerMatrixPath);
        gameViewModel.ObserveWin();
        gameViewModel.ObserveStep();
        gameViewModel.ObserveOpponentShips(opponentShipsPath);

        gameViewModel.hostWin.observe(this, hostWin -> {
            if (hostWin)
                Toast.makeText(getApplicationContext(),
                        Objects.requireNonNull(gameViewModel.hostUser.getValue()).getUsername() + " wins!", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(),
                        Objects.requireNonNull(gameViewModel.guestUser.getValue()).getUsername()+ " wins!", Toast.LENGTH_LONG).show();
            opponentAdapter.SetClickable(false);
            opponentAdapter.ShowShipsAfterDefeat();
            if(isHost)
                gameViewModel.SaveResult(hostWin);
            DialogFragment statisticsFragment = new StatisticsFragment();
            statisticsFragment.show(getSupportFragmentManager(), "statisticsFragment");
        });
        gameViewModel.hostStep.observe(this, isHostStep -> {
            if (isHostStep) {
                if (isHost)
                    ShowPlayerStepImage();
                else
                    ShowOpponentStepImage();
            } else {
                if (isHost)
                    ShowOpponentStepImage();
                else
                    ShowPlayerStepImage();
            }

            if (opponentAdapter != null)
                opponentAdapter.SetClickable(isHostStep == isHost);
        });
        gameViewModel.playerMatrix.observe(this, matrix -> {
            playerAdapter.UpdateMatrix(matrix.matrix);
        });
        gameViewModel.opponentMatrix.observe(this, matrix -> {
            opponentAdapter = new MatrixAdapter(getApplicationContext(), matrix.matrix,
                    true, GameActivity.this,
                    gameViewModel.hostStep.getValue() == isHost);
            opponentRecyclerView.setAdapter(opponentAdapter);
        });
        gameViewModel.hitsToWin.observe(this, hitsToWin -> {
            if(hitsToWin == 0)
                gameViewModel.FinishGame(isHost);
            });
    }

    private void ShowPlayerStepImage() {
        playerStepImage.setVisibility(View.VISIBLE);
        opponentStepImage.setVisibility(View.INVISIBLE);
    }
    private void ShowOpponentStepImage(){
        playerStepImage.setVisibility(View.INVISIBLE);
        opponentStepImage.setVisibility(View.VISIBLE);
    }
    private void InitializeDisplaying(){
        if(isHost){
            playerNicknameText.setText(Objects.requireNonNull(gameViewModel.hostUser.getValue()).getUsername());
            opponentNicknameText.setText(Objects.requireNonNull(gameViewModel.guestUser.getValue()).getUsername());
            playerImage.setImageURI(gameViewModel.hostUser.getValue().getProfileImageUrl());
            opponentImage.setImageURI(gameViewModel.guestUser.getValue().getProfileImageUrl());
        }
        else{
            playerNicknameText.setText(Objects.requireNonNull(gameViewModel.guestUser.getValue()).getUsername());
            opponentNicknameText.setText(Objects.requireNonNull(gameViewModel.hostUser.getValue()).getUsername());
            playerImage.setImageURI(gameViewModel.guestUser.getValue().getProfileImageUrl());
            opponentImage.setImageURI(gameViewModel.hostUser.getValue().getProfileImageUrl());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCellClicked(int row, int column, int result) {
        switch (result){
            case Constants.RESULT_HIT:{
                Objects.requireNonNull(gameViewModel.opponentMatrix.getValue()).
                        matrix[row][column].setType(Constants.HIT_CELL);
                gameViewModel.opponentMatrix.getValue().CheckShip(row, column);
                opponentAdapter.UpdateMatrix(gameViewModel.opponentMatrix.getValue().matrix);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                gameViewModel.hitsToWin.setValue(gameViewModel.hitsToWin.getValue() - 1);
                break;
            }
            case Constants.RESULT_MISS:{
                Objects.requireNonNull(gameViewModel.opponentMatrix.getValue()).
                        matrix[row][column].setType(Constants.CHECKED_CELL);
                gameViewModel.SetStep(isHost);
            }
        }
        gameViewModel.UpdateOpponentMatrix(opponentMatrixPath);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog aboutDialog = new AlertDialog.Builder(
                GameActivity.this).setMessage("Are you sure tou want to leave this game?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", (dialog, which) -> {
                }).create();

        aboutDialog.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}