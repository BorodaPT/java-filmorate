package ru.yandex.practicum.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.exception.ExceptionDataRequest;
import ru.yandex.practicum.exception.ExceptionNotFound;
import ru.yandex.practicum.model.Film;
import java.util.*;

@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {

    private long filmId;

    private HashMap<Long, Film> films;

    private TreeSet<Film> popularFilm;

    public InMemoryFilmStorage() {
        this.filmId = 0;
        this.films = new HashMap<>();
        popularFilm = new TreeSet<>(Comparator.comparingLong(Film::getCountLike).reversed().thenComparing(Film::getId));

    }

    private void addFilm(Film film) {
        filmId++;
        film.setId(filmId);
        films.put(film.getId(), film);
    }

    @Override
    public Film create(Film film) {
        if (films.containsKey(film.getId())) {
            throw new ExceptionNotFound("createFilm","Фильм уже зарегестрирован");
        } else {
            addFilm(film);
            return films.get(film.getId());
        }
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            return films.get(film.getId());
        } else {
            throw new ExceptionNotFound("updateFilm","Фильм для обновления отсутствует");
        }
    }

    @Override
    public void delete(Long id) {
        if (films.containsKey(id)) {
            films.remove(id);
        } else {
            throw new ExceptionNotFound("deleteFilm","Фильм для удаления отсутствует");
        }
    }

    @Override
    public Film getFilm(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new ExceptionNotFound("selectFilm","Фильм не найден");
        }
    }

    @Override
    public Film setLike(Long idFilm, Long idUser, boolean isInstalled) {
        if (films.containsKey(idFilm)) {
            Film film = films.get(idFilm);
            if (film.getLike().contains(idUser)) {
                if (!isInstalled) {
                    Set<Long> likeFilm = film.getLike();
                    likeFilm.remove(idUser);
                    film.setLike(likeFilm);
                    films.put(idFilm,film);
                }
                return film;
            } else {
                if (isInstalled) {
                    Set<Long> likeFilm = film.getLike();
                    if (likeFilm.add(idUser)) {
                        film.setLike(likeFilm);
                        films.put(idFilm, film);
                        return film;
                    } else {
                        throw new ExceptionDataRequest("updateFilm", "Не удалось поставить лайк");
                    }
                }
                return film;
            }
        } else {
            throw new ExceptionNotFound("likeFilm","Фильм не найден");
        }
    }

    @Override
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public ArrayList<Film> getPopular(long count) {
        sortPopularFilm();
        long cnt = (count >= popularFilm.size()) ? popularFilm.size() : count;
        ArrayList<Film> result = new ArrayList<>();
        long i = 0;
        for (Film film : popularFilm) {
            result.add(film);
            i++;
            if (i == cnt) break;
        }
        return result;
    }

    public void sortPopularFilm() {
        popularFilm.clear();
        popularFilm.addAll(films.values());
    }

}
