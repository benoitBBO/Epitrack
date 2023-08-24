package org.example.application.user;

import org.example.domaine.user.UserProfile;

public interface IUserProfileService {

    void createUserProfile(UserProfile userProfile);

    void updateUserProfile(UserProfile userProfile);

    public UserProfile findUserProfileByUsername (String username);

}
