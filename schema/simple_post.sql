CREATE UNIQUE INDEX dbpedia_subject_index ON dbpedia_subject(uri);
CREATE UNIQUE INDEX dbpedia_type_index ON dbpedia_type (uri, type);
CREATE INDEX dbpedia_ontology_sindex ON dbpedia_ontology(subject);
CREATE INDEX dbpedia_ontology_spindex ON dbpedia_ontology(subject, property);
CREATE INDEX dbpedia_ontology_pindex ON dbpedia_ontology(property);
CREATE INDEX dbpedia_property_sindex ON dbpedia_property(subject);
CREATE INDEX dbpedia_property_spindex ON dbpedia_property(subject, property);
CREATE INDEX dbpedia_property_pindex ON dbpedia_property(property);
