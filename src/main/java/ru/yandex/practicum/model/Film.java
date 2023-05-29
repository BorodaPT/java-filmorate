package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.validation.MinDateFilms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter

@Data
public class Film {

    Long id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Превышена максимальная длина описания(200)")
    private String description;

    @MinDateFilms(message = "Дата фильма не может быть меньше 28.12.1895")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть положительной")
    private int duration;

    @Nullable
    private Set<Long> like;

    private Mpa mpa;

    @Nullable
    private Set<Genre> genres;

    private long countLike;

    public Film() {
        like = new HashSet<>();
        genres = new HashSet<>();
    }

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = new HashSet<>();;
    }

    public void setLike(@Nullable Set<Long> like) {
        this.like = like;
        if (like != null) {
            countLike = like.size();
        } else {
            countLike = 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return duration == film.duration && countLike == film.countLike && Objects.equals(id, film.id) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(like, film.like) && Objects.equals(mpa, film.mpa) && Objects.equals(genres, film.genres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, like, mpa, genres, countLike);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("duration", duration);
        values.put("releaseDate", releaseDate);
        values.put("mpa_id", mpa.getId());
        return values;
    }
}
