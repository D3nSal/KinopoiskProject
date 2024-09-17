package ru.project.denis.FinalProject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.denis.FinalProject.models.Film;

import java.util.Optional;

@Repository
public interface FilmsRepository extends JpaRepository<Film, Integer> {
    Optional<Film> findByKinopoiskId(int kinopoiskId);
}
