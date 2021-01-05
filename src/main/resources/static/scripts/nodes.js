
//load nodes buttons according to the clicked node and add classes
  $(document).ready(function(){
    $("#result").on("click", "button.shownodes", function(){ //DELEGATION
          console.log("shownodes running");
           $(this).siblings().removeClass("buttonpressed") ;
           $(this).addClass("buttonpressed") ;
         // Store
         var node = $( this ).text();
        localStorage.setItem("currentNode", node);
       $("#result2").empty();
       $("#result3").empty();
        $("<button>" + node + " node properties</button>" ).addClass("nodeProperties").attr("value", $(this).text()).appendTo( $("#result2"));
        $("<button>" + node + " node relationships</button>" ).addClass("nodeRelationships").attr("value", $(this).text()).appendTo( $("#result2"));
        $("<button>check all " + node +" nodes</button>" ).addClass("allNodes").attr("value", $(this).text()).appendTo( $("#result2"));
        countNode(node);
    });
  });



//clear results when change list option
 $(document).ready(function(){
    $( "#listReturned" ).change(function() {
        $("#result").empty();
    });
 });


  //count node instances
  function countNode(node){
     $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/node/count/' + node,
          dataType : "json",
          contentType:"application/json",
          success: function(data){
              console.log(data);
              var val= JSON.stringify(data).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');
              $("<label>There are " + val +" instances of "+node+" node </label>" ).addClass("counterLabel").attr("value", $(this).text()).appendTo( $("#result3"));
          }
      });

  }

//node properties buttons onclick CLASS .nodeProperties
   $(document).ready(function(){
      $("#result2").on("click", "button.nodeProperties", function(){
         console.log("nodeProperties running");
         $(this).addClass("buttonpressed") ;
       $("#result").empty();
      var node = $( this ).val();  //text().replace("properties", "").replace(/[^a-zA-Z ]/g, "");
     //  localStorage.setItem("currentNode", node);
      //alert(node);
            ajaxAllpropertiesOf(node);
      });
    });

// node relationships buttons onclick CLASS .nodeRelationships
   $(document).ready(function(){
      $("#result2").on("click", "button.nodeRelationships", function(){
       console.log("nodeRelationships running");
       $("#result").empty();
     //  $("#result2").children().not($( this )).hide();
     // var node = $( this ).text().replace("relationships", "").replace(/[^a-zA-Z ]/g, "");
     var node = localStorage.getItem("currentNode"); //$( this ).val();
         // Store
       //  localStorage.setItem("currentNode", node);
     // alert(node);
            ajaxAllrelTypesOf(node);

          });
        });

// ALL node instances button onclick
$(document).ready(function(){
    $("#result2").on("click", "button.allNodes", function(){
      console.log("allNodes running");
    // $("#result").empty();
    var node = $( this ).val();
    // Store
  //  localStorage.setItem("currentNode", node);
          ajaxAllnodesOf(node);
    });
});

//check specific node relationships button , based on clicked button id (same as node id)---> CHECKRELBUTTON
$(document).ready(function(){
    $('#theTable').on("click", "button.checkRelBtn", function(){
        alert("checkRelBtn works!");
        $('#theTable2').empty();
        $('#viz').empty();
        console.log("button id:", $(this).attr("id")); //clicked button id
       // $(this).closest('.bar').children().removeClass("rowColored")
        $(this).closest('.bar').addClass("rowColored") ;   //children().css('backgroundColor', '#ffdc87');
        $(this).closest('.bar').siblings().removeClass('rowColored');
        var nodeId = $(this).attr("id");
        var node = localStorage.getItem("currentNode");
         $.ajax({
              type: 'GET',
              url: 'http://localhost:8080/node/relationships/' + node+ '/'+ nodeId ,
              dataType : "json",
              contentType:"application/json",
              success: function(data){
                var table = $('<table>').addClass('resultTable');
                $.each( data, function( key, val ) {
                    //console.log("stringify:",JSON.stringify(val)); //.replace("{\"value\":", "").replace(/\}}/g, "}"));
                   // console.log("name",val.value.name);

                           console.log("stringify:",JSON.stringify(val)); //.replace("{\"value\":", "").replace(/\}}/g, "}"));
                           var propertiesArray= val.value.propertyList;

                           console.log("not merged------",val.value);

                           var valueFinal='';

                           if (typeof propertiesArray !== 'undefined' && propertiesArray.length > 0) {
                               console.log("before-----------",JSON.stringify(val.value));
                               var valueF = {};
                                valueF.id = val.value.id;
                                for (let i = 0; i < propertiesArray.length; ++i){
                                   console.log('proparr',propertiesArray[i])
                                    console.log('key value',propertiesArray[i].key, propertiesArray[i].value)
                                   valueF[propertiesArray[i].key] = propertiesArray[i].value;

                                }

                               console.log("value fixed------",valueF );
                               valueFinal= JSON.stringify(valueF).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');;

                           }else{
                                valueFinal = JSON.stringify(val).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');

                           }


                    var items = [];
                    var arr = valueFinal.split(',');
                    console.log("check json: ", arr);
                    for (let i = 0; i < arr.length; ++i) {
                           console.log(arr[i]);
                        if(!arr[i].endsWith("null")){
                           // alert("null value")
                         items.push( "<td>" + arr[i] + "</td>" );
                        }
                    }
                    console.log("btn2 id is:", val.value.id);
                    console.log("btn2 node attribute will be", val.theOtherNode);
                    var btn= $( "<button>check relationships</button>").addClass("checkRelBtn2").attr("id",val.value.id).attr("node",val.theOtherNode); // CODE ADAPTION ~~
                    var vizBtn=$( "<button>viz relationships</button>").addClass("vizRelBtn").attr("vid",val.value.id);
                    var row = $('<tr>').addClass('bar').append(items.join("")).append(btn).append(vizBtn);
                    //console.log("btn id is:", val.value.id);
                    table.append(row);
                });
                $(table).appendTo($('#theTable2'));//.attr("id",'theTable').insertAfter( ".box2" ));

              }
          });
    });
});

