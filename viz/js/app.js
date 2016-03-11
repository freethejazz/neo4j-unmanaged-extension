
//setup graph viz here
$.getJSON('http://192.168.99.100:7474/example/service/spanningTree/ACTED_IN', function(data) {
  var processed = {
    nodes: new vis.DataSet(data.nodes.map(function(d) {
      return {
        id: d.id,
        label: d.properties.name || d.properties.title
      };
    })),
    edges: new vis.DataSet(data.relationships.map(function(d) {
      return {
        from: d.startNode,
        to: d.otherNode
      }
    }))
  };

  // create a network
  var container = document.getElementById('graph-container');
  var options = {};
  var network = new vis.Network(container, processed, options);
});
