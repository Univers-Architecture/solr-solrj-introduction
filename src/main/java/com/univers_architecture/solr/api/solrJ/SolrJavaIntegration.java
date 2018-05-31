package com.univers_architecture.solr.api.solrJ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.common.SolrInputDocument;

public class SolrJavaIntegration {

    private HttpSolrClient solrClient;

    public SolrJavaIntegration(String clientUrl) {

        solrClient = new HttpSolrClient.Builder(clientUrl).build();
        solrClient.setParser(new XMLResponseParser());
    }

    public void addProductBean(Film fBean) throws IOException, SolrServerException {

        solrClient.addBean(fBean);
        solrClient.commit();
    }

    public void addSolrDocument(String documentId, String name, String directedBy, String publishedDate, String[] category) throws SolrServerException, IOException {
    	
    	ArrayList<String> values = new ArrayList<String>(Arrays.asList(category));
    	SolrInputDocument document = new SolrInputDocument();
        document.addField("id", documentId);
        document.addField("name", name);
        document.addField("directed_by", directedBy);
        document.addField("published_date", publishedDate);
        document.addField("category", values );
        solrClient.add(document);
        solrClient.commit();
    }

    public void deleteSolrDocumentById(String documentId) throws SolrServerException, IOException {

        solrClient.deleteById(documentId);
        solrClient.commit();
    }

    public void deleteSolrDocumentByQuery(String query) throws SolrServerException, IOException {

        solrClient.deleteByQuery(query);
        solrClient.commit();
    }

    protected HttpSolrClient getSolrClient() {
        return solrClient;
    }

    protected void setSolrClient(HttpSolrClient solrClient) {
        this.solrClient = solrClient;
    }

}