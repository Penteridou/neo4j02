//UPDATED 17/5



  $(document).ready(function(){
    localStorage.setItem("currentNode", '');
    localStorage.setItem("currentProperty", '');

  });

$(document).ready(function(){
   $("#draw").click(function(){
       window.open("viz.html");
    });
});

$(document).ready(function(){
   $("#refresh").click(function(){
      location.reload();
    });
});

$(document).ready(function(){
   $("#import").click(function(){
          $.ajax({
              type: 'GET',
              url: 'http://localhost:8080/importschema',
              dataType : "json",
              contentType:"application/json",
              success: function(data){
                    alert("imported");
              }
          });
    });
});

// OPTION LIST -------------------------------------------------------------------------------------------
  //load data as buttons according to select option and define classes to buttons (addClass method)
   $(document).ready(function(){
      $( "#mainList" ).change(function() {
          $("#result").empty();
           $("#result2").empty();
           $("#result3").empty();
           localStorage.setItem("currentNode", '');
           localStorage.setItem("currentProperty", '');
           localStorage.setItem("currentRelationshipType", '');
          var option = $( "#mainList option:selected" ).val(); //option values: shownodes, showrelationshiptypes, showproperties, showschema
                $.ajax({
                    type: 'GET',
                    url: 'http://localhost:8080/' + option,
                    dataType : "json",
                    contentType:"application/json",
                    success: function(data){
                        console.log(data);
                                for (let nodeTitle in data) {
                                   $("<button>" + data[nodeTitle] + " </button>" ).addClass(option).appendTo( $("#result"));
                                }
                    }
                });
      });
   });


//SEARCH BUTTON---------------------------------------------------------------------------------------------------------
// GIA AUTOCOMPLETE CHECK https://www.w3schools.com/howto/howto_js_autocomplete.asp !!!!!!!!!!!!!!
$(document).ready(function(){
   $("#btn").click(function(){
        var keyword = $("#keywordinput").val();
        wordsCounter = countWords(keyword);           // keywords inserted
        console.log("wordsCounter ",wordsCounter);
        $("#result").empty();
        $("#result2").empty();
        $('#mainList').val('none');
        localStorage.setItem("currentNode", '');
        localStorage.setItem("currentProperty", '');

        if (keyword==''){
                alert("Please insert any keyword!")
         }else if(wordsCounter==1) {
            searchWithOneKeyword(keyword);             //call the corresponding function
        } else if(wordsCounter==2) {                     //call the function for MORE KEYWORDS INSERTED   <
                 var counter = 0;
                console.log("more than one keywords inserted: ", wordsCounter, keyword)
               var keywordsArray = keywordsToArray(keyword);
               var keywordTypesArray=[];

               $.each(keywordsArray, function setType( index, value  ) {
                counter++;
                   console.log("keywordsArray[index]?",keywordsArray[index],index);
                   if( checkIfKeywordIsACategory(keywordsArray[index])){                                                //if keyword is category
                         if(defineKeywordCategory(keywordsArray[index])=="showproperties"){
                            keywordTypesArray[index]="properties";
                         }else if(defineKeywordCategory(keywordsArray[index])=="showrelationshiptypes"){
                            keywordTypesArray[index]="relTypes";
                         }else if(defineKeywordCategory(keywordsArray[index])=="shownodes"){
                            keywordTypesArray[index]="nodes";
                         }


                   }else { console.log("search for type with: ", keywordsArray[index] );

                             return  $.ajax({
                                    type: 'GET',
                                    url: 'http://localhost:8080/searchfortype/' + keywordsArray[index],
                                    dataType : "text",
                                    contentType:"application/json",
                                    success: function(data){
                                        console.log("does it work????-->", data);
                                        keywordTypesArray[index]=data;
                                        console.log("keywordTypesArray[index] took any value?",keywordTypesArray[index] );
                                    }
                                });
                    }

               });

               $(document).ajaxStop(function () {
                       console.log("type array with delay???: ", keywordTypesArray );
                       if(counter==2){
                         //   alert("counter ", counter)
                            keywordHandling(keywordTypesArray, keywordsArray); //call the corresponding function to handle the keywords
                            counter=0;
                      }
               });
          }

    }); //ok
}); //ok




