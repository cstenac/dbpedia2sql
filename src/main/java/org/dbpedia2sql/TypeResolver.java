package org.dbpedia2sql;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dbpedia2sql.model.DataTriple;
import org.dbpedia2sql.model.LinkTriple;
import org.dbpedia2sql.util.URISimplifier;

public class TypeResolver {
	final static String owlThing = "http://www.w3.org/2002/07/owl#Thing";

	public void addTypeDeclaration(LinkTriple dt) {
		assert(dt.getPredicate().equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>"));

		String type = dt.getObject().replace("<", "").replace(">", "");

		// Useless
		if (type.equals(owlThing)) return;

		String internResult = URISimplifier.simplify(type).intern();

		Set<String> curTypes = subjectTypes.get(dt.getSubject());
		if (curTypes == null) {
			curTypes = new HashSet<String>();
			subjectTypes.put(dt.getSubject(), curTypes);
		}
		curTypes.add(internResult);
	}

	/* Extract of non-DBPedia types 
		 2 <http://schema.org/Hospital>
		      4 <http://schema.org/Canal>
		      4 <http://schema.org/Library>
		      4 <http://schema.org/RadioStation>
		      5 <http://schema.org/MusicRecording>
		      6 <http://schema.org/Continent>
		      6 <http://schema.org/Park>
		     10 <http://schema.org/Festival>
		     11 <http://schema.org/School>
		     14 <http://schema.org/WebPage>
		     16 <http://schema.org/StadiumOrArena>
		     18 <http://schema.org/TVEpisode>
		     19 <http://schema.org/LandmarksOrHistoricalBuildings>
		     21 <http://schema.org/TelevisionStation>
		     24 <http://schema.org/Museum>
		     26 <http://schema.org/Airport>
		     29 <http://schema.org/SportsEvent>
		     35 <http://schema.org/Mountain>
		     59 <http://schema.org/LakeBodyOfWater>
		     67 <http://schema.org/GovernmentOrganization>
		    111 <http://schema.org/RiverBodyOfWater>
		    135 <http://schema.org/MusicAlbum>
		    147 <http://schema.org/SportsTeam>
		    158 <http://schema.org/Language>
		    174 <http://schema.org/BodyOfWater>
		    232 <http://schema.org/CollegeOrUniversity>
		    247 <http://schema.org/EducationalOrganization>
		    324 <http://schema.org/Event>
		    384 <http://schema.org/Country>
		    421 <http://schema.org/Book>
		    438 <http://schema.org/Product>
		    657 <http://schema.org/City>
		    657 <http://schema.org/Movie>
		    693 <http://schema.org/MusicGroup>
		   1506 <http://schema.org/Organization>
		   1891 <http://schema.org/CreativeWork>
		   5554 <http://schema.org/Person>
		   5554 <http://xmlns.com/foaf/0.1/Person>
		   7502 <http://schema.org/Place>
		  21180 <http://www.w3.org/2002/07/owl#Thing>
	 */


	public Set<String> getTypes(String subject) {
		return subjectTypes.get(subject);
	}

	Map<String, Set<String>> subjectTypes = new HashMap<String, Set<String>>();
}
