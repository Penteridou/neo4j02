// graph static visualization
// https://medium.com/neo4j/graph-visualization-with-neo4j-using-neovis-js-a2ecaaa7c379   


$(document).ready(function(){
console.log("vis script running")
      var config = {
        container_id :"viz",
        server_url:"bolt://localhost:7687",
        server_user:"neo4j",
        server_password:"pass",
        labels: {
        "Genre": {
                                caption: "user_key",
                                size: "pagerank",
                                community: "community"
                            }

        },
        relationships:{
         "HAS_CATEGORY": {
                                caption: "user_key" ,
                                thickness: "count"
                            }

        },
        initial_cypher: "MATCH p=(a)-[r]-(b) return p"
      }

      var viz = new NeoVis.default(config);
      viz.render();

});

function visThis(query){
// visualization based on query
}