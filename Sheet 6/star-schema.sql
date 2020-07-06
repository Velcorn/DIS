CREATE TABLE facttable(
  id INTEGER NOT NULL PRIMARY KEY,
  dateID INTEGER,
  shopID INTEGER,
  articleID INTEGER,
  sold INTEGER,
  revenue double precision NOT NULL
);

CREATE TABLE dateID(
  id INTEGER NOT NULL PRIMARY KEY,
  day INTEGER,
  month INTEGER,
  year INTEGER
);

CREATE TABLE articleID(
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(50),
  price double precision,
  productGroup VARCHAR(50),
  productFamily VARCHAR(50),
  productCategory VARCHAR(50)
);

CREATE TABLE shopID(
  id INTEGER NOT NULL PRIMARY KEY,
  name VARCHAR(50),
  city VARCHAR(50),
  region VARCHAR(50),
  country VARCHAR(50)
);

ALTER TABLE facttable ADD FOREIGN KEY (dateID) REFERENCES dateID(id) ON DELETE RESTRICT;
ALTER TABLE facttable ADD FOREIGN KEY (articleID) REFERENCES articleID(id) ON DELETE RESTRICT;
ALTER TABLE facttable ADD FOREIGN KEY (shopID) REFERENCES shopID(id) ON DELETE RESTRICT;