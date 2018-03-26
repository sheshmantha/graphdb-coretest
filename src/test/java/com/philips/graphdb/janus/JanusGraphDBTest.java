package com.philips.graphdb.janus;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by shesh on 3/25/2018.
 */
public class JanusGraphDBTest {
    @Test
    public void getInstance() throws Exception {
        JanusGraph graph = JanusGraphDB.getInstance();
//        JanusGraphTransaction tx = graph.newTransaction();
        GraphTraversalSource g = graph.traversal();
        g.addV(JanusGraphDB.VL_MOVIE).property("movieId", 1,
                "title", "Midsummer",
                "year", 1962,
                "country", "UK",
                "production", "Spyglass");
        g.tx().commit();
//        tx = graph.newTransaction();
        Vertex v = g.V().hasLabel(JanusGraphDB.VL_MOVIE).next();
        assertEquals("Retrieved MovieID", 1, v.property("movieId"));
    }

    @Test
    public void loadData() throws Exception {

    }

}