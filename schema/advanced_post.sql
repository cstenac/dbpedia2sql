CREATE UNIQUE INDEX dbpedia_subject_index ON dbpedia_subject(uri);
CREATE INDEX dbpedia_subject_foafname ON dbpedia_subject(foafname);

CREATE INDEX dbpedia_type_index ON dbpedia_type(type);

CREATE INDEX dbpedia_predicate_index ON dbpedia_predicate(predicate);

CREATE INDEX dbpedia_ontology_sp ON dbpedia_ontology(subject_id, predicate_id);
CREATE INDEX dbpedia_ontology_s ON dbpedia_ontology(subject_id);
CREATE INDEX dbpedia_ontology_p ON dbpedia_ontology(predicate_id);

CREATE INDEX dbpedia_property_sp ON dbpedia_property(subject_id, predicate_id);
CREATE INDEX dbpedia_property_s ON dbpedia_property(subject_id);
CREATE INDEX dbpedia_property_p ON dbpedia_property(predicate_id);
