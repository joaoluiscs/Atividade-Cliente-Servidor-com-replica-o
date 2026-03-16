package com.unifor.br.server_primary.controller;

import com.unifor.br.server_primary.service.ElectionState;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/election")
public class ElectionController {

    private final ElectionState state;

    public ElectionController(ElectionState state) {
        this.state = state;
    }

    @PostMapping("/vote")
    public synchronized boolean vote(
            @RequestParam int term,
            @RequestParam(required = false) String candidate
    ) {

        System.out.println("Pedido de voto recebido | term=" + term + " | candidate=" + candidate);

        // se o termo recebido for maior, atualiza estado
        if (term > state.getCurrentTerm()) {

            state.setCurrentTerm(term);
            state.resetVote();
            state.setState(ElectionState.NodeState.FOLLOWER);

            System.out.println("Atualizando term para " + term);

        }

        // concede voto se ainda não votou
        if (term == state.getCurrentTerm() && state.getVotedFor() == null) {

            state.setVotedFor(candidate);

            System.out.println("Voto concedido para " + candidate);

            return true;

        }

        System.out.println("Voto negado");

        return false;

    }

}