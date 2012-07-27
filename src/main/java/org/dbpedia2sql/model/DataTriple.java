package org.dbpedia2sql.model;

public class DataTriple extends Triple {
	public enum Type {
		STRING,
		INT,
		DOUBLE,
	}
	private String data;
	private String lang;
	private Type type;
	
	public DataTriple(String subject, String predicate, String data, String lang) {
		this.subject = subject;
		this.predicate = predicate;
		this.data = data;
		this.lang = lang;
		this.type = Type.STRING;
	}
	
	public DataTriple(String subject, String predicate, int data) {
		this.subject = subject;
		this.predicate = predicate;
		this.data = "" + data;
		this.type = Type.INT;
		
	}
	public DataTriple(String subject, String predicate, double data) {
		this.subject = subject;
		this.predicate = predicate;
		this.data = "" + data;
		this.type = Type.DOUBLE;
	}
	
	public String toString() {
		return "<DATA: s=" + subject + " p=" + predicate + " d=" + data + (lang == null ? "" : "(l=" + lang + ")") + ">";
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public int getInt() {
		assert(type == Type.INT);
		return Integer.parseInt(data);
	}
	
	public double getDouble() {
		assert(type == Type.DOUBLE);
		return Double.parseDouble(data);
	}	
}