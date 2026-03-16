package com.unifor.br.client_app.service;

import com.unifor.br.client_app.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ClientService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String GETAWAY_URL = "http://localhost:9000/getaway";

    public User createUser(User user) {

        return restTemplate.postForObject(
                GETAWAY_URL + "/users",
                user,
                User.class
        );

    }

    public List<User> listUsers() {

        User[] users = restTemplate.getForObject(
                GETAWAY_URL + "/users",
                User[].class
        );

        return Arrays.asList(users);
    }
}