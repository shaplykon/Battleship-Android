package com.example.battleship.ViewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.battleship.Models.Game;
import com.example.battleship.Models.Matrix;

public class GameViewModel extends ViewModel {
    public MutableLiveData<String> hostId;
    public MutableLiveData<String> guestId;
    public MutableLiveData<Matrix> playerMatrix;
    public MutableLiveData<Matrix> opponentMatrix;
    public MutableLiveData<Boolean> hostIsReady;
    public MutableLiveData<Boolean> guestIsReady;

    public GameViewModel(Game game) {
        this.hostId = new MutableLiveData<>();
        this.guestId = new MutableLiveData<>();
        this.hostIsReady = new MutableLiveData<>();
        this.guestIsReady = new MutableLiveData<>();

        this.hostId.setValue(game.getHostUser().uid);
        this.guestId.setValue(game.getConnectedUser().uid);
        this.hostIsReady.setValue(false);
        this.guestIsReady.setValue(false);
    }
}
