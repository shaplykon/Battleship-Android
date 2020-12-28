package com.example.battleship.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
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
        isHost = Objects.equals(currentUser.getDisplayName(), game.getHostUser().getUsername());
        gameViewModel = new ViewModelProvider(this, new GameViewModelFactory(game)).get(GameViewModel.class);

        fragmentManager = getSupportFragmentManager();
        ShowConnectionDetails(game);

        gameViewModel.guestIsReady.observe(this, isReady -> {
            if (isReady) {
                guestReadyImage.setImageResource(R.drawable.check);
                if (isHost)
                    CheckReadiness();
            } else
                guestReadyImage.setImageResource(R.drawable.close);
            });
        gameViewModel.hostIsReady.observe(this, isReady -> {
            if (isReady) {
                hostReadyImage.setImageResource(R.drawable.check);
                if (isHost)
                    CheckReadiness();

            } else
                hostReadyImage.setImageResource(R.drawable.close);

        });

        if(isHost) {
            gameViewModel.ObserveReadiness("guestReady");
            gameViewModel.gameState.observe(this, gameState -> {
                gameViewModel.SetGameState(gameState);
                if(gameState.equals(Constants.ACTIVE_STATE))
                    StartGame();
            });

        }
        else{
            gameViewModel.ObserveReadiness("hostReady");
            gameViewModel.gameState.observe(this, gameState -> {
                if(gameState.equals(Constants.ACTIVE_STATE))
                    StartGame();
            });
        }

        refreshButton = findViewById(R.id.refreshButton);
        readySwitch = findViewById(R.id.readySwitch);
        refreshButton.setOnClickListener(v -> {
            HideField();
            ShowField();
        });

        readySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isHost)
                gameViewModel.SetReadiness("hostReady", isChecked);
            else
                gameViewModel.SetReadiness("guestReady", isChecked);
        });
        gameViewModel.ObserveGameState();
    }
    private void CheckReadiness(){
        if(gameViewModel.hostIsReady.getValue() && gameViewModel.guestIsReady.getValue()){
            gameViewModel.gameState.setValue(Constants.ACTIVE_STATE);
        }
    }
    private void ShowConnectionDetails(Game game){
        ShowField();
        hostTextView.setText(game.getHostUser().getUsername());
        guestTextView.setText(game.getConnectedUser().getUsername());

        hostProfileImage.setImageURI(game.getHostUser().getProfileImageUrl());
        guestProfileImage.setImageURI(game.getConnectedUser().getProfileImageUrl());
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
        gameViewModel.StartGame(isHost);

        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
        gameViewModel.SetOpponentMatrix();
        gameViewModel.hostStep.setValue(true);
        Game game = gameViewModel.GetGameInstance();
        intent.putExtra(Constants.GAME_EXTRA, game);
        finish();
        startActivity(intent);
    }
    @Override
    public void OnFieldChanged(Matrix matrix) {
        gameViewModel.SetPlayerMatrix(matrix);
    }
}