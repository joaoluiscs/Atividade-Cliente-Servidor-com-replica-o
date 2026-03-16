package com.unifor.br.getaway_service.controller;

import com.unifor.br.getaway_service.model.User;
import com.unifor.br.getaway_service.service.LeaderElectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/getaway")
@RequiredArgsConstructor
public class GetawayController {

    private final LeaderElectionService electionService;
    private final RestTemplate restTemplate;

    // ----------------------------
    // Criar usuário
    // ----------------------------

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {

        String leader = electionService.getLeader();

        String url = leader + "/users";

        return restTemplate.postForObject(
                url,
                user,
                User.class
        );
    }

    // ----------------------------
    // Listar usuários
    // ----------------------------

    @GetMapping("/users")
    public User[] listUsers() {

        String leader = electionService.getLeader();

        String url = leader + "/users";

        return restTemplate.getForObject(
                url,
                User[].class
        );
    }

}