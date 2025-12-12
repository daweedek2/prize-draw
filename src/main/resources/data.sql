-- Init data for Person and Prize
-- H2 note: boolean literals are TRUE/FALSE

-- ==== PERSONS ====
INSERT INTO person (jmeno) VALUES ('Tele_a_Sele');
INSERT INTO person (jmeno) VALUES ('geoPetros');
INSERT INTO person (jmeno) VALUES ('MT-Jonea');
INSERT INTO person (jmeno) VALUES ('Samko20');
INSERT INTO person (jmeno) VALUES ('mmop');
INSERT INTO person (jmeno) VALUES ('gogon1+3pidalky');
INSERT INTO person (jmeno) VALUES ('Blue.Elf');
INSERT INTO person (jmeno) VALUES ('Shepherd96');
INSERT INTO person (jmeno) VALUES ('katu1005');
INSERT INTO person (jmeno) VALUES ('kacerpetr+E');
INSERT INTO person (jmeno) VALUES ('sutrarka');
INSERT INTO person (jmeno) VALUES ('KUBAJS+J');
INSERT INTO person (jmeno) VALUES ('Teniskovi');
INSERT INTO person (jmeno) VALUES ('Adi Obo');
INSERT INTO person (jmeno) VALUES ('a_d_a_m_s');
INSERT INTO person (jmeno) VALUES ('VitaL9');
INSERT INTO person (jmeno) VALUES ('Sonule');
INSERT INTO person (jmeno) VALUES ('KočičáciZlín');

-- ==== PRIZES (orderIndex defines draw order; lower number = earlier) ====
-- Additional prizes
INSERT INTO prize (nazev, assigned, order_index) VALUES ('multifunkční mixér s kovovými převody', FALSE, 1);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('motorový vyžínač', FALSE, 2);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('nerezový odšťavňovač', FALSE, 3);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('roční prémiové členství na geocaching.com + travel bug', FALSE, 4);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('toastovač', FALSE, 5);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('ručně vyrobený krojovaný valach', FALSE, 6);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('bílá bezdrátová sluchátka', FALSE, 7);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('černá bezdrátová sluchátka', FALSE, 8);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('tyčový mixér', FALSE, 9);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('LED fotorámeček', FALSE, 10);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('krokoměr', FALSE, 11);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('čelovka + plátkové mýdlo', FALSE, 12);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('český křišťál + pláštěnka', FALSE, 13);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('logická karetní hra + pexeso', FALSE, 14);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('geovíno + petka plná peněz', FALSE, 15);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('souprava pro přípravu salátů', FALSE, 16);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('pelety 15kg + ventilační mřížky', FALSE, 17);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('brikety 10kg + podpalovač dřeva', FALSE, 18);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('kávová sada + lapač hmyzu', FALSE, 19);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('magický hrnek + zásobník na občerstvení', FALSE, 20);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('skleničky na šampaňské + zásobník na občerstvení', FALSE, 21);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('napařovač + odkapávací miska + tvořítko croissantů', FALSE, 22);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('plyšový sněhulák Olaf + plyšový králík Bobek', FALSE, 23);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('cyklosada', FALSE, 24);
INSERT INTO prize (nazev, assigned, order_index) VALUES ('sluneční brýle + tričko v kelímku', FALSE, 25);

INSERT INTO prize (nazev, assigned, order_index) VALUES ('BONUS: Geokalendář 2026', FALSE, 100);