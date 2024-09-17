package ru.project.denis.FinalProject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Entity
@Table(name = "films")
public class Film {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "film_id")
    private int kinopoiskId;

    @Column(name = "film_name")
    private String nameRu;

    @Column(name = "year")
    private int year;

    @Column(name = "rating")
    private Double ratingKinopoisk;

    @Column(name = "description")
    private String description;

    @Column(name = "type")
    private String type;

    @Column(name = "votes")
    private int reviewsCount;

    @OneToMany(mappedBy = "film", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Country> countries;

    @OneToMany(mappedBy = "film", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Genre> genres;

    public Film() {}

    public Film(int kinopoiskId, String nameRu, int year, Double ratingKinopoisk, String description) {
        this.kinopoiskId = kinopoiskId;
        this.nameRu = nameRu;
        this.year = year;
        this.ratingKinopoisk = ratingKinopoisk;
        this.description = description;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKinopoiskId() {
        return kinopoiskId;
    }

    public void setKinopoiskId(int kinopoiskId) {
        this.kinopoiskId = kinopoiskId;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getRatingKinopoisk() {
        return ratingKinopoisk;
    }

    public void setRatingKinopoisk(Double ratingKinopoisk) {
        this.ratingKinopoisk = ratingKinopoisk;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
