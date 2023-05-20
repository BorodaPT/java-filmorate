package ru.yandex.practicum.filmorate.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;



@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    //создание user
    @Test
    public void createUserTestCorrect() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ya@mail.com\",\"login\":\"Testusers1\",\"name\":\"123\",\"birthday\":\"2019-04-04\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("Testusers1"))
                .andExpect(jsonPath("$.email").value("ya@mail.com"))
                .andExpect(jsonPath("$.name").value("123"))
                .andExpect(jsonPath("$.birthday").value("2019-04-04") )
                .andReturn();
    }

    @Test
    public void createUserTestFuture() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ya@mail.com\",\"login\":\"Testusers1\",\"name\":\"123\",\"birthday\":\"2030-04-04\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].message").value("Дата рождения не может быть больше текущей"))
                .andReturn();
    }

    @Test
    public void createUserTestBadEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"yamail.com\",\"login\":\"Testusers1\",\"name\":\"123\",\"birthday\":\"2020-04-04\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].message").value("Неверный формат электронной почты"))
                .andReturn();
    }

    @Test
    public void createUserTestBadLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ya@mail.com\",\"login\":\"\",\"name\":\"123\",\"birthday\":\"2020-04-04\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].message").value("Логин не может быть пустым или состоять только из пробелов"))
                .andReturn();
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ya@mail.com\",\"name\":\"123\",\"birthday\":\"2020-04-04\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.violations[0].message").value("Логин не может быть пустым или состоять только из пробелов"))
                .andReturn();
    }

    //изменение
    @Test
    public void updateUserTestCorrect() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"ya@mail.com\",\"login\":\"Testusers1\",\"name\":\"123\",\"birthday\":\"2019-04-04\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\", \"email\":\"ya@mail.com\",\"login\":\"Testusers1\",\"name\":\"ddd\",\"birthday\":\"2019-04-04\"}")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.login").value("Testusers1"))
                .andExpect(jsonPath("$.email").value("ya@mail.com"))
                .andExpect(jsonPath("$.name").value("ddd"))
                .andExpect(jsonPath("$.birthday").value("2019-04-04") )
                .andReturn();
    }



}
