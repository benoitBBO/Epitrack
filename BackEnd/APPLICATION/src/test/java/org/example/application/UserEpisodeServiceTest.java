package org.example.application;

import org.example.application.util.CalculServiceImpl;
import org.example.domaine.catalog.Episode;
import org.example.domaine.user.UserProfile;
import org.example.domaine.userselection.UserEpisode;
import org.example.infrastructure.repository.ISerieRepository;
import org.example.infrastructure.repository.IUserEpisodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {UserEpisodeServiceImpl.class, IUserEpisodeRepository.class})
public class UserEpisodeServiceTest {
    @MockBean
    IUserEpisodeRepository userEpisodeRepository;
    @Autowired
    IUserEpisodeService userEpisodeService;
    Episode mockedEpisode;
    UserProfile mockedUserProfile;
    UserEpisode mockedUserEpisode;
    @BeforeEach
    public void init() {
        mockedEpisode = new Episode("num1", 1, "test", "", LocalDate.now());
        mockedEpisode.setId(1L);

        mockedUserProfile = new UserProfile();
        mockedUserProfile.setId(65L);
        mockedUserProfile.setUserName("TestUsername");
        mockedUserProfile.setPassword("TestPassword");
        mockedUserProfile.setEmail("test@test.fr");

        mockedUserEpisode = new UserEpisode();
        mockedUserEpisode.setId(3L);
        mockedUserEpisode.setEpisode(mockedEpisode);
        mockedUserEpisode.setStatus("WATCHED");
        mockedUserEpisode.setStatusDate(LocalDate.now());
        mockedUserEpisode.setUser(mockedUserProfile);

        when(userEpisodeRepository.save(mockedUserEpisode)).thenReturn(mockedUserEpisode);
    }
    @Test
    public void test_should_return_newUserEpisode_when_update_userEpisode(){
        assertThat(userEpisodeService.update(mockedUserEpisode)).isEqualTo(mockedUserEpisode);
    }
}
