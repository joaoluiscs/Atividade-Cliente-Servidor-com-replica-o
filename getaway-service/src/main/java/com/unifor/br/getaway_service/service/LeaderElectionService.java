package com.unifor.br.getaway_service.service;

import com.unifor.br.getaway_service.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

@Service
public class LeaderElectionService {

    private final RestTemplate restTemplate;

    public LeaderElectionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;

        nodeWasDown.put(NODE1, false);
        nodeWasDown.put(NODE2, false);
        nodeWasDown.put(NODE3, false);
    }

    // -------------------------------
    // Nodes do cluster
    // -------------------------------

    private final String NODE1 = "http://localhost:8080";
    private final String NODE2 = "http://localhost:8081";
    private final String NODE3 = "http://localhost:8082";

    private final List<String> clusterNodes = List.of(
            NODE1,
            NODE2,
            NODE3
    );

    // -------------------------------
    // Estado do cluster
    // -------------------------------

    private String currentLeader = NODE1;

    private int term = 0;

    private boolean electionInProgress = false;

    private final Random random = new Random();

    private final Map<String, Boolean> nodeWasDown = new HashMap<>();


    // -------------------------------
    // Leader atual
    // -------------------------------

    public String getLeader() {
        return currentLeader;
    }


    // -------------------------------
    // Heartbeat
    // -------------------------------

    private boolean isAlive(String serverUrl) {

        try {

            restTemplate.getForObject(serverUrl + "/health", String.class);

            System.out.println("Heartbeat OK -> " + serverUrl);

            return true;

        } catch (Exception e) {

            System.out.println("Heartbeat FAIL -> " + serverUrl);

            return false;

        }

    }


    // -------------------------------
    // Request Vote
    // -------------------------------

    private boolean requestVote(String serverUrl) {

        try {

            Boolean vote = restTemplate.postForObject(
                    serverUrl + "/election/vote?term=" + term,
                    null,
                    Boolean.class
            );

            return vote != null && vote;

        } catch (Exception e) {

            System.out.println("Falha ao solicitar voto para " + serverUrl);

            return false;

        }

    }


    // -------------------------------
    // Eleição de líder
    // -------------------------------

    private synchronized void electLeader(List<String> aliveNodes) {

        if (electionInProgress) {
            return;
        }

        electionInProgress = true;

        try {

            System.out.println("=== INICIANDO ELEIÇÃO ===");

            term++;

            System.out.println("Termo atual -> " + term);
            System.out.println("Nós disponíveis -> " + aliveNodes);

            int quorum = (aliveNodes.size() / 2) + 1;

            System.out.println("Quorum necessário -> " + quorum);

            String newLeader = null;

            for (String candidate : aliveNodes) {

                int votes = 1;

                for (String voter : aliveNodes) {

                    if (!voter.equals(candidate)) {

                        if (requestVote(voter)) {
                            votes++;
                        }

                    }

                }

                System.out.println("Candidato " + candidate + " recebeu votos -> " + votes);

                if (votes >= quorum) {

                    newLeader = candidate;
                    break;

                }

            }

            if (newLeader == null) {

                newLeader = aliveNodes.get(0);

                System.out.println("Nenhuma maioria clara. Escolhendo primeiro nó ativo.");

            }

            currentLeader = newLeader;

            System.out.println("Novo líder eleito -> " + currentLeader);
            System.out.println("==========================");

        }
        finally {

            electionInProgress = false;

        }

    }


    // -------------------------------
    // Sincronização de dados
    // -------------------------------

    private void syncNode(String node) {

        try {

            if (node.equals(currentLeader)) {
                return;
            }

            User[] users = restTemplate.getForObject(
                    currentLeader + "/users",
                    User[].class
            );

            if (users == null || users.length == 0) {

                System.out.println("Node sincronizado com estado atual do cluster.");

                return;

            }

            System.out.println("Sincronizando dados para " + node);

            restTemplate.postForObject(
                    node + "/users/sync",
                    users,
                    Void.class
            );

            System.out.println("Sincronização concluída -> " + node);

        }
        catch (Exception e) {

            System.out.println("Falha silenciosa ao sincronizar -> " + node);

        }

    }


    // -------------------------------
    // Monitor do cluster
    // -------------------------------

    @Scheduled(fixedRate = 10000)
    public void clusterMonitor() {

        System.out.println("\n===== CLUSTER MONITOR =====");

        List<String> aliveNodes = new ArrayList<>();

        for (String node : clusterNodes) {

            if (isAlive(node)) {
                aliveNodes.add(node);
            }

        }

        if (aliveNodes.isEmpty()) {

            System.out.println("Nenhum servidor disponível no cluster.");
            return;

        }

        boolean leaderAlive = aliveNodes.contains(currentLeader);

        if (!leaderAlive) {

            int timeout = 2000 + random.nextInt(3000);

            System.out.println("Leader caiu -> " + currentLeader);
            System.out.println("Election timeout -> " + timeout + " ms");

            try {
                Thread.sleep(timeout);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            electLeader(aliveNodes);

        }
        else {

            System.out.println("Cluster estável.");

        }


        // detectar nós que voltaram

        for (String node : clusterNodes) {

            boolean alive = aliveNodes.contains(node);
            boolean wasDown = nodeWasDown.get(node);

            if (!alive) {

                nodeWasDown.put(node, true);

            }
            else {

                if (wasDown) {

                    System.out.println("Node voltou ao cluster -> " + node);

                    syncNode(node);

                    nodeWasDown.put(node, false);

                }

            }

        }

        System.out.println(
                "CLUSTER STATE -> LEADER: " +
                        currentLeader +
                        " | TERM: " +
                        term
        );

        System.out.println("===========================\n");

    }

}