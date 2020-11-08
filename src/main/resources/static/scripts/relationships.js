
//load relationships buttons according to the clicked relationship and add classes
    $(document).ready(function(){
      $("#result").on("click", "button.showrelationshiptypes", function(){ //DELEGATION
          console.log("showrelationshiptypes running");
          var rel =$(this).text()
          localStorage.setItem("currentRelationshipType", rel);
          $("#result2").empty();
          $("#result3").empty();
          $("<button>" + rel + " properties</button>" ).addClass("relProperties").appendTo( $("#result2"));
          $("<button>" + rel+ " involved nodes</button>" ).addClass("relativeNodes").appendTo( $("#result2"));
          countRel(rel);
      });
    });



 //count the existed relationship
function countRel(rel){
     $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/relationship/count/' + rel,
          dataType : "json",
          contentType:"application/json",
          success: function(data){
              console.log(data);
              var val= JSON.stringify(data).replace("count(p)\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');
              $("<label>There are " + val +" relationships of "+rel+" type </label>" ).addClass("counterLabel").attr("value", $(this).text()).appendTo( $("#result3"));          }
      });

  }



// relationship properties button onclick CLASS .relProperties
    $(document).ready(function(){
      $("#result2").on("click", "button.relProperties", function(){
       console.log("relProperties running");
       $("#result").empty();
      var rel = $( this ).text().replace("properties", "").replace(/[^a-zA-Z ]/g, "");
          ajaxForRel(rel);
       });
     });

// relationship properties button onclick CLASS .relPropertiesValues
    $(document).ready(function(){
      $("#result").on("click", "button.relPropertiesValues", function(){
       console.log("relPropertiesValues running");
       $("#result2").empty();
      var prop = $( this ).text();
          ajaxForRelValues(prop); //relationship/propertiesvalues/rank
       });
     });

 // nodes involved in relationships button onclick CLASS .relativeNodes
       $(document).ready(function(){
          $("#result2").on("click", "button.relativeNodes", function(){
                console.log("relativeNodes running");
               $("#result").empty();
              var rel = $( this ).text().replace("involved nodes", "").replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, "");
              alert(rel);

              ajaxForNodesRel(rel);

               });
         });

 // all instances of relationship button onclick TO FIX
        $(document).ready(function(){
            $("#allTypeRel").click(function(){

             $("#result").empty();
            var rel = $( "#relationshipsReturned option:selected" ).text();
                   $.ajax({
                        type: 'GET',
                        url: 'http://localhost:8080/relationship/allrelationshipsof/' + rel,
                        dataType : "json",
                        contentType:"application/json",
                        success: function(data){
                            console.log(data);
                             var items = [];
                            $.each( data, function( key, val ) {
                              items.push( "<button id='" + JSON.stringify(key) + "'>" + JSON.stringify(val) + "</button>" );
                            });

                            $( "<ul/>", {
                              "class": "my-new-list",
                              html: items.join( "" )
                            }).appendTo( "#result" );
                        }
                    });
            });
          });

 // CHECKREL button onclick TO FIX all
$(document).ready(function(){
    $(".checkRelclass").on("click", "button.checkRelclass", function(){
            // $("#result").empty();
             alert("ok");

           // var rel = $( "#relationshipsReturned option:selected" ).text();
//                   $.ajax({
//                        type: 'GET',
//                        url: 'http://localhost:8080/relationship/allrelationshipsof/' + rel,
//                        dataType : "json",
//                        contentType:"application/json",
//                        success: function(data){
//                            console.log(data);
//                             var items = [];
//                            $.each( data, function( key, val ) {
//                              items.push( "<button id='" + JSON.stringify(key) + "'>" + JSON.stringify(val) + "</button>" );
//                            });
//
//                            $( "<ul/>", {
//                              "class": "my-new-list",
//                              html: items.join( "" )
//                            }).appendTo( "#result" );
//                        }
//                    });
            });
          });


//-----------AJAX CALLS-------------------------------------------------------------------------------------------

function ajaxForRel(rel){

      $.ajax({
           type: 'GET',
           url: 'http://localhost:8080/relationship/properties/' + rel,
           dataType : "json",
           contentType:"application/json",
           success: function(data){

               console.log(data);

                   var items = [];
                    $.each( data, function( key, val ) {
                     console.log("relProperties data returned: ", val.value)
                     items.push( "<button class='relPropertiesValues'>" + val.value + "</button>" );
                    });

                    $( "<div/>", {
                      "class": "my-new-list",
                      html: items.join( "" )
                    }).appendTo( "#result" );
           }
       });

}

function ajaxForRelValues(prop){

      $.ajax({
           type: 'GET',
           url: 'http://localhost:8080/relationship/propertiesvalues/' + localStorage.getItem("currentRelationshipType") +'/'+ prop,
           dataType : "json",
           contentType:"application/json",
           success: function(data){

               console.log(data);

                   var items = [];
                   var propTable = $('<table>').addClass('relTable'); //copied
                    $.each( data, function( key, val ) {
                         var items = [];
                         var value = JSON.stringify(val).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');
                         console.log("check json: ", value);
                             items.push( "<td>" + value + "</td>" );
                        // var btn= $( "<button>check relationships</button>").addClass("checkRelBtn").attr("id",val.value.id);
                         var row = $('<tr>').addClass('bar').append(items.join("")); //.append(btn);
                         //console.log("btn id is:", val.value.id);
                         propTable.append(row);
                         //  items.push( "<label>next-----------</label>" );
                    });

                  $(".box2").empty();
                  $(propTable).appendTo($( "<div style='overflow-x:auto'>Table</div>" ).insertAfter( ".box2" ));
           }
       });

}



function ajaxForNodesRel(rel){

      $.ajax({
           type: 'GET',
           url: 'http://localhost:8080/relationship/nodesinvolved/' + rel,
           dataType : "json",
           contentType:"application/json",
           success: function(data){
               console.log(data);

               var items = [];
                $.each( data, function( key, val ) {
                //get the first label from relationship json file
                 var labA = JSON.stringify(val).substr(0, JSON.stringify(val).indexOf(']')).split("[").pop();
                 var labelA= labA.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, "");
                //get the second label from relationship json file
                 var labB = JSON.stringify(val).split("[").pop().substr(0, JSON.stringify(val).split("[").pop().indexOf(']'));
                 var labelB= labB.replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, "");

                  console.log("labels: ", labelA,labelB);
                  items.push( "<button class='shownodes'>" + labelA + "</button>" );
                  items.push( "<label>-</label>" );
                  items.push( "<button class='showrelationshiptypes'>" + rel + "</button>" );
                  items.push( "<label>-></label>" );
                  items.push( "<button class='shownodes'>" + labelB + "</button>" );
                });

                $( "<div/>", {
                  "class": "my-new-list",
                  html: items.join( "" )
                }).appendTo( "#result" );

           }
       });

}