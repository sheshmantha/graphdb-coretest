package com.philips.graphdb.janus;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.VertexProperty;
import org.janusgraph.core.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.property;
import static org.junit.Assert.*;

/**
 * Created by shesh on 3/25/2018.
 */
public class JanusGraphDBTest {
    private Graph graph;

    @Before
    public void setUp() throws Exception {
        graph = JanusGraphDB.getInstance();
    }

    @Test
    public void addSingleNodeTest() throws Exception {
        Vertex v1 = graph.addVertex(T.label, JanusGraphDB.VL_MOVIE,
                "movieId", 11,
                "title", "Midsummer",
                "year", "1962",
                "country", "UK",
                "production", "Spyglass");
        Vertex v = graph.traversal().V().hasLabel(JanusGraphDB.VL_MOVIE).next();
        assertEquals("Retrieved MovieID", "11", v.property("movieId").value());
        getMoviesTest();
    }

    @Test
    public void addSingleNodeViaTraversalTest() throws Exception {
        GraphTraversalSource g = graph.traversal();
        Vertex v1 = g.V().addV(JanusGraphDB.VL_MOVIE)
                .property("movieId", 12)
                .property("title", "HelloWorld")
                .property("year", 1990)
                .property("country", "US")
                .property("production", "C")
                .next();
        g.tx().commit();
        Vertex v2 = g.V(v1.id()).next();
        assertEquals("Retrieved node inserted via traveral", "12", v2.property("movieId").value());
        getMoviesTest();
    }

    public void getMoviesTest() throws Exception {
        GraphTraversalSource g = graph.traversal();
        List<Vertex> nodes = g.V().toList();
        for (Vertex node: nodes) {
            System.out.println("vertexId:" + node.id()
                    + " movieId: " + node.property("movieId").value()
                    + " Movie: " + node.property("title").value());
        }
    }

//    @Test
    public void loadData() throws Exception {

    }

}