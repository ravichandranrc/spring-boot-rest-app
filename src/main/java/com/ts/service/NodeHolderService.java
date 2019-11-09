package com.ts.service;

import com.ts.model.Node;
import com.ts.model.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class does the following
 * 1. loads the nodes from csv into in-memory when server startup
 * 2. findDescendants of the given node id
 * 3. Change the parent of the given node id into given parent id
 * <p>
 * <p>
 * allNodesMap = O(N) // This map contains all the nodes in the Tree.
 * parentChildMap = O(N) // This map contains the parent child Relationship
 * <p>
 * Space Complexity is: O(N) + O(N) = O(2N) = O(N)
 */
public class NodeHolderService {
    private static final Logger logger = LogManager.getLogger(NodeHolderService.class);
    private static final String COMMA = ",";

    // map contains all the nodes with the Key as Id and Value as Node (id -> Node)
    // map is used to look up the node from the Id
    Map<String, Node> allNodesMap = new ConcurrentHashMap<>();

    // map contains parent child relations with the Key as Parent Node Id and Value as List of children Node Ids
    // this map is additional space to reduce the time complexity for the function (findDescendants)
    private Map<String, List<String>> parentChildMap = new ConcurrentHashMap<>();

    public NodeHolderService(String dataFile) throws Exception {
        processInputFile(dataFile);
    }

    /**
     * This method is initialized when the server startup.
     * 1. it reads the record from the csv
     * 2. And creates the Node
     * 3. Populate the allNodesMap to map the id with Node
     * 4. Populate the parentChildMap to map the Node with childrens.
     *
     * @param dataFile
     * @throws Exception
     */
    private void processInputFile(String dataFile) throws Exception {
        logger.info("Loading file " + dataFile);
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/" + dataFile)))) {
            // skips the header of the csv
            String line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(COMMA);
                String id = columns[0];
                Node node = new Node(id);
                if (columns.length == 3) {
                    String parent = columns[1];
                    String root = columns[2];
                    node.setParent(parent);
                    node.setRoot(root);
                    node.setHeight(allNodesMap.get(parent).getHeight() + 1);
                    List<String> children = parentChildMap.getOrDefault(parent, new ArrayList<>());
                    children.add(id);
                    parentChildMap.put(parent, children);
                }
                allNodesMap.put(id, node);
                logger.info("Node loaded:" + node.toString());
            }
        }
    }

    /**
     * This method finds the all descendant nodes from the parentChildMap
     * Since we preprocessed store the relationship in the parentChildMap.
     * <p>
     * Time Complexity is O(N)
     *
     * @param id for which the descendants needs to be find out
     * @return
     */
    public Set<Node> findDescendants(String id) {
        if (!allNodesMap.containsKey(id))
            throw new IllegalArgumentException(String.format("Id [%s] doesn't exist", id));
        Set<Node> allChildren = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(id);
        while (!queue.isEmpty()) {
            // pop a node off the queue
            String tempId = queue.remove();
            List<String> children = parentChildMap.get(tempId);
            if (children != null) {
                for (String child : children) {
                    allChildren.add(allNodesMap.get(child));
                    queue.add(child);
                }
            }
        }
        return allChildren;
    }

    /**
     * This method changes the parent of the node
     * Time Complexity is O(1) Mostly since we use Map for lookup
     *
     * @param id          node id whose parents needs to be changed
     * @param newParentId - new parent that needs be assigned to
     * @return
     * @throws ResourceNotFoundException
     */
    public void changeParent(String id, String newParentId) throws ResourceNotFoundException {
        if (!allNodesMap.containsKey(id))
            throw new ResourceNotFoundException(String.format("Id [%s] doesn't exist", id));
        if (!allNodesMap.containsKey(newParentId))
            throw new ResourceNotFoundException(String.format("ParentId [%s] doesn't exist", newParentId));

        Node node = allNodesMap.get(id);
        String oldParentId = node.getParent();
        List<String> oldChildrens = parentChildMap.get(oldParentId);
        List<String> newChildrens = parentChildMap.get(newParentId);
        // Below logic needs to be Atomic.May need to use ReadWriteLock to allow read and write concurrently
        node.setParent(newParentId);
        node.setHeight(allNodesMap.get(newParentId).getHeight() + 1);
        oldChildrens.remove(id);
        newChildrens.add(id);
        parentChildMap.put(oldParentId, oldChildrens);
        parentChildMap.put(newParentId, newChildrens);
    }
}
