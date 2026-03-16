package com.unifor.br.server_replica.repository;

import com.unifor.br.server_replica.model.User;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryReplica {

    private final String FILE_NAME = "database_replica2.txt";

    public void save(User user) throws IOException {

        FileWriter fw = new FileWriter(FILE_NAME, true);

        fw.write(
                user.getId() + "," +
                        user.getName() + "," +
                        user.getEmail() + "\n"
        );

        fw.close();
    }

    public List<User> findAll() throws IOException {

        Path path = Paths.get(FILE_NAME);

        if (!Files.exists(path)) {
            return new ArrayList<>();
        }

        return Files.lines(path)
                .map(this::parseUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private User parseUser(String line) {

        try {

            String[] parts = line.split(",");

            if (parts.length < 3) {
                return null;
            }

            Long id = null;

            if (!parts[0].equals("null") && !parts[0].isEmpty()) {
                id = Long.parseLong(parts[0]);
            }

            String name = parts[1];
            String email = parts[2];

            return new User(id, name, email);

        } catch (Exception e) {

            System.out.println("Linha inválida ignorada na réplica: " + line);

            return null;
        }
    }

    public void overwriteAll(List<User> users) throws IOException {

        Path path = Paths.get(FILE_NAME);

        List<String> lines = users.stream()
                .map(user ->
                        user.getId() + "," +
                                user.getName() + "," +
                                user.getEmail()
                )
                .collect(Collectors.toList());

        Files.write(
                path,
                lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }
}