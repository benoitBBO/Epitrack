package org.example.exposition.season.converter;

import org.example.domaine.catalog.Season;
import org.example.exposition.season.dto.SeasonDetailDto;
import org.example.exposition.season.dto.SeasonDetailWithoutEpisodeDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SeasonConverter {

    public SeasonDetailWithoutEpisodeDto convertEntityToDetailWithoutEpisodeDto(Season entity){
        ModelMapper mapper=new ModelMapper();
        return mapper.map(entity,SeasonDetailWithoutEpisodeDto.class);
    }

    public SeasonDetailDto convertEntityToDetailDto(Season entity){
        ModelMapper mapper=new ModelMapper();
        return mapper.map(entity,SeasonDetailDto.class);
    }
}
