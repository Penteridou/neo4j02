
//load rel properties buttons according to the clicked property and add classes
$(document).ready(function(){
  $("#result, #result4").on("click", "button.showrelproperties", function(){ //DELEGATION
  console.log("showrelproperties running");
   $("#result2").empty();
  $('#result2').parent().addClass("grid-item result2GridItem") ;
  $(this).siblings().removeClass("buttonpressed") ;
  $(this).addClass("buttonpressed") ;
  $("#theTable").empty();
  $( ".selectLabel" ).remove();
  $("<label>select: </label>" ).addClass("selectLabel").appendTo( $("#result2"));
    //store
   var prop =  $(this).text();
   localStorage.setItem("currentRelProperty", prop);
   console.log("current rel prop = ", prop);
      //alert("rel");
     // $("<button>" + $(this).text() + "property nodes' relationships</button>" ).addClass("propNodeRelationships").appendTo( $("#result2"));

      $("<button>" + prop + " relationship property involved relationships</button>" ).addClass("relpropNodes").appendTo( $("#result2"));
      if(localStorage.getItem("currentRelationshipType")!=""){
         $("<button>" + prop+ "relationship property of "+localStorage.getItem("currentRelationshipType")+" relationship</button>" ).addClass("relpropOfCurrentNode").appendTo( $("#result2"));
      }
      countRelProp(prop);
  });
});


  //count rel prop instances
  function countRelProp(prop){
     $.ajax({
          type: 'GET',
          url: 'http://localhost:8080/relproperty/count/' + prop,
          dataType : "json",
          contentType:"application/json",
          success: function(data){
              console.log(data);
              var val= JSON.stringify(data).replace("value\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');
              $("<label>There are " + val +" instances of relationship property "+prop+" </label>" ).addClass("counterLabel").attr("value", $(this).text()).appendTo( $("#result3"));
          }
      });

  }


// rel properties' involved nodes buttons onclick, CLASS .relpropNodes
   $(document).ready(function(){
      $("#result2").on("click", "button.relpropNodes", function(){
           $(this).siblings().removeClass("buttonpressed") ;
           $(this).addClass("buttonpressed") ;
           $("#result4").empty();
           $('#result4').parent().addClass("grid-item result4GridItem") ;
              $( ".selectLabel" ).remove();
              $("<label>select: </label>" ).addClass("selectLabel").appendTo( $("#result4"));
      console.log("propNodes running?");
   //    $("#result").empty();
      var prop = $( this ).text().replace(" relationship property involved relationships", "").replace(/[^a-zA-Z ]/g, "");
             $.ajax({
                  type: 'GET',
                  url: 'http://localhost:8080/relproperty/rel/'+ prop,
                  dataType : "json",
                  contentType:"application/json",
                  success: function(data){
                      console.log(data);
                          var items = [];
                           $.each( data, function( key, val ) {
                           console.log("val:",JSON.stringify(val));
                            var value = JSON.stringify(val).replace("value", "").replace(/[^a-zA-Z ]/g, "");
                             items.push( "<button class='showrelationshiptypes '>" + value + "</button>" );
                           });

                           $( "<div/>", {
                             "class": "my-new-list",
                             html: items.join( "" )
                           }).appendTo( "#result4" );
                  }
              });
      });
    });

// rel property values of current node  : SHOW TABLE WITH THE rel PROPERTY
      $(document).ready(function(){
          $("#result2").on("click", "button.relpropOfCurrentNode", function(){
           console.log("propOfCurrentNode running");
           $(this).siblings().removeClass("buttonpressed") ;
           $(this).addClass("buttonpressed") ;
                  $( ".selectLabel" ).remove();
                  $("<label>select: </label>" ).addClass("selectLabel").appendTo( $("#result4"));
              $("#result").empty();
              var node = localStorage.getItem("currentNode");
              var prop = localStorage.getItem("currentProperty");
              console.log("prop=",prop);
              console.log("node=", node);
                 $.ajax({
                      type: 'GET',
                      url: 'http://localhost:8080/relproperty/propertyOfrel/'+ rel +'/' +prop,
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
           $(this).siblings().removeClass("buttonpressed") ;
           $(this).addClass("buttonpressed") ;
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