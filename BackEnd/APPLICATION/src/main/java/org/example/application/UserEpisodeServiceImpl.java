package org.example.application;

import org.example.domaine.userselection.UserEpisode;
import org.example.infrastructure.repository.IUserEpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserEpisodeServiceImpl implements IUserEpisodeService {

    @Autowired
    IUserEpisodeRepository userEpisodeRepository;

    @Override
    public UserEpisode update(UserEpisode userEpisode) {
        return userEpisodeRepository.save(userEpisode);
    }

}
