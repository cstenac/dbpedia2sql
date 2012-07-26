dbpedia2sql
===========

DBPedia (http://www.dbpedia.org) provides a structured export of the Wikipedia infoboxes. It is a huge repository of structured data, based on semantic Web technologies.

While these technologies provide advanced structuring and reasoning capabilities, their usage can be harder, as it requires the use of a specific SPARQL server, making it hard to efficiently
combine this data with data pre-existing in RDBMS.

dbpedia2sql aims at providing a partial and feature-limited export of DBPedia to populate a SQL database.

Design notes
------------

* Need careful streaming considerations, given the huge size of the dataset
* Not a generic RDF2SQL converter, will use some structure from the DBPedia dataset to provide efficient extraction
  (like the types / ontology / infoboxes triples split --> possible to first import the types, then the properties)

* Provide ability to have specific processing for some classes (example: generating a flat table for the "PopulatedPlace" ontology class)