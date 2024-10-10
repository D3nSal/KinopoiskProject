package ru.project.denis.FinalProject.activemq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import ru.project.denis.FinalProject.models.FilmsResponseEntity;
import ru.project.denis.FinalProject.services.FilmsService;

@Component
public class ActiveMQConsumer {

    private Logger log = LoggerFactory.getLogger(ActiveMQConsumer.class);
    private final FilmsService filmsService;

    public ActiveMQConsumer(FilmsService filmsService) {
        this.filmsService = filmsService;
    }

    @JmsListener(destination = "everyDayFilms")
    public void receiveMessage(String entity) throws JsonProcessingException {
        FilmsResponseEntity entityFilms = new ObjectMapper().readValue(entity, FilmsResponseEntity.class);
        filmsService.saveFilms(entityFilms.getItems(), "deniskas1127@gmail.com");
        log.info("Фильмы получены и сохранены!");
    }
}
