package org.example.exposition.usermovie.converter;

import org.example.domaine.userselection.UserMovie;
import org.example.exposition.movie.converter.MovieConverter;
import org.example.exposition.user.dto.UserDto;
import org.example.exposition.usermovie.dto.UserMovieDetailDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMovieConverter {

    @Autowired
    MovieConverter movieConverter;

    public UserMovie convertDetailDtoToEntity(UserMovieDetailDto dto){
        ModelMapper mapper=new ModelMapper();
        return mapper.map(dto, UserMovie.class);
    }
    public UserMovieDetailDto convertEntityToDetailDto(UserMovie entity){
        UserDto userDto = new UserDto();
        userDto.setId(entity.getUser().getId());

        UserMovieDetailDto userMovieDto = new UserMovieDetailDto();
        userMovieDto.setId(entity.getId());
        userMovieDto.setUserRating(entity.getUserRating());
        userMovieDto.setStatus(entity.getStatus());
        userMovieDto.setStatusDate(entity.getStatusDate());
        userMovieDto.setMovie(movieConverter.convertEntityToDetailDto(entity.getMovie()));
        userMovieDto.setUser(userDto);

        return userMovieDto;
    }

}
