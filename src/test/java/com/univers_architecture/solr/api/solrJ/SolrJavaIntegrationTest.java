package com.univers_architecture.solr.api.solrJ;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.junit.Before;
import org.junit.Test;

import com.univers_architecture.solr.api.solrJ.model.Film;

public class SolrJavaIntegrationTest {

    private SolrJavaIntegration solrJavaIntegration;

    @Before
    public void setUp() throws Exception {

        solrJavaIntegration = new SolrJavaIntegration("http://localhost:8983/solr/films");
        String[] djangoCategory = {"drama","action","thriller"};
        solrJavaIntegration.addSolrDocument("123456", "Django unchained", "quentin tarantino", "2017-10-15", djangoCategory);
    }

    @Test
    public void whenAdd_thenVerifyAddedByQueryOnId() throws SolrServerException, IOException {

        SolrQuery query = new SolrQuery();
        query.set("q", "id:123456");
        QueryResponse response = null;

        response = solrJavaIntegration.getSolrClient().query(query);

        SolrDocumentList docList = response.getResults();
        assertEquals(1, docList.getNumFound());

        for (SolrDocument doc : docList) {
            assertEquals("Django unchained", (String) doc.getFieldValue("name"));
            assertEquals("quentin tarantino", (String) doc.getFieldValue("directed_by"));
        }
    }

    @Test
    public void whenAdd_thenVerifyAddedByQueryOnName() throws SolrServerException, IOException {

        SolrQuery query = new SolrQuery();
        query.set("q", "name:Django unchained");
        QueryResponse response = null;

        response = solrJavaIntegration.getSolrClient().query(query);

        SolrDocumentList docList = response.getResults();
        assertEquals(1, docList.getNumFound());

        for (SolrDocument doc : docList) {
            assertEquals("123456", (String) doc.getFieldValue("id"));
            assertEquals("quentin tarantino", (String) doc.getFieldValue("directed_by"));
        }
    }

    @Test
    public void whenAdd_thenVerifyAddedByQuery() throws SolrServerException, IOException {

        SolrDocument doc = solrJavaIntegration.getSolrClient().getById("123456");
        assertEquals("Django unchained", (String) doc.getFieldValue("name"));
        assertEquals("quentin tarantino", (String) doc.getFieldValue("directed_by"));
    }

    @Test
    public void whenAddBean_thenVerifyAddedByQuery() throws SolrServerException, IOException {
        String[] ITCategory = {"horror", "mystery", "drama"};
        Film film = new Film("555", "IT", "steven spielberg", ITCategory );
        solrJavaIntegration.addProductBean(film);

        SolrDocument doc = solrJavaIntegration.getSolrClient().getById("555");
        assertEquals("steven spielberg", (String) doc.getFieldValue("directed_by"));
        assertEquals("IT", (String) doc.getFieldValue("name"));
    }

    @Test
    public void whenDeleteById_thenVerifyDeleted() throws SolrServerException, IOException {

        solrJavaIntegration.deleteSolrDocumentById("123456");

        SolrQuery query = new SolrQuery();
        query.set("q", "id:123456");
        QueryResponse response = solrJavaIntegration.getSolrClient().query(query);

        SolrDocumentList docList = response.getResults();
        assertEquals(0, docList.getNumFound());
    }

    @Test
    public void whenDeleteByQuery_thenVerifyDeleted() throws SolrServerException, IOException {

        solrJavaIntegration.deleteSolrDocumentByQuery("name:Django unchained");

        SolrQuery query = new SolrQuery();
        query.set("q", "id:123456");
        QueryResponse response = null;

        response = solrJavaIntegration.getSolrClient().query(query);

        SolrDocumentList docList = response.getResults();
        assertEquals(0, docList.getNumFound());
    }
}
