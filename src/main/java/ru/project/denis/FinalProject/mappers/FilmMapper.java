package ru.project.denis.FinalProject.mappers;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.project.denis.FinalProject.models.Film;
import ru.project.denis.FinalProject.dto.FilmDTO;
import java.util.List;

@Mapper
public interface FilmMapper {

    FilmMapper INSTANCE = Mappers.getMapper(FilmMapper.class);

    List<Film> filmsDTOToFilms(List<FilmDTO> filmsDTO);
    List<FilmDTO> filmsToFilmsDTO(List<Film> film);
}
