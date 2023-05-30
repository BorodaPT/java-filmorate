package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dao.MpaDao;
import ru.yandex.practicum.model.Mpa;

import java.util.List;

@Service
public class MpaService {

    @Autowired
    private MpaDao mpaDao;

    public Mpa getMpa(int id) {
        return mpaDao.getMpa(id);
    }

    public List<Mpa> getListMpa() {
        return mpaDao.getListMpa();
    }

}
