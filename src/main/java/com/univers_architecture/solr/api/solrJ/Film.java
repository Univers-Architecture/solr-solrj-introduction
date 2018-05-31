package com.univers_architecture.solr.api.solrJ;

import org.apache.solr.client.solrj.beans.Field;

public class Film {

    String id;
    String directed_by;
    String name;

    public Film(String id, String name, String directed_by) {
        super();
        this.id = id;
        this.name = name;
        this.directed_by = directed_by;
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

   
}
