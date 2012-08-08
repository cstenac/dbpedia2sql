DROP TABLE IF EXISTS dbpedia_city;
CREATE TABLE dbpedia_city (
  insee VARCHAR(100),
  population INTEGER,
  maire VARCHAR(256),
  uri VARCHAR(1024),
  foafname varchar(1024)
);
