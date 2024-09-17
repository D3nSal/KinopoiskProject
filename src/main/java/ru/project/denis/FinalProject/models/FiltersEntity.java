package ru.project.denis.FinalProject.models;

import java.util.List;

public class FiltersEntity {
    List<Country> countries;
    List<Genre> genres;

    public FiltersEntity() {
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
