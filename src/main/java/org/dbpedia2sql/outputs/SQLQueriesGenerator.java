package org.dbpedia2sql.outputs;

import java.io.IOException;
import java.util.List;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.util.URISimplifier;

public class SQLQueriesGenerator {
	public StringBuilder queries = new StringBuilder();
	public String getQueries(String subject, List<DataTriple> properties, List<LinkTriple> links) throws IOException{
		queries.setLength(0);

		String simpleSubject =  URISimplifier.simplify(subject);

		queries.append("INSERT INTO dbpedia_subject ('uri') VALUES('" + simpleSubject + "')\n"); 

		for (DataTriple property : properties) {
			String predicate = URISimplifier.simplify(property.getPredicate());
			if (predicate.equals("http://www.georss.org/georss/point")) {
				// Ignore redundant with wgs84
				continue;
			}

			queries.append("INSERT INTO dbpedia_ontology ('subject', 'property', 'value')");
			queries.append(" VALUES('" + simpleSubject + "','" + URISimplifier.simplify(property.getPredicate()) + "','" +
					property.getData() + "')\n");
		}
		return queries.toString();
	}
}
