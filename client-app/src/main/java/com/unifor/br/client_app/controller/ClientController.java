package com.unifor.br.client_app.controller;

import com.unifor.br.client_app.model.User;
import com.unifor.br.client_app.service.ClientService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {

        return service.createUser(user);

    }

    @GetMapping("/users")
    public List<User> list() {

        return service.listUsers();

    }
}