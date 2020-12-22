package com.example.battleship.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.battleship.Fragments.FieldFragment;
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

import java.util.Objects;

public class LobbyActivity extends AppCompatActivity implements
        FieldFragment.OnFieldChangedListener {

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    FragmentManager fragmentManager;

    DatabaseReference gamesDatabaseReference;
    GameViewModel gameViewModel;

    TextView  hostTextView;
    TextView guestTextView;

    ImageView hostReadyImage;
    ImageView guestReadyImage;

    Button refreshButton;

    SimpleDraweeView hostProfileImage;
    SimpleDraweeView guestProfileImage;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch readySwitch;

    boolean isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        hostProfileImage = findViewById(R.id.hostProfileImage);
        guestProfileImage = findViewById(R.id.guestProfileImage);

        hostReadyImage = findViewById(R.id.hostReadyImage);
        guestReadyImage = findViewById(R.id.guestReadyImage);

        hostTextView = findViewById(R.id.hostTextView);
        guestTextView = findViewById(R.id.guestTextView);

        Intent intent = getIntent();
        Game game = (Game) intent.getSerializableExtra("game");

        isHost = Objects.equals(currentUser.getDisplayName(), game.getHostUser().username);

        gameViewModel = new ViewModelProvider(this, new GameViewModelFactory(game)).get(GameViewModel.class);

        gamesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("games").
                child(Objects.requireNonNull(gameViewModel.gameId.getValue()));

        fragmentManager = getSupportFragmentManager();
        ShowConnectionDetails(game);

        gameViewModel.guestIsReady.observe(this, isReady -> {
            if (isReady) {
                guestReadyImage.setImageResource(R.drawable.check);
                if (isHost){
                    CheckReadiness();
                    }
            } else {
                        guestReadyImage.setImageResource(R.drawable.close);
            }});
        gameViewModel.hostIsReady.observe(this, isReady -> {
            if (isReady) {
                hostReadyImage.setImageResource(R.drawable.check);
                if (isHost) {
                    CheckReadiness();
                }
            } else {
                hostReadyImage.setImageResource(R.drawable.close);
            }
        });

        if(isHost){
            gamesDatabaseReference.child("guestReady").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    gameViewModel.guestIsReady.setValue(snapshot.getValue(Boolean.class));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
           gameViewModel.gameState.observe(this, gameState ->
                    gamesDatabaseReference.child("gameState").setValue(gameState));
        }
        else{
            gamesDatabaseReference.child("hostReady").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    gameViewModel.hostIsReady.setValue(snapshot.getValue(Boolean.class));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        refreshButton = findViewById(R.id.refreshButton);
        readySwitch = findViewById(R.id.readySwitch);
        refreshButton.setOnClickListener(v -> {
            HideField();
            ShowField();
        });
        readySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isHost) {
                gamesDatabaseReference.child("hostReady").setValue(isChecked);
                gameViewModel.hostIsReady.setValue(isChecked);
            } else {
                gamesDatabaseReference.child("guestReady").setValue(isChecked);
                gameViewModel.guestIsReady.setValue(isChecked);
            }
        });
        gamesDatabaseReference.child("gameState").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String gameState = snapshot.getValue(String.class);
                assert gameState != null;
                if(gameState.equals(Constants.ACTIVE_STATE)){
                    StartGame();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void CheckReadiness(){
        if(gameViewModel.hostIsReady.getValue() && gameViewModel.guestIsReady.getValue()){
            gameViewModel.gameState.setValue(Constants.ACTIVE_STATE);
        }
    }
    private void ShowConnectionDetails(Game game){
        ShowField();
        hostTextView.setText(game.getHostUser().username);
        guestTextView.setText(game.getConnectedUser().username);

        hostProfileImage.setImageURI(game.getHostUser().profileImageUrl);
        guestProfileImage.setImageURI(game.getConnectedUser().profileImageUrl);
      }
    private void ShowField(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .add(R.id.fieldContainer, new FieldFragment(), Constants.FIELD_FRAGMENT)
                .commit();
    }
    private void HideField(){
        FieldFragment fieldFragment = (FieldFragment) fragmentManager.findFragmentByTag(Constants.FIELD_FRAGMENT);
        assert fieldFragment != null;
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .remove(fieldFragment)
                .commit();
    }
    private void StartGame() {
        String matrixReference;
        String shipsReference;
        if (isHost){
            matrixReference = "hostMatrix";
            shipsReference = "hostShips";}
        else {
            matrixReference = "connectedMatrix";
            shipsReference = "connectedShips";
        }
        gamesDatabaseReference.child(matrixReference).setValue(
                Objects.requireNonNull(gameViewModel.playerMatrix.getValue()).GetList());

        gamesDatabaseReference.child( shipsReference).setValue(gameViewModel.playerMatrix.getValue().ships);
        gamesDatabaseReference.child("hostStep").setValue(true);
        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        gameViewModel.SetOpponentMatrix();
        gameViewModel.hostStep.setValue(true);
        Game game = gameViewModel.GetGameInstance();
        intent.putExtra(Constants.GAME_EXTRA, game);
        startActivity(intent);
    }

    @Override
    public void OnFieldChanged(Matrix matrix) {
        gameViewModel.SetPlayerMatrix(matrix);
    }
}