package com.univers_architecture.solr.api.solrJ;



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
    protected void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    @Field("name")
    protected void setName(String name) {
        this.name = name;
    }

	public String getDirected_by() {
		return directed_by;
	}
	
    @Field("directed_by")
	protected void setDirected_by(String directed_by) {
		this.directed_by = directed_by;
	}

	public String getPublished_date() {
		return published_date;
	}
    @Field("published_date")
	protected void setPublished_date(String published_date) {
		this.published_date = published_date;
	}

	public List<String> getCategory() {
		return category;
	}
    @Field("category")
	protected void setCategory(List<String> category) {
		this.category = category;
	}

	

	
   
}
