package com.example.neo4j02.services;

import com.example.neo4j02.Neo4jSessionFactory;
import com.example.neo4j02.repositories.NodeRepository;


//import org.neo4j.driver.Session;

import org.neo4j.ogm.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ObjectRecognitionService extends GenericService<Object> {

    @Autowired
    NodeRepository nodeRepository;


    private String keywordInserted;
    private ArrayList<String> nodeList = new ArrayList<String>();


    public ObjectRecognitionService() {
    }

// THE SEARCH FUNCTION----------------------------------------------------------------------------------------------------------------------
    //entire object(s) N depending on the keyword
    public Result setParamsAndSearch(String keywordInserted){
            //ckeck if keywordInserted is any property title
        System.out.println("keywordInserted: " +keywordInserted);
        keywordInserted = keywordInserted.replaceAll("^\'|\'$", "");
        if(checkIfKeywordIsAproperty(keywordInserted)){//set params
            System.out.println("the keyword is property");
        Map<String,Object> params = new HashMap<>();
        params.put( "props",getAllPropertiesListed()); // current properties
        String query = "unwind $props as prop\n" +
                "MATCH (n) WHERE prop  =~ '(?i).*"+keywordInserted+".*' \n" +
                "RETURN  distinct prop as showproperties";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);

            //ckeck if keywordInserted is any node title
        }else if (checkIfKeywordIsAnode(keywordInserted)){
            System.out.println("the keyword is node");
            Map<String,Object> params = new HashMap<>();
            params.put( "nodes",getAllNodesListed()); // current node labels
            String query =
                    "MATCH (n) \n" +
                    "unwind  $nodes as node\n" +
                    "match (n) where node=~ '(?i).*"+keywordInserted+".*' \n" +
                    "RETURN  distinct node as shownodes ";
            return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
        //check if keywordInserted is any relationship type title
        }else if (checkIfKeywordIsArel(keywordInserted)){
            System.out.println("the keyword is Rel");
            Map<String,Object> params = new HashMap<>();
            params.put( "rel",getAllRelTypesListed()); // current rel types
            String query =
                    "MATCH (a)-[r]-(b) \n" +
                    "WHERE type(r)=~ '(?i).*"+keywordInserted+"*'\n" +
                    "RETURN distinct type(r) as showrelationshiptypes ";
            return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);

        }else {  //if keyword matches a property value
            System.out.println("the keyword is property value");
            Map<String,Object> params = new HashMap<>();
            params.put("props",getAllPropertiesListed()); // current properties
            String query =
                    "MATCH (n) \n" +
                            "unwind keys(n) as prop\n" +
                            "MATCH (n) WHERE n[prop]  =~ '(?i).*"+keywordInserted+".*'\n" +
                            "RETURN labels(n) as NodeLabel, n as info";
            return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);

        }// else System.out.println("fail  " +getAllRelTypesListed().toString()); return null;


    }

    public Result setParamsAndSearch(String keywordInserted1,String keywordInserted2){
        System.out.println("the keywords are property value");
        System.out.println(keywordInserted1+" "+keywordInserted2);
        Map<String,Object> params = new HashMap<>();
        params.put("props",getAllPropertiesListed()); // current properties
        String query ="MATCH (n) \n" +
                "unwind keys(n) as prop\n" +
                "with prop as p\n" +
                "match (n)\n" +
                "WHERE n[p]  =~ '(?i).*"+keywordInserted1+".*'\n" +
                "with n as n1,p as p1\n" +
                "MATCH (n) \n" +
                "unwind keys(n) as prop\n" +
                "with prop as p,n1,p1\n" +
                "match (n)\n" +
                " WHERE n[p]  =~ '(?i).*"+keywordInserted2+".*'\n" +
                "match (n1)-[r]-(n)\n" +
                "return distinct\n type(r)," +
                "case \n" +
                "\twhen startNode(r)[p1] is null then startNode(r)[p]\n" +
                "    else startNode(r)[p1] \n" +
                " end    as starter_node,\n" +
                "  case \n" +
                "\twhen endNode(r)[p1] is null then endNode(r)[p]\n" +
                "    else endNode(r)[p1] \n" +
                " end    as end_node";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);

