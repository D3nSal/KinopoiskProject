package ru.project.denis.FinalProject.scheduling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.project.denis.FinalProject.dto.FilmDTO;
import ru.project.denis.FinalProject.models.FilmsResponseEntity;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SchedulingFilms {

    private static final Logger log = LoggerFactory.getLogger(SchedulingFilms.class);
    private final JmsTemplate jmsTemplate;
    private final HttpHeaders headers;
    private final RestTemplate restTemplate;

    public SchedulingFilms(JmsTemplate jmsTemplate, HttpHeaders headers, RestTemplate restTemplate) {
        this.jmsTemplate = jmsTemplate;
        this.headers = headers;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 7 * * *")
    public void job() throws JsonProcessingException {
        LocalDate date = LocalDate.now();
        Locale lang = new Locale("en");
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        String day = dayOfWeek.getDisplayName(TextStyle.FULL, lang);

        String url = switchDayForUrl(day);
        List<FilmDTO> films = getFiftyFilms(url);

        FilmsResponseEntity responseEntity = new FilmsResponseEntity();
        responseEntity.setTotal(films.size());
        responseEntity.setTotalPages(1);
        responseEntity.setItems(films);

        String filmsJson = new ObjectMapper().writeValueAsString(responseEntity);
        jmsTemplate.convertAndSend("everyDayFilms", filmsJson);
        log.info("Фильмы отправлены в очередь!");
    }

    private String switchDayForUrl(String day) {
        String url = "https://kinopoiskapiunofficial.tech/api/v2.2/films";
        switch (day) {
            case "Monday":
                url += "?genres=1";
                break;
            case "Tuesday":
                url += "?genres=2";
                break;
            case "Wednesday":
                url += "?genres=3";
                break;
            case "Thursday":
                url += "?genres=4";
                break;
            case "Friday":
                url += "?genres=5";
                break;
            case "Saturday":
                url += "?genres=6";
                break;
            case "Sunday":
                url += "?genres=7";
                break;
        }
        return url;
    }

    private List<FilmDTO> getFiftyFilms(String url) {
        String urlPage1 = url + "&page=1";
        String urlPage2 = url + "&page=2";
        String urlPage3 = url + "&page=3";
        HttpEntity entity = new HttpEntity(headers);
        List<FilmDTO> filmsPage1 = restTemplate.exchange(urlPage1, HttpMethod.GET, entity, FilmsResponseEntity.class).getBody().getItems();
        List<FilmDTO> filmsPage2 = restTemplate.exchange(urlPage2, HttpMethod.GET, entity, FilmsResponseEntity.class).getBody().getItems();
        List<FilmDTO> filmsPage3 = restTemplate.exchange(urlPage3, HttpMethod.GET, entity, FilmsResponseEntity.class).getBody().getItems();

        boolean page1Empty = filmsPage1 == null || filmsPage1.isEmpty();
        boolean page2Empty = filmsPage2 == null || filmsPage2.isEmpty();
        boolean page3Empty = filmsPage3 == null || filmsPage3.isEmpty();

        if (page1Empty && page2Empty && page3Empty) {
            return new ArrayList<>();
        } else if (page1Empty) {
            return new ArrayList<>();
        } else if (page2Empty) {
            return new ArrayList<>(filmsPage1);
        } else if (page3Empty) {
            ArrayList<FilmDTO> result = new ArrayList<>(filmsPage1.size() + filmsPage2.size());
            result.addAll(filmsPage1);
            result.addAll(filmsPage2);
            return result;
        } else {
            filmsPage3 = filmsPage3.stream().limit(10).toList();
            ArrayList<FilmDTO> result = new ArrayList<>(filmsPage1.size() + filmsPage2.size() + filmsPage3.size());
            result.addAll(filmsPage1);
            result.addAll(filmsPage2);
            result.addAll(filmsPage3);
            return result;
        }
    }

}
