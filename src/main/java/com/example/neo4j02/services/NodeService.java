package com.example.neo4j02.services;

import com.example.neo4j02.Neo4jSessionFactory;
import com.example.neo4j02.nodes.Book;
import com.example.neo4j02.repositories.NodeRepository;
import org.neo4j.ogm.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NodeService extends GenericService<Object> {

    @Autowired
    NodeRepository nodeRepository;

    @Override //delete it later
    Class<Object> getEntityType() {
        return null;
    }

    //------------------EXPLORE NODES---------------------------------------------------------------------------------
    //COUNT a node instances
    public Result getNodeCount(String node){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n:" + node + ") RETURN count(n) as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a node properties
    public Result getNodeProperties(String node){
        Map<String,Object> params = new HashMap<>();
        String query="match (n:" + node + ") \n" +
                " with keys(n) as nested\n" +
                "unwind nested as property\n" +
                " return distinct property as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a node relationships
    public Result getNodeRelationships(String node){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH ( a:"+ node + " )-[r]-(b) RETURN distinct type(r) as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return all nodes of the given label
    public Result getNodeAll(String node){
        Map<String,Object> params = new HashMap<>();
        System.out.println("getNodeAll function for node:  "+node);
        String query="MATCH (n:"+ node + ") RETURN n as value LIMIT 5";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a specific node relationships
    public Result getNodeRelationshipsId(String node,Long id){
        System.out.println("node/id: "+node+" / "+id);
        Map<String,Object> params = new HashMap<>();
        String query="MATCH p=( a:"+node+" )-[r]-(b) where ID(a)=" +id+ " RETURN  distinct type(r) as relType, labels(b) as theOtherNode , b as value"; //+ RETURN b NODE
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //------------------EXPLORE RELATIONSHIPS---------------------------------------------------------------------------------

    //COUNT a relationship insatnces
    public Result getRelCount(String rel){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH p=()-[r:" + rel + "]->() RETURN count(p)";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a relationship properties
    public Result getRelProperties(String rel){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n)-[r:" + rel + "]->(m) with keys(r) as nested\n" +
                "unwind nested as x\n" +
                "return distinct x as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a relationship properties VALUES
    public Result getRelPropertiesValues(String rel,String prop){
        System.out.println("rel parsed: "+rel +"prop parsed: "+prop);
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n)-[r:"+rel+"]->(m) WHERE exists(r."+prop+") return distinct r."+prop+", labels(startNode(r)) as start , type(r)  as r, labels(endNode(r)) as end ";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return nodes related to relationship
    public Result getInvolvedNodes(String rel){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (a)-[r:"+ rel +"]->(b) \n" +
                "return distinct labels(a) as a ,type(r) as r,labels(b) as b";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return all relationships of this type
    public Result getRelAll(String rel){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (a)-[r:"+rel+"]->(b) RETURN a, type(r), b LIMIT 4";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return all relationships of this type
    public Result getTwoNodesRel(String node1, String node2){
        Map<String,Object> params = new HashMap<>();
        String query="match (b:"+node1+"),(e:"+node2+")\n" +
                "match (b)-[r]-(e)\n" +
                "return distinct labels(startNode(r)) as starter_node,type(r),labels(endNode(r)) as endNode";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }



    //------------------EXPLORE PROPERTIES---------------------------------------------------------------------------------

    //COUNT a property total
    public Result getPropCount(String prop){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n) WHERE EXISTS(n." + prop + ") RETURN count(n) as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a property nodes
    public Result getPropNodes(String prop){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n) WHERE EXISTS(n." + prop + ") RETURN DISTINCT labels(n) as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a property nodes' relationships and the node they belong
    public Result getPropNodesRel(String prop){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n) WHERE EXISTS(n." + prop + ") with n\n" +
                "match (n)-[r]-(b)\n" +
                "return distinct labels(n) as node, type(r) as rel ";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a property all values with the node they belong
    public Result getPropAll(String prop){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n) WHERE EXISTS(n."+prop+") RETURN DISTINCT  n."+prop+"  ,labels(n) as entity LIMIT 4";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return all values of a property for selected node
    public Result getPropOfNode(String node, String prop){
        System.out.println("property of a node: "+prop+" "+node);
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n:"+node+") WHERE EXISTS(n."+prop+") RETURN DISTINCT  n."+prop+" as value LIMIT 4";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    public Result getVelueOfPropOfNode(String node, String prop,String value){
        System.out.println("value of property of a node: "+prop+" "+node+" "+value);
        Map<String,Object> params = new HashMap<>();
        String query;
             query="MATCH (n:" + node + ")  WHERE n." + prop + "  =~ '(?i).*" + value + ".*'\n" +
                     "return n as info";

        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    public Result getVelueOfProp(String prop,String value){
        System.out.println("value of property of a nodeLabel: "+prop+" "+value);
        Map<String,Object> params = new HashMap<>();
        String query;
        query="MATCH (n)  WHERE n." + prop + "  =~ '(?i).*" + value + ".*'\n" +
                "return n as info,labels(n) as Node label";

        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    public Result getNodesOfValue(String node1, String node2,String value){
        System.out.println("Returns all : "+node1+" nodes related to  "+node2+" node that matches the value: "+value);
        Map<String,Object> params = new HashMap<>();
        System.out.println("1: "+node1+" 2: "+node2);
        if (node1.equals("node1missing")){
            System.out.println("case with 1 node");
            String query = "MATCH (n:"+node2+")\n" +
                    "        unwind keys(n) as prop\n" +
                    "        with prop as p\n" +
                    "        match (n)\n" +
                    "        where n[p]=~ '(?i).*"+value+".*'\n" +
                    "        return distinct n as info";
            return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
        }else {
            System.out.println("case with 2 nodes");
            String query = "MATCH (n:"+node2+")\n" +
                    "        unwind keys(n) as prop\n" +
                    "        with prop as p\n" +
                    "        match (n)\n" +
                    "        where n[p]=~ '(?i).*"+value+".*'\n" +
                    "        with n as n1\n" +
                    "        match (n1)-[r]-(b:"+node1+")\n" +
                    "        return distinct b as info";
            return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
        }
    }

    //------------------EXPLORE RELATIONSHIP PROPERTIES---------------------------------------------------------------------------------

    //COUNT a property total
    public Result getRelPropCount(String prop){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH ()-[r]-() WHERE EXISTS(r." + prop + ") RETURN count(r) as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    //return a rel property relationship
    public Result getRelPropRel(String prop){
        System.out.println(prop);
        Map<String,Object> params = new HashMap<>();
        String query="MATCH ()-[r]-() WHERE EXISTS(r." + prop + ") RETURN distinct type(r) as value";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    public Result getRelPropOfRel(String rel,String prop){  //ex. WROTE location
        System.out.println("rel property of a relationship: "+rel+" "+prop);
        Map<String,Object> params = new HashMap<>();
        String query;
        query="MATCH (n)-[r:"+rel+"]->(m) WHERE EXISTS(r." + prop + ") RETURN DISTINCT r." + prop + " as value";

        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    public Result getVelueOfRelProp(String prop,String value){  //ex. location France
        System.out.println("value of rel property: "+prop+" "+value);
        Map<String,Object> params = new HashMap<>();
        String query;
        query="MATCH ()-[r]-()  WHERE r." + prop + "  =~ '(?i).*"+value+".*'"+
                "return distinct type(r) as relType , r." + prop + " as " + prop + "";

        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }

    public Result getVelueOfRelPropOfRel(String rel, String prop,String value){  //ex. WROTE location France
        System.out.println("value of rel property of a REL: "+prop+" " +prop+" " +value);
        Map<String,Object> params = new HashMap<>();
        String query;
        query="MATCH ()-[r:"+rel+" ]-()  WHERE r." + prop + "  =~ '(?i).*"+value+".*'"+
                "return distinct type(r) as relType , r." + prop + " as " + prop + "";

        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }



    //------------------EXPLORE SCHEMA ---------------------------------------------------------------------------------

    //return schema nodes and their relationships
    public Result getSchema() {
        Map<String, Object> params = new HashMap<>();
        String query = "match (n)-[r]->(b)\n" +
                "return distinct labels(n) as a ,type(r) as r,labels(b) as b";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
    }
    //return schema nodes and their relationships
    public Result getSchemaConstraints() {
        Map<String, Object> params = new HashMap<>();
        String query = "call db.constraints";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query, params);
    }





}
