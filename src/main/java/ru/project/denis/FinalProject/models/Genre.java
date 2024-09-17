package ru.project.denis.FinalProject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "genres")
public class Genre {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "film", referencedColumnName = "id")
    @XmlTransient
    private Film film;

    @Column(name = "genre_id")
    private int genreId;

    @Column(name = "genre")
    private String genre;

    public Genre() {
    }

    public Genre(int id, Film film, String genre) {
        this.id = id;
        this.film = film;
        this.genre = genre;
    }

    public int getGenreId() {
        return genreId;
    }

    public Genre setGenreId(int genreId) {
        this.genreId = genreId;
        return this;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
