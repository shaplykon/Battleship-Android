package com.example.battleship.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.battleship.Adapters.MatrixAdapter;
import com.example.battleship.Adapters.OnCellClickListener;
import com.example.battleship.Models.Cell;
import com.example.battleship.Models.Game;
import com.example.battleship.Models.Matrix;
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

import java.util.ArrayList;
import java.util.HashMap;
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
    MatrixAdapter  playerAdapter;
    MatrixAdapter  opponentAdapter;
    GameViewModel gameViewModel;

    DatabaseReference gameDatabaseReference;
    Matrix opponentMatrix;

    String opponentMatrixPath;
    String playerMatrixPath;

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

        gameDatabaseReference = FirebaseDatabase.getInstance().getReference("games").
                child(Objects.requireNonNull(gameViewModel.gameId.getValue()));


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        isHost = Objects.equals(currentUser.getDisplayName(), game.getHostUser().username);

        opponentMatrixPath = isHost ? "connectedMatrix" : "hostMatrix";
        playerMatrixPath = !isHost  ? "connectedMatrix" : "hostMatrix";

        playerNicknameText = findViewById(R.id.playerNicknameText);
        opponentNicknameText = findViewById(R.id.opponentNicknameText);

        playerImage = findViewById(R.id.playerImage);
        opponentImage = findViewById(R.id.opponentImage);

        InitializeDisplaying();

        playerRecyclerView = findViewById(R.id.playerRecyclerView);
        opponentRecyclerView = findViewById(R.id.opponentRecyclerView);

        playerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));
        opponentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));

        Matrix playerMatrix = gameViewModel.playerMatrix.getValue();
        playerAdapter = new MatrixAdapter(this, playerMatrix, false, this);
        playerRecyclerView.setAdapter(playerAdapter);
        Context context = this;

        gameDatabaseReference.child(opponentMatrixPath).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<List<HashMap<Object, Object>>> fieldList = (List<List<HashMap<Object, Object>>>) snapshot.getValue();
                if (fieldList != null) {
                    opponentMatrix = new Matrix(fieldList);
                    opponentAdapter = new MatrixAdapter(getApplicationContext(), opponentMatrix, true, (OnCellClickListener) context);
                    opponentRecyclerView.setAdapter(opponentAdapter);
                    gameDatabaseReference.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gameDatabaseReference.child(playerMatrixPath).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<List<HashMap<Object, Object>>> fieldList = (List<List<HashMap<Object, Object>>>) snapshot.getValue();
                if (fieldList != null) {
                    playerAdapter.set(new Matrix(fieldList));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void InitializeDisplaying(){
        if(isHost){
            playerNicknameText.setText(Objects.requireNonNull(gameViewModel.hostUser.getValue()).username);
            opponentNicknameText.setText(Objects.requireNonNull(gameViewModel.guestUser.getValue()).username);
            playerImage.setImageURI(gameViewModel.hostUser.getValue().profileImageUrl);
            opponentImage.setImageURI(gameViewModel.guestUser.getValue().profileImageUrl);
        }
        else{
            playerNicknameText.setText(Objects.requireNonNull(gameViewModel.guestUser.getValue()).username);
            opponentNicknameText.setText(Objects.requireNonNull(gameViewModel.hostUser.getValue()).username);
            playerImage.setImageURI(gameViewModel.guestUser.getValue().profileImageUrl);
            opponentImage.setImageURI(gameViewModel.hostUser.getValue().profileImageUrl);
        }
    }

    @Override
    public void onCellClicked(int row, int column, int result) {
        switch (result){
            case Constants.RESULT_HIT:{
                opponentMatrix.matrix[row][column].type = Constants.HIT_CELL;
                break;
            }
            case Constants.RESULT_MISS:{
                opponentMatrix.matrix[row][column].type = Constants.CHECKED_CELL;
            }
        }

        gameDatabaseReference.child(opponentMatrixPath).setValue(opponentMatrix.GetList());
    }
}