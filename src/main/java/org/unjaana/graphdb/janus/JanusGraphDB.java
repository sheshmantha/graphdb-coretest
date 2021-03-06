package org.unjaana.graphdb.janus;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.janusgraph.core.*;
import org.janusgraph.core.schema.JanusGraphManagement;

import java.io.IOException;

/**
 * Created by shesh on 3/25/2018.
 */
public final class JanusGraphDB {
    private static final JanusGraphDB singleton = new JanusGraphDB();
    public static final String VL_MOVIE = "movie";
    private static final String VL_GENRE = "genre";
    public static final String VL_PERSON = "person";
    public static final String VL_USER = "user";
    private final JanusGraph graph;

    private JanusGraphDB() {
        graph = JanusGraphFactory.build()
                .set("storage.backend", "cassandra")
                .set("storage.keyspace", "test")
                .set("hostname", "127.0.0.1")
                .open();
        makeSchema(graph);
    }

    private void makeSchema(JanusGraph g) {
        /* Schema is derived from: https://academy.datastax.com/content/gremlin-traversals */
        JanusGraphManagement mgmt = graph.openManagement();
        if (mgmt.getVertexLabel("genre") == null) {
            // Property keys
            PropertyKey genreIdKey = mgmt.makePropertyKey("genreId").dataType(String.class).make();
            PropertyKey personIdKey = mgmt.makePropertyKey("personId").dataType(String.class).make();
            PropertyKey userIdKey = mgmt.makePropertyKey("userId").dataType(String.class).make();
            PropertyKey movieIdKey = mgmt.makePropertyKey("movieId").dataType(String.class).make();
            PropertyKey nameKey = mgmt.makePropertyKey("name").dataType(String.class).make();
            PropertyKey ageKey = mgmt.makePropertyKey("age").dataType(Integer.class).make();
            mgmt.makePropertyKey("gender").dataType(String.class).make();
            PropertyKey titleKey = mgmt.makePropertyKey("title").dataType(String.class).make();
            PropertyKey yearKey = mgmt.makePropertyKey("year").dataType(Integer.class).make();
            mgmt.makePropertyKey("duration").dataType(Integer.class).make();
            PropertyKey countryKey = mgmt.makePropertyKey("country").dataType(String.class).make();
            mgmt.makePropertyKey("production").dataType(String.class).cardinality(Cardinality.LIST).make();
            PropertyKey ratingKey = mgmt.makePropertyKey("rating").dataType(Integer.class).make();

// Vertex labels
            VertexLabel genre = mgmt.makeVertexLabel(VL_GENRE).make(); // .properties("genreId","name").make();
            VertexLabel person = mgmt.makeVertexLabel(VL_PERSON).make(); // .properties("personId","name").make();
            VertexLabel user = mgmt.makeVertexLabel(VL_USER).make(); // .properties("userId","age","gender").make();
            VertexLabel movie = mgmt.makeVertexLabel(VL_MOVIE).make(); // .properties("movieId","title","year","duration","country","production").make();

// Edge labels
            mgmt.makeEdgeLabel("knows").multiplicity(Multiplicity.MULTI).make(); // .connection("user","user").make();
            EdgeLabel ratedEdge = mgmt.makeEdgeLabel("rated").multiplicity(Multiplicity.MULTI).make(); // single().properties("rating").connection("user","movie").make();
            mgmt.makeEdgeLabel("belongsTo").multiplicity(Multiplicity.MULTI).make(); // .single().connection("movie","genre").make();
            mgmt.makeEdgeLabel("actor").multiplicity(Multiplicity.MULTI).make(); // .connection("movie","person").make();
            mgmt.makeEdgeLabel("director").multiplicity(Multiplicity.MULTI).make(); // .single().connection("movie","person").make();

// Vertex indexes
            mgmt.buildIndex("genreByIdIdx", Vertex.class).addKey(genreIdKey).indexOnly(genre).buildCompositeIndex();
            mgmt.buildIndex("genreByNameIdx", Vertex.class).addKey(nameKey).indexOnly(genre).buildCompositeIndex();
            mgmt.buildIndex("personByIdIdx", Vertex.class).addKey(personIdKey).indexOnly(person).buildCompositeIndex();
            mgmt.buildIndex("personByNameIdx", Vertex.class).addKey(nameKey).indexOnly(person).buildCompositeIndex();
            mgmt.buildIndex("userByIdIdx", Vertex.class).addKey(userIdKey).indexOnly(user).buildCompositeIndex();
            mgmt.buildIndex("userByAgeIdx", Vertex.class).addKey(ageKey).indexOnly(user).buildCompositeIndex();

            mgmt.buildIndex("moviesByIdIdx", Vertex.class).addKey(movieIdKey).indexOnly(movie).buildCompositeIndex();
            mgmt.buildIndex("moviesByTitleIdx", Vertex.class).addKey(titleKey).indexOnly(movie).buildCompositeIndex();
// Edge indexes
            mgmt.buildIndex("userRatingIdx", Edge.class).addKey(ratingKey).indexOnly(ratedEdge).buildCompositeIndex();
            mgmt.commit();
        }
        else
         mgmt.rollback();
    }

    public static final JanusGraph getInstanceGraph() {
        return singleton.graph;
    }
    public static final JanusGraphDB getSingleton() {return singleton; }
    public void loadData(String file) throws IOException {
        graph.io(IoCore.gryo()).readGraph(file);
    }

}
