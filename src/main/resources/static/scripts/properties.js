
//load properties buttons according to the clicked property and add classes
$(document).ready(function(){
  $("#result").on("click", "button.showproperties", function(){ //DELEGATION
  console.log("showproperties running");
   $("#result2").empty();
  $('#result2').parent().addClass("grid-item result2GridItem") ;
  $(this).siblings().removeClass("buttonpressed") ;
  $(this).addClass("buttonpressed") ;
    //store
   var prop =  $(this).text();
   localStorage.setItem("currentProperty", prop);
   console.log("current prop== ", prop);
      //alert("rel");
     // $("<button>" + $(this).text() + "property nodes' relationships</button>" ).addClass("propNodeRelationships").appendTo( $("#result2"));
      $("<button>" + prop + " property involved nodes</button>" ).addClass("propNodes").appendTo( $("#result2"));
      if(localStorage.getItem("currentNode")!=""){
         $("<button>" + prop+ " property of "+localStorage.getItem("currentNode")+" node</button>" ).addClass("propOfCurrentNode").appendTo( $("#result2"));
      }
  });
});

//// count button onclick
// $(document).ready(function(){
//    $("#propCount").click(function(){
//     $("#result").empty();
//    var prop = $( "#listReturned option:selected" ).text();
//           $.ajax({
//                type: 'GET',
//                url: 'http://localhost:8080/property/count/' + prop,
//                dataType : "json",
//                contentType:"application/json",
//                success: function(data){
//                    console.log(data);
//                           // alert(  JSON.stringify(data)  );
//                            $("#result").append("<span id='" + JSON.stringify(data) + "'>" + JSON.stringify(data) + "</span>"  )
//                }
//            });
//    });
//  });
//


// properties' involved nodes buttons onclick, CLASS .propNodes
   $(document).ready(function(){
      $("#result2").on("click", "button.propNodes", function(){
      console.log("propNodes running");
       $("#result").empty();
      var prop = $( this ).text().replace("property involved nodes", "").replace(/[^a-zA-Z ]/g, "");
             $.ajax({
                  type: 'GET',
                  url: 'http://localhost:8080/property/nodes/' + prop,
                  dataType : "json",
                  contentType:"application/json",
                  success: function(data){
                      console.log(data);
                          var items = [];
                           $.each( data, function( key, val ) {
                           console.log("val:",JSON.stringify(val));
                            var value = JSON.stringify(val).replace("value", "").replace(/[^a-zA-Z ]/g, "");
                             items.push( "<button class='shownodes'>" + value + "</button>" );
                           });

                           $( "<div/>", {
                             "class": "my-new-list",
                             html: items.join( "" )
                           }).appendTo( "#result" );
                  }
              });
      });
    });

// property values of current node  : SHOW TABLE WITH THE PROPERTY
      $(document).ready(function(){
          $("#result2").on("click", "button.propOfCurrentNode", function(){
           console.log("propOfCurrentNode running");
              $("#result").empty();
              var node = localStorage.getItem("currentNode");
              var prop = localStorage.getItem("currentProperty");
              console.log("prop=",prop);
              console.log("node=", node);
                 $.ajax({
                      type: 'GET',
                      url: 'http://localhost:8080/property/propertyOfnode/'+ node +'/' +prop,
                      dataType : "json",
                      contentType:"application/json",
                      success: function(data){
                        var propTable = $('<table>').addClass('propTable');
                        $.each( data, function( key, val ) {
                            //console.log("stringify:",JSON.stringify(val)); //.replace("{\"value\":", "").replace(/\}}/g, "}"));
                      //      console.log("name",val.value);
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
           });
      });




// property nodes' relationships buttons onclick, CLASS .propNodeRelationships
      $(document).ready(function(){
      $("#result2").on("click", "button.propNodeRelationships", function(){
       console.log("propNodeRelationships running");
       $("#result").empty();
      var prop = $( this ).text().replace("property nodes' relationships", "").replace(/[^a-zA-Z ]/g, "");
                 $.ajax({
                      type: 'GET',
                      url: 'http://localhost:8080/property/noderelationships/' + prop,
                      dataType : "json",
                      contentType:"application/json",
                      success: function(data){
                          console.log(data);
                          var items = [];
                           $.each( data, function( key, val ) {
                             items.push( "<button id='" + JSON.stringify(key) + "'>" + JSON.stringify(val) + "</button>" );
                           });

                           $( "<div/>", {
                             "class": "my-new-list",
                             html: items.join( "" )
                           }).appendTo( "#result" );
                      }
                  });
          });
        });

// all instances button onclick TO FIX
       $(document).ready(function(){
           $("#allProp").click(function(){
           console.log("allProp running");
            $("#result").empty();
           var prop = $( "#listReturned option:selected" ).text();
                  $.ajax({
                       type: 'GET',
                       url: 'http://localhost:8080/property/allpropertiesof/' + prop,
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