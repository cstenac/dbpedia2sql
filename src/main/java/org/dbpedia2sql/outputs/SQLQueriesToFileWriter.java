package org.dbpedia2sql.outputs;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;

public class SQLQueriesToFileWriter implements SubjectHandler{
	public SQLQueriesToFileWriter(String file) throws IOException {
		fos = new FileOutputStream(file);
	}
	FileOutputStream fos ;
	SQLQueriesGenerator generator = new SQLQueriesGenerator();
	
	StringBuilder queries = new StringBuilder();
	public void handleSubject(String subject, List<DataTriple> properties, List<LinkTriple> links) throws IOException{
		fos.write(generator.getQueries(subject, properties, links).getBytes("utf8"));
	}
}