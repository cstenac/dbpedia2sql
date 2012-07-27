DROP TABLE IF EXISTS dbpedia_subject;
CREATE TABLE dbpedia_subject (
  uri varchar(1024)
);


DROP TABLE IF EXISTS dbpedia_type;
CREATE TABLE dbpedia_type (
  uri varchar(1024),
  type varchar(128)
);

DROP TABLE IF EXISTS dbpedia_ontology;
CREATE TABLE dbpedia_ontology (
  subject varchar(1024),
  property varchar(2048),
  value text
);

DROP TABLE IF EXISTS dbpedia_property;
CREATE TABLE dbpedia_property (
  subject varchar(1024),
  property varchar(2048),
  value text
);
