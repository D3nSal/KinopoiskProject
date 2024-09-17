package ru.project.denis.FinalProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import ru.project.denis.FinalProject.models.FiltersEntity;

import java.util.Properties;

@Configuration
public class Beans {

    @Bean
    public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-API-KEY", "38e34224-03ec-490a-9800-2f2626a940a9");
        return headers;
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FiltersEntity getFiltersEntity() {
        String url = "https://kinopoiskapiunofficial.tech/api/v2.2/films/filters";
        HttpEntity httpEntity = new HttpEntity(getHeaders());
        return getRestTemplate().exchange(url, HttpMethod.GET, httpEntity, FiltersEntity.class).getBody();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.mail.ru");
        mailSender.setPort(465);
        mailSender.setUsername("deniskas1127@mail.ru");
        mailSender.setPassword("uBDHELtm9nYde6tQSnWP");
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

}
