package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.controller.UserController;
import ru.yandex.practicum.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest
public class UserControllerTest {

    private HttpClient client ;
    private HttpResponse<String> response;
    private HttpRequest request;
    private URI url;
    private final int port = 8080;
    String urlController = "http://localhost:8080";
    private static final String DEFAULT_ACCEPT = "application/json";

    private UserController controller;

    @BeforeEach
    void creator() {
        controller = new UserController();
        client = HttpClient.newHttpClient();
    }

    @Test
    public void addUser() throws IOException, InterruptedException {
       // String result = "[{\"id\": 1,\"email\": \"ya@mail.com\",\"login\": \"Testusers\",\"name\": \"TestName\",\"birthday\": \"04.04.2019\"}]";
     /*   User user  = new User();
        user.setEmail("ya@mail.com");
        user.setLogin("Testusers");
        user.setName("TestName");
        user.setBirthday(LocalDate.of(2020,4,4));*/

        url = URI.create(urlController + "/users");
        request = HttpRequest.newBuilder()
                .uri(url)
                .header("Content-type", DEFAULT_ACCEPT)
                .POST(HttpRequest.BodyPublishers.ofString("{\"email\":\"ya@mail.com\",\"login\":\"Testusers\",\"name\":\"TestName\",\"birthday\":\"04.04.2019\"}"))
                .build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Assertions.assertEquals(response.statusCode(),200);
        Assertions.assertEquals(response.body(),"Пользователь добавлен");
        //String response = this.controller.createUser("http://localhost:8080/users","{\"email\":\"ya@mail.com\",\"login\":\"Testusers\",\"name\":\"TestName\",\"birthday\":\"04.04.2019\"}");

        //final ResponseEntity<String> response = template.postForEntity(String.format("http://localhost:%d/api/customers", port), newCustomer, String.class);
    }
}
