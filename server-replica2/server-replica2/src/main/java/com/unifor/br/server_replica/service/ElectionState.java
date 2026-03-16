package com.unifor.br.server_replica.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ElectionState {

    private int currentTerm = 0;

    private String votedFor = null;

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