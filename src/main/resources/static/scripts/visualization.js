// graph static visualization
// https://medium.com/neo4j/graph-visualization-with-neo4j-using-neovis-js-a2ecaaa7c379

$(document).ready(function(){
   $("#draw").click(function(){
            //MODAL DISPLAY---------------------------------------------
            // Get the modal
            var modal = document.getElementById("myModal0");
            modal.style.display = "block";
            //MODAL HANDLING
            // When the user clicks on <span> (x), close the modal
            var span = document.getElementsByClassName("close0")[0];
             span.onclick = function() {
               modal.style.display = "none";
             }

             // When the user clicks anywhere outside of the modal, close it
             window.onclick = function(event) {
               if (event.target == modal) {
                 modal.style.display = "none";
               }
            }


            $(document).ready(function(){
            console.log("vis script running")
                  var config = {
                    container_id :"vizModal0",
                    server_url:"bolt://localhost:7687",
                    server_user:"neo4j",
                    server_password:"pass",
                    labels: {
            //        "Genre": {
            //                                caption: "user_key",
            //                                size: "pagerank",
            //                                community: "community"
            //                            }

                    },
                    relationships:{
            //         "HAS_CATEGORY": {
            //                                caption: "user_key" ,
            //                                thickness: "count"
            //                            }

                    },
                    initial_cypher: "MATCH p=(a)-[r]-(b) return p"
                  }

                  var viz = new NeoVis.default(config);
                  viz.render();

            });

    });
});
function visThis(query){
// visualization based on query
}