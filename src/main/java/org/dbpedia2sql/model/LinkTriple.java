package org.dbpedia2sql.model;

public class LinkTriple extends Triple {
	private String object;

	public LinkTriple(String subject, String predicate, String object) {
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
	}
	
	public String toString() {
		return "<LINK: s=" + subject + " p=" + predicate + " o=" + object + ">";
	}


	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}
}