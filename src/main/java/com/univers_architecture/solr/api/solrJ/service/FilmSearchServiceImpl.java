package com.univers_architecture.solr.api.solrJ.service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

import com.univers_architecture.solr.api.solrJ.model.Film;


public class FilmSearchServiceImpl implements FilmSearchService {

    private final SolrClient solrClient;

    public FilmSearchServiceImpl(SolrClient solrClient) {
        this.solrClient = solrClient;
    }

   
    public void indexBean(Film film) throws IOException, SolrServerException {
        solrClient.addBean(film);
        solrClient.commit();
    }

	public void index(String id, String directed_by, String name, String published_date, String[] category,
			float revenue) throws SolrServerException, IOException {
		ArrayList<String> values = new ArrayList<String>(Arrays.asList(category));

    	SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", id);
        doc.addField("directed_by", directed_by);
        doc.addField("name", name);
        doc.addField("category", values);
        doc.addField("published_date", published_date);
        doc.addField("revenue", revenue);
        solrClient.add(doc);
        solrClient.commit();
		
	}

}
