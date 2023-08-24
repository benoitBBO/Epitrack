package org.example.application;

import org.example.domaine.userselection.UserSeason;
import org.example.infrastructure.repository.IUserSeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSeasonServiceImpl implements IUserSeasonService {
    @Autowired
    IUserSeasonRepository repository;
    @Override
    public void updateUserSeason(UserSeason userSeason) {
        repository.save(userSeason);
    }
}
