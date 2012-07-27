package org.dbpedia2sql.stream;
import java.io.IOException;

import org.dbpedia2sql.model.Triple;



public interface TriplesInputStream {
	/**
	 * @return null when end of stream is reached
	 */
	public Triple nextTriple() throws IOException;
}
