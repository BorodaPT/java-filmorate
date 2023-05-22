package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;
import ru.yandex.practicum.validation.MinDateFilms;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    private long countLike;

    public Film() {
        like = new HashSet<>();
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
        return duration == film.duration && countLike == film.countLike && id.equals(film.id) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(like, film.like);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration, like, countLike);
    }
}
