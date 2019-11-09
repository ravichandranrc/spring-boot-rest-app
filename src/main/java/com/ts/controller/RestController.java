package com.ts.controller;

import com.ts.model.*;
import com.ts.service.NodeHolderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class RestController {
    private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class);

    @Autowired
    NodeHolderService nodeHolderService;

    @GetMapping("/hello-world")
    @ResponseBody
    public Greeting sayHello(@RequestParam(name = "name", required = false, defaultValue = "world") String name) {
        return new Greeting(1, String.format("Hello, %s", name));
    }

    @GetMapping("/api/v1/tree/allDescendants")
    @ResponseBody
    public Response getAllDescendants(@RequestParam(name = "id", required = true) String id) {
        logger.info("getAllDescendants for the Node " + id);
        Set<Node> descendants = getNodeHolderService().findDescendants(id);
        return new Response(descendants);
    }

    @PutMapping("/api/v1/tree/{id}/parent/{parentId}")
    @ResponseBody
    public void changeParent(@PathVariable("id") String id, @PathVariable("parentId") String parentId) throws ResourceNotFoundException {
        logger.info("changeParent: Node " + id + ", Parent: " + parentId);
        getNodeHolderService().changeParent(id, parentId);
    }

    public NodeHolderService getNodeHolderService() {
        return nodeHolderService;
    }

    public void setNodeHolderService(NodeHolderService nodeHolderService) {
        this.nodeHolderService = nodeHolderService;
    }
}
