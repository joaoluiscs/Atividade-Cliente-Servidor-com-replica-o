package com.unifor.br.server_replica.controller;

import com.unifor.br.server_replica.model.User;
import com.unifor.br.server_replica.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    // criação de usuário (executada no líder)
    @PostMapping
    public User create(@RequestBody User user) throws IOException {
        return service.save(user);
    }

    // usado para replicação
    @PostMapping("/replicate")
    public void replicate(@RequestBody User user) throws IOException {
        service.saveReplica(user);
    }

    // leitura
    @GetMapping
    public List<User> list() throws IOException {
        return service.findAll();
    }

    // sincronização completa
    @PostMapping("/sync")
    public void sync(@RequestBody List<User> users) throws IOException {
        service.overwrite(users);
    }
}