//---------------------KEYWORD INPUT FUNCTIONS--------------------------------------------------

//only one keyword inserted
function searchWithOneKeyword(keyword){
   console.log("only one keyword inserted: ",keyword);
    //check if keyword matches any category
    if(checkIfKeywordIsACategory(keyword)){
                keywordIsACategory(keyword); //call the corresponding function
     }else{  console.log("else search for the rest values");
            $.ajax({
                  type: 'GET',
                  url: 'http://localhost:8080/search/' + keyword,
                  dataType : "json",
                  contentType:"application/json",
                  success: function(data){
                      console.log(data);
                      var items = [];
                      var table = $('<table>').addClass('resultTable');
                       $.each( data, function( key, val ) { //check the value title from the query result to figure out what it is , example: ("shownodes":"Book")
                          var value = JSON.stringify(val);// .replace("value", "").replace(/[&\/\\#,+()$~%.'":*?<>{}]/g, '');
                          console.log("the initial value is: "+value);
                          var resultType = value.split(/"(.+)/)[1].split(/"(.+)/)[0]; //resultType value: shownodes||showproperties||showrelationshiptypes||NodeLabel
                          //check result type in order to proceed
                          console.log("result type is: "+resultType);
                          if(resultType=="NodeLabel") { // the keyword is a property value (NodeValue is returned when the property value query runs)
                            console.log("NodeLabel details ")
                                var items = [];
                                var value = JSON.stringify(val).replace("info\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');
                                var arr = value.split(',');
                                console.log("check json: ", arr);
                                for (let i = 0; i < arr.length; ++i) {
                                     if(!arr[i].endsWith("null")){
                                        items.push( "<td>" + arr[i] + "</td>" );
                                     }
                                }
                               // var btn= $( "<button>check relationships</button>").addClass("checkRelBtn").attr("id",val.value.id);
                                var row = $('<tr>').addClass('bar').append(items.join("")); //.append(btn);
                                //console.log("btn id is:", val.value.id);
                                table.append(row);

                          }else{
                                //here
                            $("<button>" + value.split(/"(.+)/)[1].split(/"(.+)/)[1].split(/"(.+)/)[1].split(/"(.+)/)[0]+ " </button>" ).addClass(resultType).appendTo( $("#result"));
                           }

                         if(resultType=="NodeLabel"){
                         $(table).appendTo($( "<div style='overflow-x:auto'>Table</div>" ).insertAfter( ".box2" ));
                         }else{ table = undefined;  console.log("table deleted");}

                      });
                  } //success function ok
            });
        }
}

//return true if the keyword is any category: node, property, relationship or schema
function checkIfKeywordIsACategory(keyword){
   if(keyword.includes("node")||keyword.includes("propert")||keyword.includes("relationship")||keyword.includes("schema"))
        return true;
   else false;
}

function defineKeywordCategory(keyword){

 if(keyword.includes("node")){
       option="shownodes";
   }else if(keyword.includes("relationsh")){
       option="showrelationshiptypes";
   }else if(keyword.includes("proper")){
       option="showproperties";
   }else if(keyword.includes("schem")){
       option="showschema";
   }
  return option;

}


//category keyword inserted
function keywordIsACategory(keyword){
  console.log("keywordIsACategory function runs");
    option= defineKeywordCategory(keyword);
            $.ajax({
                type: 'GET',
                url: 'http://localhost:8080/' +option,
                dataType : "json",
                contentType:"application/json",
                success: function(data){

                console.log(data);
                    if(option!="showschema"){
                        for (let nodeTitle in data) {$("<button>" + data[nodeTitle] + " </button>" ).addClass(option).appendTo( $("#result"));}
                    }else{

                               var schemaTable = $('<table>').addClass('schemaTable');
                                $.each( data, function( key, val ) {
                                    var items = [];
                                //get the first label from relationship json file
                                 var labelA = JSON.stringify(val).substr(0, JSON.stringify(val).indexOf(']')).split("[").pop();;
                                //get the second label from relationship json file
                                 var labelB = JSON.stringify(val).split("[").pop().substr(0, JSON.stringify(val).split("[").pop().indexOf(']'));
                                 var rel = val.r;
                                  alert(labelA,rel,labelB);
                                  items.push("<button class='shownodes'>" + labelA + "</button>" );
                                  items.push( "<label>-</label>" );
                                  items.push( "<button class='showrelationshiptypes'>" + rel + "</button>" );
                                  items.push( "<label>-></label>" );
                                  items.push( "<button class='shownodes'>" + labelB + "</button>" );

                                   var row = $('<tr>').append(items.join(""));
                                   schemaTable.append(row);
                                });

                               $(schemaTable).appendTo( "#result" );
                    }




                }
            });
}

function keywordHandling(arrayTypes,arrayValues){
    console.log("inside the handling: ", arrayTypes, arrayValues);
    if( (arrayTypes[0]=="shownodes")&&(arrayTypes[1]=="showproperties")  ){ //ex. Book price
               var node = arrayValues[0];
               var prop = arrayValues[1];
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
    }else if( (arrayTypes[0]=="shownodes")&&(arrayTypes[1]=="showrelationshiptypes")   ){ //ex. Book WRITED
       var rel = arrayValues[1];
         ajaxForNodesRel(rel);
    }else if( (arrayTypes[0]=="shownodes")&&(arrayTypes[1]=="shownodes")   ){ //ex. Book Author
           var node1 = arrayValues[0];
           var node2 = arrayValues[1]

         $.ajax({
              type: 'GET',
              url: 'http://localhost:8080/relationships/' + node1+ '/'+ node2 ,
              dataType : "json",
              contentType:"application/json",
              success: function(data){
                var table = $('<table>').addClass('resultTable');
                $.each( data, function( key, val ) {
                    //console.log("stringify:",JSON.stringify(val)); //.replace("{\"value\":", "").replace(/\}}/g, "}"));
                   // console.log("name",val.value.name);
                    var items = [];
                    var value = JSON.stringify(val).replace("type(r)\":", "").replace(/[&\/\\#+()$~%'"*?<>{}]/g, '');
                    var arr = value.split(',');
                    console.log("check json: ", arr);
                    for (let i = 0; i < arr.length; ++i) {
                        //  alert(arr[i]);
                        items.push( "<td>" + arr[i] + "</td>" );
                    }

                    var row = $('<tr>').addClass('bar').append(items.join(""));
                    //console.log("btn id is:", val.value.id);
                    table.append(row);
                    //  items.push( "<label>next-----------</label>" );

                });
                $(table).appendTo($('#theTable2'));//.attr("id",'theTable').insertAfter( ".box2" ));

              }
          });


    }else if( (arrayTypes[0]=="shownodes")&&(arrayTypes[1]=="nodes")   ){ //Book nodes
        ajaxAllnodesOf(arrayValues[0]);
        console.log("ajaxAllnodesOf function");
    }else if( (arrayTypes[0]=="shownodes")&&(arrayTypes[1]=="properties")   ){ //Book properties
         ajaxAllpropertiesOf(arrayValues[0]);
         console.log("ajaxAllpropertiesOf function");
    }else if( (arrayTypes[0]=="shownodes")&&(arrayTypes[1]=="relTypes")   ){ //Book relationships
         ajaxAllrelTypesOf(arrayValues[0]);
         console.log("ajaxAllrelTypesOf function");
    }
//>>>>>>>>>>>>> CONTINUE WITH ELSE IF FOR more cases!!

}



//words count in input #keywordinput
function countWords(keyword){
 return  (keyword.replace(/(^\s*)|(\s*$)/gi,"").replace(/[ ]{2,}/gi," ").replace(/\n /,"\n").split(' ').length);
}

function keywordsToArray(keyword){
    var wordsArr= [];
    for(i=0; i< (keyword.replace(/(^\s*)|(\s*$)/gi,"").replace(/[ ]{2,}/gi," ").replace(/\n /,"\n").split(' ').length) ; i++){
        wordsArr[i]=keyword.replace(/(^\s*)|(\s*$)/gi,"").replace(/[ ]{2,}/gi," ").replace(/\n /,"\n").split(' ')[i];
    }
    console.log("words are: ", wordsArr);
    return wordsArr;
}

