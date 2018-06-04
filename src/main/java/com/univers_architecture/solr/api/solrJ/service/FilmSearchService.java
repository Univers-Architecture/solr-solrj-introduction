package com.univers_architecture.solr.api.solrJ.service;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;

import com.univers_architecture.solr.api.solrJ.model.Film;

public interface FilmSearchService {

    public void index(String id, String directed_by,String name,String published_date, String[] category, float revenue) throws SolrServerException, IOException;

    public void indexBean(Film item) throws IOException, SolrServerException;

}
