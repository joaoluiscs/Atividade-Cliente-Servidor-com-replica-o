package com.unifor.br.server_primary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Endpoint simples de heartbeat.
 *
 * Utilizado para verificar se o servidor está ativo.
 * Inspirado no mecanismo de heartbeat do algoritmo Raft.
 */

@RestController
public class HeartbeatController {

    @GetMapping("/heartbeat")
    public String heartbeat() {
        return "alive";
    }
}