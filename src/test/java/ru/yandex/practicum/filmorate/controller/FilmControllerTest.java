package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //создание film
    @Test
    public void createFilmTestCorrect() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Му-му\",\"description\":\"По рассказам великого писателя\",\"releaseDate\":\"2000-01-01\",\"duration\":60}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Му-му"))
                .andExpect(jsonPath("$.description").value("По рассказам великого писателя"))
                .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                .andExpect(jsonPath("$.duration").value(60))
                .andReturn();
    }

    @Test
    public void createFilmTestBadName() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":\"По рассказам великого писателя\",\"releaseDate\":\"2000-01-01\",\"duration\":60}")
                        .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.violations[0].message").value("Название фильма не может быть пустым"))
                .andReturn();
    }

    @Test
    public void createFilmTestBadDescription() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"му-му\",\"description\":\"По рассказам великого писателяПо рассказам великого писателяПо рассказам великого писателяПо рассказам великого писателя" +
                                "По рассказам великого писателяПо рассказам великого писателяПо рассказам великого писателяПо рассказам великого писателя\"," +
                                "\"releaseDate\":\"2000-01-01\",\"duration\":60}")
                        .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.violations[0].message").value("Превышена максимальная длина описания(200)"))
                .andReturn();
    }

    @Test
    public void createFilmTestBadDate() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"му-му\",\"description\":\"По рассказам великого писателя\",\"releaseDate\":\"1895-11-01\",\"duration\":60}")
                        .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.violations[0].message").value("Дата фильма не может быть меньше 28.12.1895"))
                .andReturn();
    }

    @Test
    public void createFilmTestBadDuration() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"му-му\",\"description\":\"По рассказам великого писателя\",\"releaseDate\":\"2000-11-01\",\"duration\":-60}")
                        .characterEncoding("utf-8"))
                .andExpect(jsonPath("$.violations[0].message").value("Продолжительность должна быть положительной"))
                .andReturn();
    }

    //изменение
    @Test
    public void updateUserTestCorrect() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Му-му\",\"description\":\"По рассказам великого писателя\",\"releaseDate\":\"2000-01-01\",\"duration\":60}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\",\"name\":\"4 мушкетера\",\"description\":\"По рассказам великого писателя\",\"releaseDate\":\"2000-01-01\",\"duration\":120}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("4 мушкетера"))
                .andExpect(jsonPath("$.description").value("По рассказам великого писателя"))
                .andExpect(jsonPath("$.releaseDate").value("2000-01-01"))
                .andExpect(jsonPath("$.duration").value(120))
                .andReturn();
    }


}