//check specific node relationships button , based on clicked button id (same as node id)---> CHECKRELBUTTON2
$(document).ready(function(){
    $('#theTable2,#theTable').on("click", "button.checkRelBtn2", function(){
        alert("checkRelBtn2 works!");
        $('#theTable').empty();
        $('#viz').empty();
       // $('#theTable2').empty();
        console.log("button id:", $(this).attr("id")); //clicked button id
        $(this).closest('.bar').children().removeClass("rowColored")
        $(this).closest('.bar').addClass("rowColored") ;   //children().css('backgroundColor', '#ffdc87');
        $(this).closest('.bar').siblings().removeClass('rowColored');
        var clickedNode = $(this).attr("node");
        localStorage.setItem("currentNode", clickedNode);
        var nodeId = $(this).attr("id");
        var node = localStorage.getItem("currentNode");
         $.ajax({
              type: 'GET',
              url: 'http://localhost:8080/node/relationships/' + node+ '/'+ nodeId ,
              dataType : "json",
              contentType:"application/json",
              success: function(data){
                var table = $('<table>').addClass('resultTable');
                $.each( data, function( key, val ) {
                    //console.log("stringify:",JSON.stringify(val)); //.replace("{\"value\":", "").replace(/\}}/g, "}"));
                   // console.log("name",val.value.name);
                    console.log("stringify:",JSON.stringify(val)); //.replace("{\"value\":", "").replace(/\}}/g, "}"));
                          var propertiesArray= val.value.propertyList;

                          console.log("not merged------",val.value);

                          var valueFinal='';

                          if (typeof propertiesArray !== 'undefined' && propertiesArray.length > 0) {
                              console.log("before-----------",JSON.stringify(val.value));
                              var valueF = {};
                               valueF.id = val.value.id;
                               for (let i = 0; i < propertiesArray.length; ++i){
                                  console.log('proparr',propertiesArray[i])
                                   console.log('key value',propertiesArray[i].key, propertiesArray[i].value)
                                  valueF[propertiesArray[i].key] = propertiesArray[i].value;

                               }

                              console.log("value fixed------",valueF );
                              valueFinal= JSON.stringify(valueF).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');;

                          }else{
                               valueFinal = JSON.stringify(val).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');

                          }

                    var items = [];
                    var arr = valueFinal.split(',');
                    console.log("check json: ", arr);
                    for (let i = 0; i < arr.length; ++i) {
                           console.log(arr[i]);
                        if(!arr[i].endsWith("null")){
                           // alert("null value")
                         items.push( "<td>" + arr[i] + "</td>" );
                        }
                    }
                    console.log("btn3 id is:", val.value.id);
                    console.log("btn3 node attribute will be", val.theOtherNode);
                    var btn= $( "<button>check relationships again</button>").addClass("checkRelBtn2").attr("id",val.value.id).attr("node",val.theOtherNode);;
                    var vizBtn=$( "<button>viz relationships</button>").addClass("vizRelBtn").attr("vid",val.value.id);
                    var row = $('<tr>').addClass('bar').append(items.join("")).append(btn).append(vizBtn);
                   // var row = $('<tr>').addClass('bar').append(items.join(""));
                    //console.log("btn id is:", val.value.id);
                    table.append(row);
                    //  items.push( "<label>next-----------</label>" );

                });
                $(table).appendTo($('#theTable'));//.attr("id",'theTable').insertAfter( ".box2" ));

              }
          });
    });
});

