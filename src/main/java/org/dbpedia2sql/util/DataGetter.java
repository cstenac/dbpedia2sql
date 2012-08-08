package org.dbpedia2sql.util;

import java.util.List;

import org.dbpedia2sql.model.DataTriple;

public class DataGetter {
	public static String getString(List<DataTriple> triples, String predicate, String lang, String defaultValue) {
		for (DataTriple triple : triples) {
			// TODO: lang
			if (triple.getPredicate().equals(predicate) && triple.getType().equals(DataTriple.Type.STRING)) {
				return triple.getData();
			}
		}
		return defaultValue;
	}
	
	public static String getString(List<DataTriple> triples, String predicate, String defaultValue) {
		for (DataTriple triple : triples) {
			if (triple.getPredicate().equals(predicate)) {
				return triple.getData();
			}
		}
		return defaultValue;
	}
	public static double getDouble(List<DataTriple> triples, String predicate, double defaultValue) {
		for (DataTriple triple : triples) {
			if (triple.getPredicate().equals(predicate) && triple.getType().equals(DataTriple.Type.DOUBLE)) {
				return triple.getDouble();
			}
		}
		return defaultValue;
	}
	public static int getInt(List<DataTriple> triples, String predicate, int defaultValue) {
		for (DataTriple triple : triples) {
			if (triple.getPredicate().equals(predicate)) return triple.getInt();
		}
		return defaultValue;
	}
	
	public static String getWGS84PointWKT(List<DataTriple> triples) {
		double zeLong = 0.0;
		double zeLat = 0.0;
		for (DataTriple triple : triples) {
			if (triple.getPredicate().equals("wgs84#long")) {
				zeLong = triple.getDouble();
			} else if (triple.getPredicate().equals("wgs84#lat")) {
				zeLat = triple.getDouble();
			}
		}
		return "POINT(" + zeLat + "," + zeLong + ")";
	}
}
