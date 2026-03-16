package com.unifor.br.server_replica.controller;

import com.unifor.br.server_replica.service.ElectionState;
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

        if (term > state.getCurrentTerm()) {

            state.setCurrentTerm(term);
            state.resetVote();
            state.setState(ElectionState.NodeState.FOLLOWER);

        }

        if (term == state.getCurrentTerm() && state.getVotedFor() == null) {

            state.setVotedFor(candidate);

            return true;

        }

        return false;

    }

}