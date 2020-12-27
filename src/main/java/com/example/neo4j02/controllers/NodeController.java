package com.example.neo4j02.controllers;

import com.example.neo4j02.nodes.Book;
import com.example.neo4j02.repositories.NodeRepository;
import com.example.neo4j02.services.NodeService;
import com.example.neo4j02.services.ObjectRecognitionService;
import com.example.neo4j02.services.SchemaImportService;
import org.neo4j.ogm.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class NodeController<T> { // GenericController

    private ObjectRecognitionService objectRecognitionService;
    private NodeService nodeService;

    SchemaImportService schemaImportService;
    @Autowired
    NodeRepository nodeRepository;


//    @Autowired
//    Neo4jRepository neo4jRepository;

    public NodeController(ObjectRecognitionService objectRecognitionService, NodeService nodeService, SchemaImportService schemaImportService, NodeRepository nodeRepository) {
        this.objectRecognitionService = objectRecognitionService;
        this.nodeService = nodeService;
        this.schemaImportService = schemaImportService;
        this.nodeRepository= nodeRepository;
    }


    //-------------------------------------------------------------------

    @GetMapping("/importschema")
    public String importschema() {
        schemaImportService.importSchema();
        return "Schema Imported";
    }
    //-----------------get node by Id-----------------------------------------------------------------------------------------

    @GetMapping("/getNodeByIdT/{id}")
    public Optional<Book> getNodeById(@PathVariable Long id) {
        return nodeRepository.findById(id);
    }



    //-------------------- EXPLORE SCHEMA----------------------------------------------------------------------------------------
    @GetMapping("/schema/constraints")
    public Object getPropAll(){return  nodeService.getSchemaConstraints(); }

    @GetMapping("/shownodes")
    public List<Object> getTheSchemaNodes(){ return (List<Object>) nodeRepository.getAllNodes();}

    @GetMapping("/showrelationshiptypes")
    public List<Object> getTheSchemaRelTypes(){ return (List<Object>) nodeRepository.showRelTypes();}

    @GetMapping("/showproperties")
     public List<Object> getTheSchemaProperties(){ return (List<Object>) nodeRepository.getAllProperties();}

    @GetMapping("/showschema")
     public Result getTheSchema(){ return  nodeService.getSchema();}

    //-------------------- SEARCH WITH KEYWORD----------------------------------------------------------------------------------------

    //the entire NODE(s) that includes the keyword
    @GetMapping("/search/{keyword}")
    public Object getObject(@PathVariable String keyword) {
        return objectRecognitionService.setParamsAndSearch(keyword); }

    @GetMapping("/search/{keyword}/{keyword2}")
    public Object getObject(@PathVariable String keyword, @PathVariable String keyword2) {
        return objectRecognitionService.setParamsAndSearch(keyword,keyword2); }

    //the node TYPE  that includes the keyword
    @GetMapping(value = "/searchfortype/{keyword}") //,produces = MediaType.APPLICATION_JSON_VALUE
    public String getKeywordType(@PathVariable String keyword){
        return objectRecognitionService.getKeywordType(keyword); }


    //node and property of the keyword
    @GetMapping("/searchentity/{keyword}")
    public Object getObjectInfo(@PathVariable String keyword){
        return  objectRecognitionService.setParamsAndSearchPropertyEntity(keyword); }

    //relationships of the keyword
    @GetMapping("/searchforrel/{keyword}")
    public Object getObjectRel(@PathVariable String keyword){ return  objectRecognitionService.setParamsAndSearchRel(keyword); }


    //-------------------- EXPLORE NODES------------------------------------------------------------------------------------------------

    //count nodes
    @GetMapping("/node/count/{node}")
    public Object getNodeCount(@PathVariable String node){
        return  nodeService.getNodeCount(node);
    }

    //get node properties
    @GetMapping("/node/properties/{node}")
    public Object getNodeProperties(@PathVariable String node){ return  nodeService.getNodeProperties(node); }

    //get node relationships
    @GetMapping("/node/relationships/{node}")
    public Object getNodeRelationships(@PathVariable String node){
        return  nodeService.getNodeRelationships(node);
    }

    //get all nodes of the given label
    @GetMapping("/node/allnodesof/{node}")
    public Object getNodeObjects(@PathVariable String node){
        return  nodeService.getNodeAll(node);
    }

    //get specific node relationships
    @GetMapping("/node/relationships/{node}/{id}") //reltype instead of id?
    public Result getNodeRelationshipsById(@PathVariable String node,@PathVariable Long id){
        return  nodeService.getNodeRelationshipsId(node,id);
    }

    //return rel between two nodes
    @GetMapping("/relationships/{node1}/{node2}")
    public Object getTwoNodesRel(@PathVariable String node1, @PathVariable String node2){return nodeService.getTwoNodesRel(node1,node2);
    }

    @GetMapping("/nodes/value/{node1}/{node2}/{value}")
    public Object getNodesOfValue(@PathVariable String node1,@PathVariable String node2,@PathVariable String value){return  nodeService.getNodesOfValue(node1,node2,value); }

    //-------------------- EXPLORE RELATIONSHIPS----------------------------------------------------------------------------------------

    //count relationships
    @GetMapping("/relationship/count/{rel}")
    public Object getRelCount(@PathVariable String rel){
        return  nodeService.getRelCount(rel);
    }

    //get relationship properties
    @GetMapping("/relationship/properties/{rel}")
    public Object getRelProperties(@PathVariable String rel){
        return  nodeService.getRelProperties(rel);
    }

    //get relationship properties values
    @GetMapping("/relationship/propertiesvalues/{rel}/{prop}")
    public Object getRelPropertiesValues(@PathVariable String rel,@PathVariable String prop){
        return  nodeService.getRelPropertiesValues(rel,prop);
    }

    //return nodes related to relationship
    @GetMapping("/relationship/allrelationshipsof/{rel}")
    public Object getRelAll(@PathVariable String rel){return  nodeService.getRelAll(rel);}

    //return all relationships of this type
    @GetMapping("/relationship/nodesinvolved/{rel}")
    public Object getInvolvedNodes(@PathVariable String rel){return  nodeService.getInvolvedNodes(rel); }

    //-------------------- EXPLORE PROPERTIES----------------------------------------------------------------------------------------

    //count properties
    @GetMapping("/property/count/{prop}")
    public Object getPropCount(@PathVariable String prop){return  nodeService.getPropCount(prop); }

    //return a property nodes
    @GetMapping("/property/nodes/{prop}")
    public Object getPropNodes(@PathVariable String prop){return  nodeService.getPropNodes(prop); }

    @GetMapping("/property/noderelationships/{prop}")
    public Object getPropNodesRel(@PathVariable String prop){return  nodeService.getPropNodesRel(prop); }

    @GetMapping("/property/allpropertiesof/{prop}")
    public Object getPropAll(@PathVariable String prop){return  nodeService.getPropAll(prop); }

    @GetMapping("/property/propertyOfnode/{node}/{prop}")
    public Object getPropOfNode(@PathVariable String prop,@PathVariable String node){return  nodeService.getPropOfNode(node,prop); }

    @GetMapping("/property/propertyOfnode/{node}/{prop}/{value}")
    public Object getPropOfNode(@PathVariable String prop,@PathVariable String node,@PathVariable String value){return  nodeService.getPropOfNode(node,prop,value); }

}

