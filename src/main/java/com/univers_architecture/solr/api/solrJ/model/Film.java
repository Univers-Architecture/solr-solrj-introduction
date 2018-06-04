package com.univers_architecture.solr.api.solrJ.model;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

public class Film {

    String id;
    String directed_by;
    String name;
    String published_date;
    List<String> category = new ArrayList<String>();
    float revenue;
    
    public Film() {
		super();
	}
    
    public Film(String id, String name, String directed_by,String[] category) {
        super();
        this.id = id;
        this.name = name;
        this.directed_by = directed_by;
        this.category = Arrays.asList(category);
        
    }

    public String getId() {
        return id;
    }

    @Field("id")
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Field("name")
    public void setName(String name) {
        this.name = name;
    }

	public String getDirected_by() {
		return directed_by;
	}
	
    @Field("directed_by")
	public void setDirected_by(String directed_by) {
		this.directed_by = directed_by;
	}

	public String getPublished_date() {
		return published_date;
	}
    @Field("published_date")
	public void setPublished_date(String published_date) {
		this.published_date = published_date;
	}

	public List<String> getCategory() {
		return category;
	}
    @Field("category")
	public void setCategory(List<String> category) {
		this.category = category;
	}

	public float getRevenue() {
		return revenue;
	}
    @Field("revenue")
	public void setRevenue(float revenue) {
		this.revenue = revenue;
	}

	

	

	
   
}
