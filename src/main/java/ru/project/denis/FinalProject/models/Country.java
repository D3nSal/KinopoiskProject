package ru.project.denis.FinalProject.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlTransient;

@Entity
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name= "countries")
public class Country {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name= "film", referencedColumnName = "id")
    @XmlTransient
    private Film film;

    @Column(name = "country_id")
    private int countryId;

    @Column(name = "country")
    private String country;

    public Country() {}

    public Country(int id, Film film, String country) {
        this.id = id;
        this.film = film;
        this.country = country;
    }

    public int getCountryId() {
        return countryId;
    }

    public Country setCountryId(int countryId) {
        this.countryId = countryId;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
