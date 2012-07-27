package org.dbpedia2sql.outputs;

import java.io.IOException;
import java.util.List;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;

public interface SubjectHandler {
	public void handleSubject(String subject, List<DataTriple> properties, List<LinkTriple> links) throws IOException;
}