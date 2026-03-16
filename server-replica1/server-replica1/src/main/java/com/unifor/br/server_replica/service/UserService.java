package com.unifor.br.server_replica.service;

import com.unifor.br.server_replica.model.User;
import com.unifor.br.server_replica.repository.UserRepositoryReplica;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

@Service
public class UserService {

    @Value("${server.port}")
    private String serverPort;

    private final UserRepositoryReplica repository;

    private final RestTemplate restTemplate = new RestTemplate();

    private final String NODE1 = "http://localhost:8080/users/replicate";
    private final String NODE2 = "http://localhost:8081/users/replicate";
    private final String NODE3 = "http://localhost:8082/users/replicate";

    public UserService(UserRepositoryReplica repository) {
        this.repository = repository;
    }

    // escrita no líder
    public User save(User user) throws IOException {

        repository.save(user);

        replicateToFollowers(user);

        return user;
    }

    // escrita quando vem de replicação
    public void saveReplica(User user) throws IOException {

        repository.save(user);

    }

    private void replicateToFollowers(User user) {

        replicateIfNotSelf("8080", NODE1, user);
        replicateIfNotSelf("8081", NODE2, user);
        replicateIfNotSelf("8082", NODE3, user);

    }

    private void replicateIfNotSelf(String port, String url, User user) {

        if (!serverPort.equals(port)) {
            replicate(url, user);
        }

    }

    private void replicate(String url, User user) {

        try {

            restTemplate.postForObject(url, user, Void.class);

        } catch (Exception e) {

            System.out.println("Falha ao replicar para -> " + url);

        }

    }

    public List<User> findAll() throws IOException {
        return repository.findAll();
    }

    // sincronização completa
    public void overwrite(List<User> users) throws IOException {

        repository.overwriteAll(users);

        System.out.println("Database sincronizado com líder");

    }

}