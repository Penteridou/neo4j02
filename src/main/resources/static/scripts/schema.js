////load  nodes & relationships
//$(document).ready(function(){
//    $.ajax({
//        type: 'GET',
//        url: 'http://localhost:8080/showschema',
//        dataType : "json",
//        contentType:"application/json",
//        success: function(data){
//            console.log(data);
//                    for (let d in data) {
//                        $("#result").append('<ul>' + JSON.stringify(data[d]) + ' </ul>' );
//                    }
//        }
//    });
//});

// check constraints
 $(document).ready(function(){
    $("#constraints").click(function(){
     $("#result").empty();
    var prop = $( "#listReturned option:selected" ).text();
           $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/schema/constraints',
                dataType : "json",
                contentType:"application/json",
                success: function(data){
                    console.log(data);
                           // alert(  JSON.stringify(data)  );
                            $("#result").append("<span id='" + JSON.stringify(data) + "'>" + JSON.stringify(data) + "</span>"  )
                }
            });
    });
  });

// SCHEMA VISUALIZATION onclick NOT DONE YET !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
   $(document).ready(function(){
      $("#visual").click(function(){
       $("#result").empty();
      var prop = $( "#listReturned option:selected" ).text();
             $.ajax({
                  type: 'GET',
                  url: 'http://localhost:8080/schema/visual',
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