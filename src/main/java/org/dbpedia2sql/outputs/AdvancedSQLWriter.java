package org.dbpedia2sql.outputs;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.util.DataGetter;
import org.dbpedia2sql.util.URISimplifier;

/** 
 * An SQL writer which only writes to some trivial flat tables, not very optimized for efficient lookups 
 */
public class AdvancedSQLWriter implements SubjectHandler{
	public AdvancedSQLWriter(Connection conn) throws Exception {
		this.conn = conn;
		
		insertSubject = conn.prepareStatement("INSERT INTO dbpedia_subject (uri) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
		insertSubjectWithName = conn.prepareStatement("INSERT INTO dbpedia_subject (uri, foafname) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
		insertPredicate = conn.prepareStatement("INSERT INTO dbpedia_predicate (id, predicate) VALUES (?,?)");
		insertOnto = conn.prepareStatement("INSERT INTO dbpedia_ontology (subject_id, predicate_id, value) VALUES(?,?,?)");
		insertRaw = conn.prepareStatement("INSERT INTO dbpedia_property (subject_id, predicate_id, value) VALUES(?,?,?)");

		conn.setAutoCommit(false);
	}

	Map<String, Integer> propId = new HashMap<String, Integer>();
	int lastPropId;

	public void close() throws Exception {
		conn.commit();
	}

	PreparedStatement insertSubject;
	PreparedStatement insertSubjectWithName;
	PreparedStatement insertPredicate;

	PreparedStatement insertOnto;
	PreparedStatement insertRaw;

	Connection conn;

	StringBuilder queries = new StringBuilder();
	public void handleSubject(String subject, List<DataTriple> properties, List<LinkTriple> links) throws IOException{
		try {
			String foafName = DataGetter.getString(properties, "foaf:name", null);
			long subjectId = 0;
			if (foafName == null) {
				insertSubject.setString(1, subject);
				insertSubject.execute();
				ResultSet g = insertSubject.getGeneratedKeys();
				g.next(); subjectId = g.getLong(1); g.close();
			} else {
				insertSubjectWithName.setString(1, subject);
				insertSubjectWithName.setString(2, foafName);
				insertSubjectWithName.execute();
				ResultSet g = insertSubjectWithName.getGeneratedKeys();
				g.next(); subjectId = g.getLong(1); g.close();
			}

			for (DataTriple property : properties) {
				String predicate = URISimplifier.simplify(property.getPredicate());
				Integer predicateId = propId.get(predicate);
				if (predicateId == null) {
					predicateId = ++lastPropId;
					propId.put(predicate, predicateId);
					insertPredicate.setInt(1, predicateId);
					insertPredicate.setString(2, predicate);
					insertPredicate.execute();
				}

				if (predicate.startsWith("dbonto")) {
					insertOnto.setInt(1, (int)subjectId);
					insertOnto.setInt(2, predicateId);
					insertOnto.setString(3, property.getData());
					insertOnto.execute();
				}

				insertRaw.setInt(1, (int)subjectId);
				insertRaw.setInt(2, predicateId);
				insertRaw.setString(3, property.getData());
				insertRaw.execute();
			}

		} catch (SQLException e) {
			throw new IOException("Failed to insert", e);
		}
	}
}