//        MATCH (n)
//        unwind keys(n) as prop
//        with prop as p
//        match (n)
//        where n[p]="Shakespeare"
//        with n as n1
//        MATCH (n)
//        unwind keys(n) as prop
//        with prop as p,n1
//        match (n)
//        where n[p]="HamletPrinceofDenmark"
//        match (n1)-[r]-(n)
//        return distinct
//case
//	when startNode(r)[p1] is null then startNode(r)[p]
//    else startNode(r)[p1]
// end    as starter_node,
//   type(r),
//  case
//	when endNode(r)[p1] is null then endNode(r)[p]
//    else endNode(r)[p1]
// end    as end_node

    }

    //--------------------------------------------------------------------------------------------------------------------------

    //get keyword type OK
    public String getKeywordType(String keyword){
        System.out.println("getKeywordType method runs, keyword is "+ keyword);
        String type ="";
        if(getAllPropertiesListed().contains(keyword)){
            type= "showproperties";
        }else if(checkIfKeywordIsAnode(keyword)){
            type= "shownodes";
        }else if(checkIfKeywordIsArel(keyword)){
            type= "showrelationshiptypes";
        }else
            type= "NodeLabel";
        System.out.println("the type is:" +type);
        return type;
    }

   public boolean checkIfKeywordIsAproperty(String keyword){
       if(getAllPropertiesListed().contains(keyword))
           return true;
       return false;
   }

    public boolean checkIfKeywordIsAnode(String keyword){
        if(getAllNodesListed().contains(keyword.substring(0, 1).toUpperCase() + keyword.substring(1).toLowerCase()))
            return true;
        return false;
    }

    public boolean checkIfKeywordIsArel(String keyword){
        if(getAllRelTypesListed().contains(keyword.toUpperCase()))
            return true;
        return false;
    }



    //object Label
    public Result setParamsAndSearchNameEntity(String keywordInserted){
        Map<String,Object> params = new HashMap<>();
        params.put( "props",getAllPropertiesListed()); // current properties
        String query = "MATCH (n) \n" +
                "unwind $props as prop\n" +
                "MATCH (n) WHERE n[prop]  =~ '(?i).*"+keywordInserted+".*' \n" +
                "RETURN labels(n) as NodeEntityTitle";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
    }

    //relationships of a keyword
    public Result setParamsAndSearchRel(String keywordInserted){
        Map<String,Object> params = new HashMap<>();
        params.put( "props",getAllPropertiesListed()); // current properties
        String query = "MATCH (n)-[r]-(m)\n" +
                "unwind {props} as prop\n" +
                "MATCH (n) WHERE n[prop]  =~ '(?i).*theh.*' \n" +
                "RETURN distinct type(r) as type_of_rel, count(type(r))  as count order by count(type(r)) desc;";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
    }

    // retrieve all properties from repository
    public Collection<Object>  getAllPropertiesListed(){return nodeRepository.getAllProperties(); }
    // retrieve all classes from repository
    public Collection<Object>  getAllNodesListed(){
        return nodeRepository.getAllNodes();
    }
    // retrieve all relationship types from repository
    public  Collection<Object> getAllRelTypesListed(){return nodeRepository.showRelTypes(); }


    //set properties as parameters and search based on them
    public Result setParamsAndSearchPropertyEntity(String keywordInserted) {

        Map<String,Object> params = new HashMap<>();
        params.put( "props",getAllPropertiesListed()); // current properties
        String query = "optional MATCH (n) \n" +
                "unwind $props as prop\n" +
                "MATCH (n) WHERE n[prop]  =~ '(?i).*"+keywordInserted+".*' \n" +
                "RETURN prop as property, labels(n) as entity, n as node_details\n";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
    }

    public void getNodes(){nodeRepository.getAllNodes();};

    @Override
    Class<Object> getEntityType() {
        return Object.class;
    }



}
