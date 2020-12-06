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
        String query="MATCH p=( a:"+node+" )-[r]-(b) where ID(a)=" +id+ " RETURN  distinct type(r) as relType, labels(b) as theOtherNode , b as details"; //+ RETURN b NODE
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
        System.out.println("rel parsed: "+rel);
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n)-[r:RATED]->(m) RETURN distinct r."+prop+"";
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
                "return distinct labels(startNode(r)) as starter_node,type(r)";
        return Neo4jSessionFactory.getInstance().getNeo4jSession().query(query,params);
    }



    //------------------EXPLORE PROPERTIES---------------------------------------------------------------------------------

    //COUNT a property total
    public Result getPropCount(String prop){
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n) WHERE EXISTS(n." + prop + ") RETURN count(n)";
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

    public Result getPropOfNode(String node, String prop,String value){
        System.out.println("value of property of a node: "+prop+" "+node+" "+value);
        Map<String,Object> params = new HashMap<>();
        String query="MATCH (n:"+node+")  WHERE n."+prop+"  =~ '(?i).*"+value+".*'\n" +
                "return n as info";
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
