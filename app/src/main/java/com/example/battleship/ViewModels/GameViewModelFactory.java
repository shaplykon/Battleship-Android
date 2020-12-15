package com.example.battleship.ViewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.battleship.Models.Game;

public class GameViewModelFactory implements ViewModelProvider.Factory {
    private final Game game;

    public GameViewModelFactory(Game game){
        this.game = game;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(GameViewModel.class)){
            return (T) new GameViewModel(game);
        }
        throw new IllegalArgumentException("Incorrect ViewModel class");
    }
}
