package ru.project.denis.FinalProject.services;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.project.denis.FinalProject.models.*;
import ru.project.denis.FinalProject.repositories.FilmsRepository;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmsService {
    private final FilmsRepository filmsRepository;
    private final FiltersEntity filters;
    private final RestTemplate restTemplate;
    private final HttpHeaders headers;
    private final MailSender mailSender;

    @Autowired
    public FilmsService(FilmsRepository filmsRepository, FiltersEntity filters, HttpHeaders headers, RestTemplate restTemplate, MailSender mailSender) {
        this.filmsRepository = filmsRepository;
        this.filters = filters;
        this.headers = headers;
        this.restTemplate = restTemplate;
        this.mailSender = mailSender;
    }

    @Transactional
    public void saveFilms(List<Film> films, String recipient) {
        List<Film> filterFilms = films.stream().filter(f -> filmsRepository.findByKinopoiskId(f.getKinopoiskId()).isEmpty()).filter(f -> f.getNameRu() != null).collect(Collectors.toList());
        for (Film film : filterFilms) {
            film.getCountries().forEach(country -> country.setCountryId(filtersId(country)).setFilm(film));
            film.getGenres().forEach(genre -> genre.setGenreId(filtersId(genre)).setFilm(film));
            Film filmWithMoreInfo = getMoreFilmInfo(film);
            film.setDescription(filmWithMoreInfo.getDescription());
            film.setReviewsCount(filmWithMoreInfo.getReviewsCount());
            filmsRepository.save(film);
        }
        File file = saveToXML(filterFilms);
        mailSender.sendMail(recipient, file);
    }

    @Transactional
    public List<Film> getFilms() {
        return filmsRepository.findAll();
    }

    private int filtersId(Country filmCountry) {
        int id = 0;
        for (Country country : filters.getCountries()) {
            if (country.getCountry().equals(filmCountry.getCountry())) {
                id = country.getId();
            }
        }
        return id;
    }

    private int filtersId(Genre filmGenre) {
        int id = 0;
        for (Genre genre : filters.getGenres()) {
            if (genre.getGenre().equals(filmGenre.getGenre())) {
                id = genre.getId();
            }
        }
        return id;
    }

    private Film getMoreFilmInfo(Film film) {
        String url = "https://kinopoiskapiunofficial.tech/api/v2.2/films/" + film.getKinopoiskId();
        HttpEntity httpEntity = new HttpEntity<>(headers);
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, Film.class).getBody();
    }

    public List<Film> filterFilms(String key, String value, List<Film> films) {
        switch (key) {
            case "countries":
                films = films.stream().filter(film -> film.getCountries().stream().anyMatch(country -> country.getCountryId() == Integer.parseInt(value))).collect(Collectors.toList());
                break;
            case "genres":
                films = films.stream().filter(film -> film.getGenres().stream().anyMatch(genre -> genre.getGenreId() == Integer.parseInt(value))).collect(Collectors.toList());
                break;
            case "order":
                films = switchOrdersType(films, value);
                break;
            case "type":
                films = films.stream().filter(film -> film.getType().equals(value)).collect(Collectors.toList());
                break;
            case "ratingFrom":
                films = films.stream().filter(film -> film.getRatingKinopoisk() >= Double.parseDouble(value)).collect(Collectors.toList());
                break;
            case "ratingTo":
                films = films.stream().filter(film -> film.getRatingKinopoisk() <= Double.parseDouble(value)).collect(Collectors.toList());
                break;
            case "yearFrom":
                films = films.stream().filter(film -> film.getYear() >= Integer.parseInt(value)).collect(Collectors.toList());
                break;
            case "yearTo":
                films = films.stream().filter(film -> film.getYear() <= Integer.parseInt(value)).collect(Collectors.toList());
                break;
            case "keyword":
                films = films.stream().filter(film -> film.getNameRu().toLowerCase().contains(value.toLowerCase())).collect(Collectors.toList());
                break;
            case "page":
                films = switchPages(films, value);
                break;
            default:
                break;
        }
        return films;
    }

    private List<Film> switchPages(List<Film> films, String value) {
        switch (value) {
            case "1":
                films = films.stream().limit(20).collect(Collectors.toList());
                break;
            case "2":
                films = films.stream().skip(20).limit(20).collect(Collectors.toList());
                break;
            case "3":
                films = films.stream().skip(40).limit(20).collect(Collectors.toList());
                break;
            case "4":
                films = films.stream().skip(60).limit(20).collect(Collectors.toList());
                break;
            case "5":
                films = films.stream().skip(80).limit(20).collect(Collectors.toList());
                break;
            default:
                break;
        }
        return films;
    }

    private List<Film> switchOrdersType(List<Film> films, String value) {
        switch (value) {
            case "RATING":
                Collections.sort(films, new Comparator<Film>() {
                    @Override
                    public int compare(Film f1, Film f2) {
                        if (f1.getRatingKinopoisk() < f2.getRatingKinopoisk()) {
                            return 1;
                        } else if (f1.getRatingKinopoisk() > f2.getRatingKinopoisk()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
            case "NUM_VOTE":
                Collections.sort(films, new Comparator<Film>() {
                    @Override
                    public int compare(Film f1, Film f2) {
                        if (f1.getReviewsCount() < f2.getReviewsCount()) {
                            return 1;
                        } else if (f1.getReviewsCount() > f2.getReviewsCount()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
            case "YEAR":
                Collections.sort(films, new Comparator<Film>() {
                    @Override
                    public int compare(Film f1, Film f2) {
                        if (f1.getYear() < f2.getYear()) {
                            return 1;
                        } else if (f1.getYear() > f2.getYear()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                break;
        }
        return films;
    }

    private File saveToXML(List<Film> films) {
        File file = new File("films.xml");
        try {
            JAXBContext context = JAXBContext.newInstance(Content.class);
            Content content = new Content();
            content.setFilmList(films);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(content, file);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}
