package org.neo4j.example.unmanagedextension;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.internal.ServerExecutionEngine;
import org.neo4j.graphdb.*;
import org.neo4j.server.database.CypherExecutor;
import org.neo4j.test.ImpermanentGraphDatabase;
import org.neo4j.test.TestGraphDatabaseFactory;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MyServiceTest {

    private GraphDatabaseService graphDb;
    private MyService service;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final RelationshipType KNOWS = DynamicRelationshipType.withName("KNOWS");
    @Mock
    private CypherExecutor cypherExecutor;

    @Before
    public void setUp() {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        populateDb(graphDb);
        service = new MyService();
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();

    }

    @Test
    public void shouldRespondToHelloWorld() {
        assertEquals("Hello World!", service.helloWorld());
    }

    @Test
    public void shouldGetSpanningTree() throws IOException {
        Response response = service.getSpanningTree("KNOW", graphDb);
        Map map = objectMapper.readValue((String) response.getEntity(), Map.class);

        assertEquals(((List) map.get("nodes")).size(), 6);
        assertEquals(((List) map.get("relationships")).size(), 5);
        assertEquals(response.getStatus(), 200);
    }


    private void populateDb(GraphDatabaseService db) {
        try(Transaction tx = db.beginTx())
        {
            Node personA = createPerson(db, "A");
            Node personB = createPerson(db, "B");
            Node personC = createPerson(db, "C");

            Node personD = createPerson(db, "D");
            Node personE = createPerson(db, "E");
            Node personF = createPerson(db, "F");

            personA.createRelationshipTo(personB, KNOWS);
            personA.createRelationshipTo(personC, KNOWS);

            personB.createRelationshipTo(personA, KNOWS);
            personB.createRelationshipTo(personC, KNOWS);

            personC.createRelationshipTo(personA, KNOWS);
            personC.createRelationshipTo(personB, KNOWS);

            personC.createRelationshipTo(personD, KNOWS);
            personD.createRelationshipTo(personE, KNOWS);
            personC.createRelationshipTo(personF, KNOWS);
            tx.success();
        }
    }

    private Node createPerson(GraphDatabaseService db, String name) {
        Node node = db.createNode(MyService.Labels.Person);
        node.setProperty("name", name);
        return node;
    }

}
