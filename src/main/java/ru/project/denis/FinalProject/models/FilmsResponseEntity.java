package ru.project.denis.FinalProject.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.project.denis.FinalProject.dto.FilmDTO;

import java.util.List;

public class FilmsResponseEntity {
    @JsonProperty
    private int total;
    @JsonProperty
    private int totalPages;
    @JsonProperty
    private List<FilmDTO> items;

    public FilmsResponseEntity() {
    }

    public FilmsResponseEntity(int total, int totalPages, List<FilmDTO> items) {
        this.total = total;
        this.totalPages = totalPages;
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<FilmDTO> getItems() {
        return items;
    }

    public void setItems(List<FilmDTO> items) {
        this.items = items;
    }
}
