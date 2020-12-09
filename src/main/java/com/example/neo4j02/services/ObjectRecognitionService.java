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
        String query =
                "MATCH (n) \n" +
                        "unwind keys(n) as nprop\n" +
                        "MATCH (n) WHERE n.nprop  =~ '(?i).*"+keywordInserted1+".*'\n" +
                        "MATCH (m)" +
                        "unwind keys(m) as mprop\n" +
                        "WHERE m.mprop  =~ '(?i).*"+keywordInserted2+".*'\n" +
                        "MATCH (n)-[r]-(m) return distinct labels(startNode(r)) as starter_node,type(r), n";
                       // else return both (OR) "RETURN labels(n) as NodeLabel, n as info";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);

//        MATCH (n) WHERE n.writer="Shakespeare"
//        MATCH (m) WHERE m.year=1603
//        match (n)-[r]-(m)
//        return r

    }

    //--------------------------------------------------------------------------------------------------------------------------

    //get keyword type OK
    public String getKeywordType(String keyword){
        System.out.println("getKeywordType method runs");
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
