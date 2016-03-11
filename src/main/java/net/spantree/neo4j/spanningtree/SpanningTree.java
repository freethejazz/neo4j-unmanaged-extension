package net.spantree.neo4j.spanningtree;

import org.neo4j.graphdb.*;
import java.util.*;
import java.util.logging.Logger;


public class SpanningTree {
    Logger log = Logger.getLogger(SpanningTree.class.getName());

    GraphDatabaseService db;
    RelationshipType relationshipType;
    HashSet<Map> nodes = new HashSet<Map>();
    HashSet<Map> rels = new HashSet<Map>();

    enum Labels implements Label {
        Person
    }

    public SpanningTree(GraphDatabaseService db, RelationshipType relationshipType) {
        this.db = db;
        this.relationshipType = relationshipType;
    }

    public SpanningTree(GraphDatabaseService db, String relationshipType) {
        this(db, DynamicRelationshipType.withName(relationshipType));
    }

    public HashMap traverse() {
        HashMap results;
        try (Transaction tx = db.beginTx()) {
            //TODO the label here should be configurable
            Node starter = db.findNodes(Labels.Person).next();

            traverseChildren(starter);
            results = getResults();
            results.put("root", serializeNode(starter));

            tx.success();
        }
        return results;
    }

    private void traverseChildren(Node node) {
        log.info("Adding node of id: " + node.getId());
        nodes.add(serializeNode(node));
        for (Relationship childRel : node.getRelationships(relationshipType)) {
            Node childNode = childRel.getOtherNode(node);
            //TODO use a better way to check if a node is taken care of than serializing it
            if(!nodes.contains(serializeNode(childNode))) {
                log.info("Adding relationship r in " + node.getId() + "-[r]->" + childNode.getId());
                rels.add(serializeRelationship(childRel, node, childNode));
                log.info("Traversing child node " + childNode.getId());
                traverseChildren(childNode);
            } else {
                log.info("Child node " + childNode.getId() + " is already in set");
            }
        }
    }

    private Map serializeNode(Node node) {
        Map serializedNode = new HashMap();
        serializedNode.put("id", node.getId());
        serializedNode.put("properties", node.getAllProperties());
        return serializedNode;
    }

    private Map serializeRelationship(Relationship rel, Node startNode, Node otherNode) {
        Map serializedRel = new HashMap();
        serializedRel.put("id", rel.getId());
        serializedRel.put("startNode", startNode.getId());
        serializedRel.put("otherNode", otherNode.getId());
        serializedRel.put("properties", rel.getAllProperties());
        return serializedRel;
    }

    public HashMap getResults() {
        HashMap results = new HashMap();
        results.put("nodes", nodes);
        results.put("relationships", rels);
        return results;
    }

}
