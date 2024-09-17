package ru.project.denis.FinalProject.models;

import java.util.List;

public class FilmsResponseEntity {
    private int total;
    private int totalPages;
    private List<Film> items;

    public FilmsResponseEntity() {
    }

    public FilmsResponseEntity(int total, int totalPages, List<Film> items) {
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

    public List<Film> getItems() {
        return items;
    }

    public void setItems(List<Film> items) {
        this.items = items;
    }
}
