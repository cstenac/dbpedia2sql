
DROP TABLE IF EXISTS dbpedia_ontology;
DROP TABLE IF EXISTS dbpedia_property;
DROP TABLE IF EXISTS dbpedia_subject;
CREATE TABLE dbpedia_subject (
  id  SERIAL PRIMARY KEY,
  uri varchar(1024),
  foafname varchar(1024)
);


DROP TABLE IF EXISTS dbpedia_type;
CREATE TABLE dbpedia_type (
  uri varchar(1024),
  type varchar(128)
);

DROP TABLE IF EXISTS dbpedia_predicate;
CREATE TABLE dbpedia_predicate (
  id INTEGER PRIMARY KEY,
  predicate varchar(2048)
);

CREATE TABLE dbpedia_ontology (
  subject_id INTEGER REFERENCES dbpedia_subject(id),
  predicate_id INTEGER REFERENCES dbpedia_predicate(id),
  value text
);

CREATE TABLE dbpedia_property (
  subject_id INTEGER REFERENCES dbpedia_subject(id),
  predicate_id INTEGER REFERENCES dbpedia_predicate(id),
  value text
);
