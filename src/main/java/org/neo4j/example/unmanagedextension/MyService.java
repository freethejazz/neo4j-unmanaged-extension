package org.neo4j.example.unmanagedextension;

import net.spantree.neo4j.spanningtree.SpanningTree;
import org.codehaus.jackson.map.ObjectMapper;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

@Path("/service")
public class MyService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    enum Labels implements Label {
        Person
    }

    enum RelTypes implements RelationshipType {
        KNOWS
    }

    @GET
    @Path("/helloworld")
    public String helloWorld() {
        return "Hello World!";
    }

    @GET
    @Path("/spanningTree/{relType}")
    public Response getSpanningTree(@PathParam("relType") String relType, @Context GraphDatabaseService db) throws IOException {
        RelationshipType type = DynamicRelationshipType.withName(relType);

        Set<Long> nodeIds = new HashSet<Long>();

        SpanningTree spanningTree = new SpanningTree(db, relType);
        Map results = spanningTree.traverse();

        ObjectMapper objectMapper = new ObjectMapper();
        return Response.ok().entity(objectMapper.writeValueAsString(results)).build();
    }

}
