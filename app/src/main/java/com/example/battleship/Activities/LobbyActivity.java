package com.example.battleship.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleship.Fragments.FieldControlsFragment;
import com.example.battleship.Fragments.FieldFragment;
import com.example.battleship.Models.Game;
import com.example.battleship.R;
import com.example.battleship.Models.User;
import com.example.battleship.Utils.Constants;
import com.example.battleship.ViewModels.GameViewModel;
import com.example.battleship.ViewModels.GameViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LobbyActivity extends AppCompatActivity implements FieldControlsFragment.ControlsInteractionListener {
    FragmentManager fragmentManager;

    GameViewModel gameViewModel;
    TextView  hostTextView;
    TextView guestTextView;

    ImageView hostReadyImage;
    ImageView guestReadyImage;

    Button readyButton;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    DatabaseReference gamesDatabaseReference;
    boolean isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        hostReadyImage = findViewById(R.id.hostReadyImage);
        guestReadyImage = findViewById(R.id.guestReadyImage);

        hostTextView = findViewById(R.id.hostTextView);
        guestTextView = findViewById(R.id.guestTextView);
        Intent intent = getIntent();
        Game game = (Game) intent.getSerializableExtra("game");

        isHost = currentUser.getDisplayName().equals(game.getHostUser().username);


        gameViewModel = new ViewModelProvider(this, new GameViewModelFactory(game)).get(GameViewModel.class);

        gameViewModel.guestIsReady.observe(this, isReady -> {
            if(isReady)
                guestReadyImage.setImageResource(R.drawable.check);

            else
                guestReadyImage.setImageResource(R.drawable.close);
        });
        gameViewModel.hostIsReady.observe(this, isReady -> {
            if(isReady)
                hostReadyImage.setImageResource(R.drawable.check);

            else
                hostReadyImage.setImageResource(R.drawable.close);
        });



        fragmentManager = getSupportFragmentManager();

        showConnectionDetails(game);
    }

    private void showConnectionDetails(Game game){
        ShowField();
        ShowControls();
        hostTextView.setText(game.getHostUser().username);
        guestTextView.setText(game.getConnectedUser().username);
    }

    @Override
    public void controlInteraction(int action, Boolean value) {
        if (action == Constants.REFRESH_ACTION) {
            HideField();
            ShowField();
        }
        if(action == Constants.READY_ACTION) {
            if (isHost)
                gameViewModel.hostIsReady.setValue(value);

            else
                gameViewModel.guestIsReady.setValue(value);
        }
    }

    private void ShowField(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .add(R.id.fieldContainer, new FieldFragment(), Constants.FIELD_FRAGMENT)
                .commit();
    }

    private void HideField(){
        FieldFragment fieldFragment = (FieldFragment) fragmentManager.findFragmentByTag(Constants.FIELD_FRAGMENT);
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .remove(fieldFragment)
                .commit();
    }
    private void ShowControls(){
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.fui_slide_in_right, R.anim.fui_slide_out_left)
                .add(R.id.controlsContainer, new FieldControlsFragment())
                .commit();
    }

}