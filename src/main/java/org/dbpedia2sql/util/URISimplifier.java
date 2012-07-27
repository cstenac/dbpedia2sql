package org.dbpedia2sql.util;

import java.util.HashSet;
import java.util.Set;

import org.dbpedia2sql.model.DataTriple;

public class URISimplifier {
	public static void simplify(DataTriple dt) {
		dt.setSubject(simplify(dt.getSubject()));
		dt.setPredicate(simplify(dt.getPredicate()));
	}
	public static String simplify(String uri) {
		if (uri.startsWith("<")) {
			uri = uri.replace("<", "").replace(">", "");
		}

		// TODO: Better parsing
		if (uri.startsWith("http://schema.org/")) {
			return "schema:" + uri.substring(17);
		} else if (uri.startsWith("http://dbpedia.org/ontology/")) {
			return  "dbonto:" + uri.substring(28);
		} else if (uri.startsWith("http://dbpedia.org/property/")) {
			return  "dbprop:" + uri.substring(28);
		} else if (uri.startsWith("http://dbpedia.org/resource/")) {
			return uri.substring(28);
		} else if (uri.startsWith("http://xmlns.com/foaf/0.1/")) {
			return  "foaf:"  + uri.substring(26);
		} else if (uri.startsWith("http://www.w3.org/2003/01/geo/wgs84_pos")) {
			return "wgs84:" + uri.substring(39);
		} else {
			return uri; // Can't simplify
		}
	}
}
