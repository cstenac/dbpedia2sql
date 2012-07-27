package org.dbpedia2sql;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.model.Triple;
import org.dbpedia2sql.outputs.SubjectHandler;
import org.dbpedia2sql.specifics.FrenchSettlementHandler;
import org.dbpedia2sql.stream.MergedInputStream;
import org.dbpedia2sql.stream.TriplesInputStream;
import org.dbpedia2sql.util.URISimplifier;
import org.semanticweb.yars.nx.Resource;
import org.semanticweb.yars.nx.parser.NxParser;

public class SubjectAggregator {
	private String currentSubject;
	private List<DataTriple> currentProps = new ArrayList<DataTriple>();
	private List<LinkTriple> currentLinks = new ArrayList<LinkTriple>();
	private TypeResolver resolver;
	private SubjectHandler writer;
	private long processedSubjects;
	private long processedTriples;
	
	public SubjectAggregator(TypeResolver resolver, SubjectHandler writer) {
		this.resolver = resolver;
		this.writer = writer;
	}

	public void run(TriplesInputStream stream) throws IOException {
		while (true) {
			Triple next = stream.nextTriple();
			if (next == null) {
				if (currentSubject != null) {
					handleSubject(currentSubject, currentProps, currentLinks);
				}
				return;
			}
			
			String s = next.getSubject();

			// Try to url-decode the subject, errors allowed
			try {
				s = URLDecoder.decode(s, "utf8");
			} catch (IllegalArgumentException e) {
				
			}
			// And simplify the namespaces 
			s = URISimplifier.simplify(next.getSubject());
			
			
			if (!s.equals(currentSubject)) {
				/* New subject, handle the current one before accumulate */
				if (currentSubject != null) handleSubject(currentSubject, currentProps, currentLinks);
				currentSubject = s;
				currentProps.clear();
				currentLinks.clear();
			}
			if (next instanceof DataTriple) {
				DataTriple dt = (DataTriple)next;
				URISimplifier.simplify(dt);
//				System.out.println("SIMPLIFIED TRIPLE --> " + dt.getSubject());
				currentProps.add(dt);
			} else {
				currentLinks.add((LinkTriple)next);
			}
			if (++processedTriples % 20000 == 0) {
				System.out.println("Processed " + processedTriples + " triples (current subject: " + currentSubject + ")");
				((MergedInputStream)stream).debug();
//				break;
			}
		}

	}

	protected void handleSubject(String subject, List<DataTriple> properties, List<LinkTriple> links) throws IOException {
		Set<String> types = resolver.getTypes(subject);
//		System.out.println("HANDLE SUBJECT " + subject);
		
		// TODO
		if (types == null) {
//			System.out.println("Type unknown for " + subject + "!");
		} else if (types.contains("dbonto:Settlement")) {
			new FrenchSettlementHandler().handleSubject(subject, properties, links);
		//	System.out.println("A populated place ! " + subject);
		}
		
		writer.handleSubject(subject, properties, links);
		
		if (++processedSubjects % 20000 == 0) {
			System.out.println("Processed " + processedSubjects + " subjects");
		}

	}

}
