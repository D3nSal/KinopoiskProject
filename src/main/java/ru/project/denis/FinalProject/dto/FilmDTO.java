package ru.project.denis.FinalProject.dto;

import java.util.List;

public class FilmDTO {
    private int kinopoiskId;
    private String nameRu;
    private List<CountryDTO> countries;
    private List<GenreDTO> genres;
    private double ratingKinopoisk;
    private int year;
    private String type;
    private int reviewsCount;
    private String description;


    public FilmDTO() {
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

    public List<CountryDTO> getCountries() {
        return countries;
    }

    public void setCountries(List<CountryDTO> countries) {
        this.countries = countries;
    }

    public List<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreDTO> genres) {
        this.genres = genres;
    }

    public double getRatingKinopoisk() {
        return ratingKinopoisk;
    }

    public void setRatingKinopoisk(double ratingKinopoisk) {
        this.ratingKinopoisk = ratingKinopoisk;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(int reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "FilmDTO{" +
                "kinopoiskId=" + kinopoiskId +
                ", nameRu='" + nameRu + '\'' +
                ", countries=" + countries +
                ", genres=" + genres +
                ", ratingKinopoisk=" + ratingKinopoisk +
                ", year=" + year +
                ", type='" + type + '\'' +
                ", reviewsCount=" + reviewsCount +
                ", description='" + description + '\'' +
                '}';
    }
}
