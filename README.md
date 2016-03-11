Sample Neo4j unmanaged extension
================================

This is an unmanaged extension to get a spanning tree given a
relationship type. Definitely a naive solution and not super flexible at
the moment.It's a learning project :)

1. Build it: 

        ./build.sh

2. Run `docker-compose up`

3. Open the neo4j browser at port 7474, `:play movies`, and execute the
cypher query to create the movies data set.

4. Query it over HTTP (assumes docker machine IP):

```
curl http://192.168.99.100:7474/example/service/spanningTree/ACTED_IN
```

5. Alternatively, open index.html in a browser and wait a moment for the
visualization to render. Zoom, pan, interact w/ nodes, etc. (IP
hardcoded, change in the javascript for different docker IP)
