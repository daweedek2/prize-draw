-- Init data for Person and Prize
-- H2 note: boolean literals are TRUE/FALSE

-- ==== PERSONS ====
INSERT INTO person (jmeno) VALUES ('Alice');
INSERT INTO person (jmeno) VALUES ('Bob');
INSERT INTO person (jmeno) VALUES ('Charlie');
INSERT INTO person (jmeno) VALUES ('Diana');
INSERT INTO person (jmeno) VALUES ('Edward');
INSERT INTO person (jmeno) VALUES ('Fiona');

-- ==== PRIZES (orderIndex defines draw order; lower number = earlier) ====
INSERT INTO prize (nazev, assigned, order_index) VALUES ('Tričko', FALSE, 1);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('Hrníček', FALSE, 2);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('Batoh', FALSE, 3);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('Pouzdro', FALSE, 4);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('Nálepky', FALSE, 5);