//node relationships visualization button
$(document).ready(function(){
    $('#theTable2,#theTable').on("click", "button.vizRelBtn", function(){
        alert("visualization btn works!");
         $("#viz").empty();
        console.log("vis script running")
        var nodeId = $(this).attr("vid");
        var node = localStorage.getItem("currentNode");
        var cypher = "MATCH p=( a:" + node+" )-[r]-(b) where ID(a)=" +nodeId+" RETURN p"

              var config = {
                container_id :"viz",
                server_url:"bolt://localhost:7687",
                server_user:"neo4j",
                server_password:"pass",
                labels: {

                },
                relationships:{

                },
                initial_cypher: cypher
              }

              var viz = new NeoVis.default(config);
              viz.render();

    });
});



//-------------AJAX CALLS-----------------------------------------------------------------------------------------------

function  ajaxAllnodesOf(node) {
           console.log("ajaxAllnodesOf function is running");
            $('#theTable').empty();
          $.ajax({
            type: 'GET',
            url: 'http://localhost:8080/node/allnodesof/' + node,
            dataType : "json",
            contentType:"application/json",
            success: function(data){
                var table = $('<table>').addClass('resultTable');
                $.each( data, function( key, val ) {
                    console.log("stringify:",JSON.stringify(val)); //.replace("{\"value\":", "").replace(/\}}/g, "}"));
                    var propertiesArray= val.value.propertyList;

                    console.log("not merged------",val.value);

                    var valueFinal='';

                    if (typeof propertiesArray !== 'undefined' && propertiesArray.length > 0) {
                        console.log("before-----------",JSON.stringify(val.value));
                        var valueF = {};
                         valueF.id = val.value.id;
                         for (let i = 0; i < propertiesArray.length; ++i){
                            console.log('proparr',propertiesArray[i])
                             console.log('key value',propertiesArray[i].key, propertiesArray[i].value)
                            valueF[propertiesArray[i].key] = propertiesArray[i].value;

                         }

                        console.log("value fixed------",valueF );
                        valueFinal= JSON.stringify(valueF).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');;

                    }else{
                         valueFinal = JSON.stringify(val).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');

                    }

                    var items = [];
                    var arr = valueFinal.split(',');
                    console.log("check json: ", arr);
                    for (let i = 0; i < arr.length; ++i) {
                        //  alert(arr[i]);
                          if(!arr[i].endsWith("null")&&!arr[i].endsWith("[]")&&!arr[i].endsWith("false")){
                               // alert("null value")
                             items.push( "<td>" + arr[i] + "</td>" );
                          }
                    }
                    var btn= $( "<button>check relationships</button>").addClass("checkRelBtn").attr("id",val.value.id);
                    var vizBtn=$( "<button>viz relationships</button>").addClass("vizRelBtn").attr("vid",val.value.id);
                    var row = $('<tr>').addClass('bar').append(items.join("")).append(btn).append(vizBtn);
                    console.log("btn id is:", val.value.id);
                    table.append(row);
                    //  items.push( "<label>next-----------</label>" );

                });
                $(table).appendTo($('#theTable'));//.attr("id",'theTable').insertAfter( ".box2" ));
            }

        });
 }

function  ajaxAllpropertiesOf(node) {
     $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/node/properties/' + node,
          dataType : "json",
          contentType:"application/json",
          success: function(data){
              console.log(data);
                  var items = [];
                   $.each( data, function( key, val ) {
                   var test = JSON.stringify(val);
                   console.log("textttt", test);
                  //var value = JSON.stringify(val).replace("value", "").replace(/[^a-zA-Z ]/g, ""); // remove 'value:' from the json value part
                   //console.log("sliced", value);

                     items.push( "<button class='showproperties'" + JSON.stringify(key) + "'>" + val.value + "</button>" );
                   });

                   $( "<div/>", {
                     "class": "my-new-list",
                     html: items.join( "" )
                   }).appendTo( "#result" );
          }
      });
}

function  ajaxAllrelTypesOf(node) {
    $.ajax({
         type: 'GET',
         url: 'http://localhost:8080/node/relationships/' + node,
         dataType : "json",
         contentType:"application/json",
         success: function(data){
             console.log(data);

             var items = [];
              $.each( data, function( key, val ) {
              var value = JSON.stringify(val).replace("value", "").replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, '');
                items.push( "<button class='showrelationshiptypes'>" + value + "</button>" );
              });

              $( "<div/>", {
                "class": "my-new-list",
                html: items.join( "" )
              }).appendTo( "#result" );

         }
     });
}


