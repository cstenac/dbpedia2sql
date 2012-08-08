package org.dbpedia2sql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.model.Triple;
import org.dbpedia2sql.outputs.AdvancedSQLWriter;
import org.dbpedia2sql.outputs.NoOpWriter;
import org.dbpedia2sql.outputs.SQLQueriesToFileWriter;
import org.dbpedia2sql.outputs.SimpleSQLWriter;
import org.dbpedia2sql.specifics.FrenchSettlementHandler;
import org.dbpedia2sql.stream.MergedInputStream;
import org.dbpedia2sql.stream.TriplesReader;
import org.dbpedia2sql.util.URISimplifier;

public class Main {
	public static void main(String[] args) throws Exception {
		TypeResolver types = new TypeResolver();
		
		TriplesReader typesReader = new TriplesReader("/home/clement/osm/dbpedia-to-sql/data/types/types_fr.nt.gz");
		int i = 0;
		while (true) {
			Triple t = typesReader.nextTriple();
			if (t == null) break;
			LinkTriple lt = (LinkTriple)t;
			lt.setSubject(URISimplifier.simplify(lt.getSubject()));
			types.addTypeDeclaration((LinkTriple)t);
			if (++i % 10000 == 0) {
				System.out.println("Read " + i + " types");
			}
		}
		
		TriplesReader props1 = new TriplesReader("/home/clement/osm/dbpedia-to-sql/data/data/props_fr.snt.gz");
		TriplesReader props2 = new TriplesReader("/home/clement/osm/dbpedia-to-sql/data/data/infobox_fr.snt.gz");
		MergedInputStream mis = new MergedInputStream();
		mis.addInput(props1);
		mis.addInput(props2);

		mis.start();
		
		Class.forName("org.postgresql.Driver");
		Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/osm", "osm", "osm");

//		SQLQueriesToFileWriter out = new SQLQueriesToFileWriter("/tmp/data");
//		TrivialSQLWriter out = new TrivialSQLWriter();
//		AdvancedSQLWriter out  = new AdvancedSQLWriter(conn);
		
		NoOpWriter out = new NoOpWriter();
		
		SubjectAggregator aggregator = new SubjectAggregator(types, out);
		
		FrenchSettlementHandler handler = new FrenchSettlementHandler(conn);
		aggregator.addSpecificTypeHandler("dbonto:Settlement", handler);
		
		aggregator.run(mis);
		
//		out.close();
		
	}
}
