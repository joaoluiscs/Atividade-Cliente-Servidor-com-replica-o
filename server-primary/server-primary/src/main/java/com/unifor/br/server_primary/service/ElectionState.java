package com.unifor.br.server_primary.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ElectionState {

    // termo atual
    private int currentTerm = 0;

    // servidor para quem votou no termo atual
    private String votedFor = null;

    // estado do nó
    private NodeState state = NodeState.FOLLOWER;

    public void resetVote() {
        votedFor = null;
    }

    public enum NodeState {
        FOLLOWER,
        CANDIDATE,
        LEADER
    }

}