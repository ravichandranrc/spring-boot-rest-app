package com.ts.model;

import java.util.Set;

public class Response {
    Set<Node> nodes;

    public Response(Set<Node> nodes) {
        this.nodes = nodes;
    }

    public Set<Node> getNodes() {
        return nodes;
    }
}
