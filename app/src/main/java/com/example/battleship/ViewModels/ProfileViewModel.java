package com.example.battleship.ViewModels;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;

import com.example.battleship.Models.User;
import com.example.battleship.Repositories.ProfileRepository;
import com.example.battleship.Utils.HexUtil;
import com.google.firebase.auth.FirebaseUser;


public class ProfileViewModel {
    public FirebaseUser user;
    public String uid;
    public MutableLiveData<Boolean> isGravatarUsed;
    public MutableLiveData<String> profileImageUri;
    public MutableLiveData<String> username;

    public ProfileViewModel(boolean isGravatarUsed, FirebaseUser user){
        this.user = user;
        this.uid = user.getUid();
        this.profileImageUri = new MutableLiveData<>();
        this.profileImageUri.setValue(user.getPhotoUrl().toString());
        this.username = new MutableLiveData<>();
        this.username.setValue(user.getDisplayName());
        this.isGravatarUsed = new MutableLiveData<>();
        this.isGravatarUsed.setValue(isGravatarUsed);
    }

    public void SaveChanges() {
        if(!user.getPhotoUrl().toString().equals(profileImageUri.getValue()))
            if(!isGravatarUsed.getValue())
                UploadPhotoToFirebaseStorage();
            else
                SaveProfilePhoto();
        if(!user.getDisplayName().equals(username.getValue()))
            SaveNickname();
    }

    private void UploadPhotoToFirebaseStorage(){
      ProfileRepository profileRepository = new ProfileRepository();
      profileRepository.UploadPhoto(this.profileImageUri.getValue(), uri -> {
          profileImageUri.setValue(uri.toString());
          SaveProfilePhoto();
      });

    }

    private void SaveNickname() {
        User profileUser = new User(uid, username.getValue(), profileImageUri.getValue());
        ProfileRepository profileRepository = new ProfileRepository();
        profileRepository.SaveNickname(profileUser, () -> profileRepository.UpdateProfile(user, profileUser));
    }


    private void SaveProfilePhoto() {
        User profileUser = new User(uid, username.getValue(), profileImageUri.getValue());
        ProfileRepository profileRepository = new ProfileRepository();
        profileRepository.SavePhoto(profileUser, () -> profileRepository.UpdateProfile(user, profileUser));
    }

    public String GetProfileImage() {
        Uri profileImageUri;

        if (this.isGravatarUsed.getValue()) {
            String email = user.getEmail();
            assert email != null;
            String hash = HexUtil.md5Hex(email.trim().toLowerCase());
            profileImageUri = Uri.parse("https://www.gravatar.com/avatar/" + hash + "?size=256");
        } else {
            if (user.getPhotoUrl() != null) {
                profileImageUri = Uri.parse(user.getPhotoUrl().toString());
            } else {
                profileImageUri = Uri.parse("https://yt3.ggpht.com/a/AATXAJwqEcDwWfOxAKdtQUUSm-" +
                        "lLCopCNoBdoFlTv9BsTA=s900-c-k-c0xffffffff-no-rj-mo");
            }
        }
        return profileImageUri.toString();
    }
}
