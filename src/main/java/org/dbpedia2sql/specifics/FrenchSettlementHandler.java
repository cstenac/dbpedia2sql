package org.dbpedia2sql.specifics;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.outputs.SubjectHandler;
import org.dbpedia2sql.util.DataGetter;

public class FrenchSettlementHandler implements SubjectHandler {
	public FrenchSettlementHandler(Connection conn) throws Exception {
		this.conn = conn;
		insertCity = conn.prepareStatement("INSERT INTO dbpedia_city (insee, population, maire, uri, foafname) VALUES (?,?,?,?,?)");
	}
	private Connection conn;
	private PreparedStatement insertCity;
	private int handled;

	@Override
	public void handleSubject(String subject, List<DataTriple> properties,
			List<LinkTriple> links) throws IOException {

		try {
			String insee =  DataGetter.getString(properties, "dbprop:insee", "0");
			if (insee.equals("0")) return; // Not french :)

			String maire = DataGetter.getString(properties, "dbprop:maire", "?");
			
			int pop = 0;
			try {
				pop = DataGetter.getInt(properties, "dbonto:populationTotal", 0);
			} catch (Exception e) {}

			String foafname = DataGetter.getString(properties, "foaf:name", "?");

			insertCity.setString(1, insee);
			insertCity.setInt(2, pop);
			insertCity.setString(3, maire);
			insertCity.setString(4, subject);
			insertCity.setString(5, foafname);
			insertCity.execute();
			//		System.out.println("SETTLEMENT: " + subject);
			//		System.out.println("****** insee=" + DataGetter.getString(properties, "dbprop:insee", "0"));
			//		System.out.println("****** population=" + DataGetter.getString(properties, "dbonto:populationTotal", "0"));
			//		System.out.println("****** location=" + DataGetter.getWGS84PointWKT(properties));
			
			if (handled++ == 1000) {
				System.out.println("Processed " + handled + " settlements (last= " + foafname + ")");
			}
			
		} catch (Exception e) {
			System.out.println("DUMP FAILURE FOR " + subject);
			for (DataTriple dt : properties) System.out.println("  " + dt);
			throw new IOException("Failed to insert", e);
		}
	}

}
