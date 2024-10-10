package ru.project.denis.FinalProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.project.denis.FinalProject.models.Film;
import ru.project.denis.FinalProject.dto.FilmDTO;
import ru.project.denis.FinalProject.models.FilmsResponseEntity;
import ru.project.denis.FinalProject.services.FilmsService;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;


@RestController
@RequestMapping("/films")
public class FilmsController {
    private final FilmsService filmsService;
    private final HttpHeaders headers;
    private final RestTemplate restTemplate;

    @Autowired
    public FilmsController(FilmsService filmsService, HttpHeaders headers, RestTemplate restTemplate) {
        this.filmsService = filmsService;
        this.headers = headers;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<FilmDTO> getAllFilms(@RequestParam Map<String, String> params) {
        String url = "https://kinopoiskapiunofficial.tech/api/v2.2/films";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder = builder.queryParam(entry.getKey(), entry.getValue());
        }
        String urlTemplate = builder.toUriString();
        urlTemplate = URLDecoder.decode(urlTemplate, StandardCharsets.UTF_8);

        HttpEntity entity = new HttpEntity(headers);
        FilmsResponseEntity fre = restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, FilmsResponseEntity.class).getBody();
        return fre.getItems();
    }

    @PostMapping
    public ResponseEntity<HttpStatus> saveFilms(@RequestParam Map<String, String> params) {
        List<FilmDTO> filmsDTO = getAllFilms(params);
        filmsService.saveFilms(filmsDTO, "deniskas1127@gmail.com");
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/fromBD")
    public List<FilmDTO> getFilmsFromBD(@RequestParam Map<String, String> params) {
        return filmsService.getFilmsFromBD(params);
    }
}
