package com.philips.graphdb.janus;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.*;
import org.junit.Before;
import org.junit.Test;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.property;
import static org.junit.Assert.*;

/**
 * Created by shesh on 3/25/2018.
 */
public class JanusGraphDBTest {
    private JanusGraph graph;

    @Before
    public void setUp() throws Exception {
        graph = JanusGraphDB.getInstance();
    }

    @Test
    public void addSingleNodeTest() throws Exception {
        Vertex v1 = graph.addVertex(JanusGraphDB.VL_MOVIE);
        v1.property("movieId", 11,
                "title", "Midsummer",
                "year", 1962,
                "country", "UK",
                "production", "Spyglass");
        Vertex v = graph.traversal().V().hasLabel(JanusGraphDB.VL_MOVIE).next();
        assertEquals("Retrieved MovieID", "11", v.property("movieId").value());
    }

    @Test
    public void loadData() throws Exception {

    }

}