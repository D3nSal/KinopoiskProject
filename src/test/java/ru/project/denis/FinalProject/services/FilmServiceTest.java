package ru.project.denis.FinalProject.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import ru.project.denis.FinalProject.dto.CountryDTO;
import ru.project.denis.FinalProject.dto.FilmDTO;
import ru.project.denis.FinalProject.dto.GenreDTO;
import ru.project.denis.FinalProject.models.Country;
import ru.project.denis.FinalProject.models.Film;
import ru.project.denis.FinalProject.models.FiltersEntity;
import ru.project.denis.FinalProject.models.Genre;
import ru.project.denis.FinalProject.repositories.FilmsRepository;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FilmServiceTest {

    @Mock
    private FilmsRepository filmsRepository;

    @Mock
    private FiltersEntity filters;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MailSender mailSender;

    @InjectMocks
    private FilmsService filmsService;


    @Test
    public void getFilmsTest() {
        Film film1 = mock(Film.class);
        Film film2 = mock(Film.class);
        List<Film> filmList = List.of(film1, film2);

        when(filmsRepository.findAll()).thenReturn(filmList);
        List<Film> actualFilmList = filmsService.getFilms();

        assertNotNull(actualFilmList);
        assertEquals(filmList, actualFilmList);
        verify(filmsRepository, times(1)).findAll();
    }

    @Test
    public void saveFilmBeforeAddMoreInfoTest() {
        String recipient = "test@mail.ru";

        FilmDTO filmDTO = mock(FilmDTO.class);
        FilmDTO filmDTO2 = mock(FilmDTO.class);
        FilmDTO filmDTO3 = mock(FilmDTO.class);
        when(filmDTO.getKinopoiskId()).thenReturn(123);
        when(filmDTO.getNameRu()).thenReturn("Форест Гамп");
        when(filmDTO2.getKinopoiskId()).thenReturn(321);
        when(filmDTO3.getKinopoiskId()).thenReturn(1234);
        when(filmDTO3.getNameRu()).thenReturn("Сумерки");
        when(filmDTO3.getCountries()).thenReturn(List.of(new CountryDTO("США")));
        when(filmDTO3.getGenres()).thenReturn(List.of(new GenreDTO("фантастика")));
        // список фильмов на вход метода
        List<FilmDTO> filmsDTO = List.of(filmDTO, filmDTO2, filmDTO3);
        // фильмы, уже находящиеся в БД
        List<Film> filmsFromBD = List.of(new Film(123, "Форест Гамп", 0, 0.0, null, 0));

        when(filmsRepository.findByKinopoiskId(123)).thenReturn(Optional.ofNullable(filmsFromBD.get(0)));
        when(filters.getCountries()).thenReturn(List.of(new Country(1, null, 1, "США")));
        when(filters.getGenres()).thenReturn(List.of(new Genre(6, null, 6,"фантастика")));
        String url = "https://kinopoiskapiunofficial.tech/api/v2.2/films/1234";
        HttpHeaders headers = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity<>(headers);
        // имитация запроса на https://kinopoiskapiunofficial.tech/api/v2.2/films/{id} для получения большей информации о фильме
        when(restTemplate.exchange(url, HttpMethod.GET, httpEntity, Film.class)).thenReturn(new ResponseEntity<>(new Film(1234, "Сумерки", 2008, 6.7,
                "Семнадцатилетняя девушка Белла переезжает к отцу в небольшой городок Форкс.", 1568), HttpStatus.OK));

        filmsService.saveFilms(filmsDTO, recipient);

        ArgumentCaptor<Film> captor = ArgumentCaptor.forClass(Film.class);
        verify(filmsRepository).save(captor.capture());
        List<Film> savingFilms = captor.getAllValues();
        verify(mailSender).sendMail(recipient, savingFilms);

        assertEquals(1, savingFilms.size());
        assertNotNull(savingFilms.getFirst().getNameRu());
        assertNotNull(savingFilms.getFirst().getCountries());
        assertEquals(1, savingFilms.getFirst().getCountries().getFirst().getCountryId());
        assertNotNull(savingFilms.getFirst().getGenres());
        assertEquals(6, savingFilms.getFirst().getGenres().getFirst().getGenreId());
        assertEquals(1568, savingFilms.getFirst().getReviewsCount());
        assertNotNull(savingFilms.getFirst().getDescription());
    }

    @Test
    public void getFilmsFromBDWithParamsTest() {
        Film film1 = new Film(326, "Побег из Шоушенка", 1994, 9.1, null, 596);
        film1.setCountries(Collections.singletonList(new Country(1, film1, 1,"США")));
        film1.setGenres(Collections.singletonList(new Genre(1, film1, 2,"драма")));
        Film film2 = new Film(502838, "Шерлок", 2010, 8.9, null, 900);
        film2.setCountries(Collections.singletonList(new Country(1, film2, 1, "США")));
        film2.setGenres(Collections.singletonList(new Genre(1, film2, 5,"детектив")));
        Film film3 = new Film(1189814, "Казанова", 2020, 8.2, null, 12);
        film3.setCountries(Collections.singletonList(new Country(1, film3, 34, "Россия")));
        film3.setGenres(Collections.singletonList(new Genre(1, film3, 5, "детектив")));

        // список фильмов в БД
        List<Film> filmsFromBD = List.of(film1, film2, film3);
        when(filmsService.getFilms()).thenReturn(filmsFromBD);

        // сортировка по стране
        Map<String, String> params1 = new HashMap<>();
        params1.put("countries", "1");
        List<FilmDTO> resultList1 = filmsService.getFilmsFromBD(params1);
        assertTrue(resultList1.stream().allMatch(filmDTO -> filmDTO.getCountries().getFirst().getCountry().equals("США")));

        //сортировка по жанру
        Map<String, String> params2 = new HashMap<>();
        params2.put("genres", "5");
        List<FilmDTO> resultList2 = filmsService.getFilmsFromBD(params2);
        assertTrue(resultList2.stream().allMatch(filmDTO -> filmDTO.getGenres().getFirst().getGenre().equals("детектив")));

        // сортировка по рейтингу
        Map<String, String> params3 = new HashMap<>();
        params3.put("order", "RATING");
        List<FilmDTO> resultList3 = filmsService.getFilmsFromBD(params3);
        assertTrue(resultList3.getFirst().getRatingKinopoisk() > resultList3.getLast().getRatingKinopoisk());

        // сортировка по количеству отзывов
        Map<String, String> params4 = new HashMap<>();
        params4.put("order", "NUM_VOTE");
        List<FilmDTO> resultList4 = filmsService.getFilmsFromBD(params4);
        assertTrue(resultList4.getFirst().getReviewsCount() > resultList4.getLast().getReviewsCount());

        // сортировка по году выпуска
        Map<String, String> params5 = new HashMap<>();
        params5.put("order", "YEAR");
        List<FilmDTO> resultList5 = filmsService.getFilmsFromBD(params5);
        assertTrue(resultList5.getFirst().getYear() > resultList5.getLast().getYear());

        // сортировка по максимальному году выпуска
        Map<String, String> params6 = new HashMap<>();
        params6.put("yearTo", "2010");
        List<FilmDTO> resultList6 = filmsService.getFilmsFromBD(params6);
        assertTrue(resultList6.stream().allMatch(filmDTO -> filmDTO.getYear() <= 2010));

        // сортировка по минимальному году выпуска
        Map<String, String> params7 = new HashMap<>();
        params7.put("yearFrom", "2010");
        List<FilmDTO> resultList7 = filmsService.getFilmsFromBD(params7);
        assertTrue(resultList7.stream().allMatch(filmDTO -> filmDTO.getYear() >= 2010));

        // сортировка по максимальному рейтингу
        Map<String, String> params8 = new HashMap<>();
        params8.put("ratingTo", "9");
        List<FilmDTO> resultList8 = filmsService.getFilmsFromBD(params8);
        assertTrue(resultList8.stream().allMatch(filmDTO -> filmDTO.getRatingKinopoisk() <= 9));

        // сортировка по минимальному рейтингу
        Map<String, String> params9 = new HashMap<>();
        params9.put("ratingFrom", "9");
        List<FilmDTO> resultList9 = filmsService.getFilmsFromBD(params9);
        assertTrue(resultList9.stream().allMatch(filmDTO -> filmDTO.getRatingKinopoisk() >= 9));

        // сортировка по ключевому слову в названии
        Map<String, String> params10 = new HashMap<>();
        params10.put("keyword", "побег");
        List<FilmDTO> resultList10 = filmsService.getFilmsFromBD(params10);
        assertTrue(resultList10.stream().allMatch(filmDTO -> filmDTO.getNameRu().toLowerCase().contains("побег")));
    }

}
