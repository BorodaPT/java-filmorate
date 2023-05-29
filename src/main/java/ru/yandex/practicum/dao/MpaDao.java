package ru.yandex.practicum.dao;

import ru.yandex.practicum.model.Mpa;

import java.util.List;

public interface MpaDao {

    Mpa getMpa(int id);

    List<Mpa> getListMpa();
}
