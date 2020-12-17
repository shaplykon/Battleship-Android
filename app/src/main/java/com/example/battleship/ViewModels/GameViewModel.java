package com.example.battleship.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.battleship.Models.Game;
import com.example.battleship.Models.Matrix;
import com.example.battleship.Models.User;
import com.example.battleship.Repositories.FirebaseUserCallback;
import com.example.battleship.Repositories.UserRepository;

public class GameViewModel extends ViewModel {
    public MutableLiveData<String> hostId;
    public MutableLiveData<String> guestId;
    public MutableLiveData<Matrix> playerMatrix;
    public MutableLiveData<Matrix> opponentMatrix;
    public MutableLiveData<Boolean> hostIsReady;
    public MutableLiveData<Boolean> guestIsReady;
    public MutableLiveData<String> gameId;
    public MutableLiveData<String> gameState;

    public GameViewModel(Game game) {
        this.hostId = new MutableLiveData<>();
        this.guestId = new MutableLiveData<>();
        this.hostIsReady = new MutableLiveData<>();
        this.guestIsReady = new MutableLiveData<>();
        this.gameId = new MutableLiveData<>();
        this.gameState = new MutableLiveData<>();
        this.opponentMatrix = new MutableLiveData<>();
        this.playerMatrix = new MutableLiveData<>();

        this.playerMatrix.setValue(game.getPlayerMatrix());
        this.opponentMatrix.setValue(game.getOpponentMatrix());
        this.gameState.setValue(game.getGameState());
        this.gameId.setValue(game.getGameId());
        this.hostId.setValue(game.getHostUser().uid);
        this.guestId.setValue(game.getConnectedUser().uid);
        this.hostIsReady.setValue(false);
        this.guestIsReady.setValue(false);
    }

    public void SetPlayerMatrix(Matrix matrix) {
        this.playerMatrix.setValue(matrix);
    }

    public void SetOpponentMatrix() {
        Matrix matrix = new Matrix();
        this.opponentMatrix.setValue(matrix);
    }

    public Game GetGameInstance() {
        final User[] users = {new User()};
        Game game;


        UserRepository.GetUserAsynchronous(hostId.getValue(), new FirebaseUserCallback() {
            @Override
            public void onUserCallback(User user) {
                users[0] = user;
            }
        });

        game = new Game(
                //UserRepository.GetUserById(hostId.getValue()),
                //UserRepository.GetUserById(guestId.getValue()),
                users[0],
                users[0],
                playerMatrix.getValue(),
                opponentMatrix.getValue(),
                gameState.getValue(),
                gameId.getValue(),
                hostIsReady.getValue(),
                guestIsReady.getValue());


        return game;
    }
}
