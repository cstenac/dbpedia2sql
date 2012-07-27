package org.dbpedia2sql.outputs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.util.URISimplifier;

/** 
 * An SQL writer which only writes to some trivial flat tables, not very optimized for efficient lookups 
 */
public class SimpleSQLWriter implements SubjectHandler{
	public SimpleSQLWriter() throws Exception {
		Class.forName("org.postgresql.Driver");
		conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/osm", "osm", "osm");


		insertSubject = conn.prepareStatement("INSERT INTO dbpedia_subject (uri) VALUES(?)");
		insertOnto = conn.prepareStatement("INSERT INTO dbpedia_ontology (subject, property, value) VALUES(?,?,?)");
		insertRaw = conn.prepareStatement("INSERT INTO dbpedia_property (subject, property, value) VALUES(?,?,?)");
		
		conn.setAutoCommit(false);
	}
	
	public void close() throws Exception {
		conn.commit();
	}

	PreparedStatement insertSubject;
	PreparedStatement insertOnto;
	PreparedStatement insertRaw;

	Connection conn;

	StringBuilder queries = new StringBuilder();
	public void handleSubject(String subject, List<DataTriple> properties, List<LinkTriple> links) throws IOException{
		try {
			insertSubject.setString(1, subject);
			insertSubject.execute();

			for (DataTriple property : properties) {
				String predicate = URISimplifier.simplify(property.getPredicate());

				if (predicate.startsWith("dbonto")) {
					insertOnto.setString(1, subject);
					insertOnto.setString(2, predicate);
					insertOnto.setString(3, property.getData());
					insertOnto.execute();
				}

				insertRaw.setString(1, subject);
				insertRaw.setString(2, predicate);
				insertRaw.setString(3, property.getData());
				insertRaw.execute();
			}

		} catch (SQLException e) {
			throw new IOException("Failed to insert", e);
		}
	}
}