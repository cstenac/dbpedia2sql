package org.dbpedia2sql.stream;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.model.Triple;
import org.semanticweb.yars.nx.parser.NxParser;
import org.semanticweb.yars.nx.util.NxUtil;
import org.semanticweb.yars.nx.Literal;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.Resource;


public class TriplesReader implements TriplesInputStream {
	String file;
	NxParser np;
	Iterator<Node[]> it;
	public TriplesReader(String file) throws IOException {
		this.file = file;
		InputStream is = null;
		if (file.endsWith(".gz")) {
			is = new GZIPInputStream(new BufferedInputStream(new FileInputStream(new File(file))));
		} else {
			is = new BufferedInputStream(new FileInputStream(new File(file)));
		}
		np = new NxParser(is);
		it = np.iterator();
	}

	public String toString() {
		return "<TriplesReader: " + file + ">"; 
	}


	@Override
	public Triple nextTriple() throws IOException {
		if (!it.hasNext()) return null;

		Node[] next = it.next();

		Resource subject = (Resource)next[0];
		Resource predicate = (Resource)next[1];
		Node object = next[2];
		
		// WARNING: Don't clean up the subject yet ! We must only do it *after* the merge, else 
		// The merge won't be made in the same order as the Unix sort

		// Clean the predicate
		predicate = new Resource(URLDecoder.decode(predicate.toN3(), "utf8"));

		//		System.out.println("************************************");
		//		for (Node n : narray) {
		//			System.out.println(n.getClass()  + " --- " + n.toN3());
		//		}

		//		System.out.println("S=" + subject.toN3() + " P=" + predicate.toN3());

		if (object instanceof Literal) {
			Literal value = (Literal)object;
			// Resolve the \\u0FF.. stuff
			String cleanData = value.getUnescapedData();

			try {
				cleanData = URLDecoder.decode(cleanData, "utf8");
			} catch (IllegalArgumentException e) {}

			if (value.getDatatype() == null) {
				return new DataTriple(subject.toN3(), predicate.toN3(), cleanData, value.getLanguageTag());
			} else if (value.getDatatype().toN3().equals("<http://www.w3.org/2001/XMLSchema#double>")) {
				return new DataTriple(subject.toN3(), predicate.toN3(), Double.parseDouble(value.getData()));
			} else  {
				return new DataTriple(subject.toN3(), predicate.toN3(), cleanData, null);
			}


		} else {
			Resource value = (Resource)object;
			//			System.out.println("OBJ " + value.toN3());
			return new LinkTriple(subject.toN3(), predicate.toN3(), value.toN3());
		}
	}

	public static void main(String[] args) throws Exception {
		TriplesReader tr = new TriplesReader("/home/clement/osm/dbpedia/props_en.nt.gz");

		while (true) {
			Triple t = tr.nextTriple();
			if (t == null) return;
			System.out.println(t.toString());
		}
	}

}

