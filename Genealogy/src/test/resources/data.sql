INSERT INTO role (id, rolename) VALUES (1, 'genealogist');
INSERT INTO role (id, rolename) VALUES (2, 'user');

INSERT INTO Users (idrole, username, password, mail) VALUES (1, 'admin', 'password', 'admin@onet.pl');
INSERT INTO Users (idrole, username, password, mail) VALUES (2, 'iwona', 'password', 'iwona@onet.pl');
INSERT INTO Users (idrole, username, password, mail) VALUES (2, 'basia', 'password', 'basia@onet.pl');
INSERT INTO Users (idrole, username, password, mail) VALUES (2, 'zenek', 'password', 'zenek@onet.pl');
INSERT INTO Users (idrole, username, password, mail) VALUES (1, 'genealog', 'password', 'genealog@onet.pl');

INSERT INTO documenttype (typename, template) VALUES ('photography', '{"fields": [' ||
                                                                     '{"name": "exampleField1", "type": "text"}, ' ||
                                                                     '{"name": "exampleField2", "type": "text"}]}');
INSERT INTO documenttype (typename, template) VALUES ('marriage act', '{"fields": [' ||
                                                                      '{"name": "exampleField1", "type": "text"}, ' ||
                                                                      '{"name": "exampleField2", "type": "text"}]}');
INSERT INTO documenttype (typename, template) VALUES ('birth act', '{"fields": [' ||
                                                                   '{"name": "exampleField1", "type": "text"}, ' ||
                                                                   '{"name": "exampleField2", "type": "text"}]}');
INSERT INTO documenttype (typename, template) VALUES ('death act', '{"fields": [' ||
                                                                   '{"name": "exampleField1", "type": "text"}, ' ||
                                                                   '{"name": "exampleField2", "type": "text"}]}');
INSERT INTO documenttype (typename, template) VALUES ('school report', '{"fields": [' ||
                                                                       '{"name": "exampleField1", "type": "text"}, ' ||
                                                                       '{"name": "exampleField2", "type": "text"}]}');
INSERT INTO documenttype (typename, template) VALUES ('apprenticeship document', '{"fields": [' ||
                                                                                 '{"name": "exampleField1", "type": "text"}, ' ||
                                                                                 '{"name": "exampleField2", "type": "text"}]}');
INSERT INTO documenttype (typename, template) VALUES ('posters, leaflets, placards', '{"fields": [' ||
                                                                                     '{"name": "exampleField1", "type": "text"}, ' ||
                                                                                     '{"name": "exampleField2", "type": "text"}]}');
INSERT INTO documenttype (typename, template) VALUES ('letters and correspondence', '{"fields": [' ||
                                                                                    '{"name": "newWifeSurname", "type": "varchar(256)"}, ' ||
                                                                                    '{"name": "newHusbandSurname", "type": "varchar(256)"}, ' ||
                                                                                    '{"name": "wifeFather", "type": "varchar(256)"}, ' ||
                                                                                    '{"name": "wifeMother", "type": "varchar(256)"}, ' ||
                                                                                    '{"name": "husbandFather", "type": "varchar(256)"}, ' ||
                                                                                    '{"name": "husbandMother", "type": "varchar(256)"}, ' ||
                                                                                    '{"name": "childrenSurname", "type": "varchar(256)"}]}');

INSERT INTO person (name, surname) VALUES ('Kris', 'Jenner');
INSERT INTO person (name, surname) VALUES ('William', 'Jenner');
INSERT INTO person (name, surname) VALUES ('Kylie', 'Jenner');
INSERT INTO person (name, surname) VALUES ('Kendall', 'Jenner');
INSERT INTO person (name, surname) VALUES  ('Jacques', 'Webster');
INSERT INTO person (name, surname) VALUES ('Stormi', 'Webster');
INSERT INTO person (name, surname) VALUES ('Robert', 'Kardashian');
INSERT INTO person (name, surname) VALUES ('Kim', 'Kardashian');

INSERT INTO family (cid, fid, mid) VALUES (3, 2, 1);
INSERT INTO family (cid, fid, mid) VALUES (4, 2, 1);
INSERT INTO family (cid, fid, mid) VALUES (6, 5, 3);
INSERT INTO family (cid, fid, mid) VALUES (8, 7, 1);

INSERT INTO address(country, voivodeship, city, longitude, latitude) VALUES ('USA', 'California', 'Los Angeles', -118.242766, 34.0536909);
INSERT INTO address(country, voivodeship, community, city, address, postalcode, longitude, latitude, parish, secular) VALUES ('USA', 'California', 'Los Angeles', 'Los Angeles', '123 Main St', '90001', -118.242766, 34.0536909, 'St. Patrick"s Parish', true);
INSERT INTO address(country, city, longitude, latitude) VALUES ('France', 'Paris', 2.352222, 48.856614);
INSERT INTO address(country, city) VALUES ('Japan', 'Tokyo');
INSERT INTO address(longitude, latitude) VALUES (139.691706, 35.689487);
INSERT INTO address(country, voivodeship, city, postalcode, parish) VALUES ('Germany', 'Bavaria', 'Munich', '80331', 'St. Peters Church');
INSERT INTO address(parish, secular) VALUES ('St. Johns Cathedral', false);
INSERT INTO address(country, city, address, postalcode) VALUES ('Italy', 'Rome', 'Via Nazionale 10', '00184');
INSERT INTO address(country, city, postalcode) VALUES ('Australia', 'Sydney', '2000');
INSERT INTO address(city, parish) VALUES ('Dublin', 'Christ Church Cathedral');
INSERT INTO address(country) VALUES ('Brazil');

INSERT INTO date (year, month, day) VALUES (1997, 8, 10);
INSERT INTO date (year, month, day) VALUES (2017, 11, 13);
INSERT INTO date (year, month, day) VALUES (1958, 8, 18);
INSERT INTO date (year, month, day) VALUES (2001, 5, 21);
INSERT INTO date (year, month, day) VALUES (2016, 7, 11);
INSERT INTO date (year, month, day) VALUES (2003, 1, 19);
INSERT INTO date (year, month, day) VALUES (1999, 3, 27);
INSERT INTO date (year, month, day) VALUES (1970, 10, 30);
INSERT INTO date (year, month, day) VALUES (1986, 11, 2);
INSERT INTO date (year, month, day) VALUES (1969, 2, 17);

INSERT INTO localaddress (country, city, postalcode) VALUES ('USA', 'Los Angeles', '12345');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Poland', 'Dolnośląskie', 'Wrocław', 'Adres 2/5', '54654');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Poland', 'Zachodniopomorskie', 'Szczecin', 'Słowackiego 2/6', '71100');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Germany', 'Bavaria', 'Munich', 'Maximilianstraße 17', '80539');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('France', 'Île-de-France', 'Paris', 'Champs-Élysées 92', '75008');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Italy', 'Lazio', 'Rome', 'Via del Corso 15', '00186');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Australia', 'New South Wales', 'Sydney', 'George Street 200', '2000');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Japan', 'Tokyo Metropolis', 'Tokyo', 'Shibuya 2-21-1', '150-0002');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Brazil', 'São Paulo', 'São Paulo', 'Avenida Paulista 1230', '01310-100');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Canada', 'Ontario', 'Toronto', 'Queen Street West 420', 'M5V 2B6');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('Russia', 'Moscow', 'Moscow', 'Tverskaya Street 15', '125009');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('South Africa', 'Western Cape', 'Cape Town', 'Long Street 130', '8001');
INSERT INTO localaddress (country, voivodeship, city, address, postalcode) VALUES ('China', 'Shanghai', 'Shanghai', 'Nanjing Road 300', '200001');

INSERT INTO location(idphys, idurl) VALUES (1, 1);
INSERT INTO location(idphys, idurl) VALUES (2, 2);
INSERT INTO location(idphys, idurl) VALUES (3, 3);

INSERT INTO physicallocations("Date", isoriginal, type, idphys, localaddressid, "User") VALUES ('10/08/1997', true, 'Księga urodzenia', 1, 1, 1);
INSERT INTO physicallocations("Date", isoriginal, type, idphys, localaddressid, "User") VALUES ('07/15/2006', false, 'Skan księgi urodzenia', 1, 2, 5);
INSERT INTO physicallocations("Date", isoriginal, type, idphys, localaddressid, "User") VALUES ('02/14/2010', false, 'Kopia wycinka z księgi', 1, 3, 1);
INSERT INTO physicallocations("Date", isoriginal, type, idphys, localaddressid, "User") VALUES ('08/10/2022', true, 'Akt małżeństwa Kim Kardashian oraz Kanye West', 2, 1, 5);

INSERT INTO urls(IDURL, URL, COMMENT) VALUES (1, 'https://www.drive.com/user/catalog', 'Zdjęcie aktu z księgi');
INSERT INTO urls(IDURL, URL, COMMENT) VALUES (2, 'https://www.drive.com/user/catalog/kim_kanye_marriage_certificate', 'Akt małżeństwa Kim Kardashian i Kanye Westa');
INSERT INTO urls(IDURL, URL, COMMENT) VALUES (3, 'https://www.drive.com/user/catalog/jenner_family_trip_italy_poster', 'Plakat z wyjazdu Jennerów do Włoch');
INSERT INTO urls(IDURL, URL, COMMENT) VALUES (2, 'https://www.drive.com/user/catalog/kim', 'Akt małżeństwa Kim Kardashian');

INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE, LOCALIZATION) VALUES (true, 'Akt urodzenia Kylie Jenner', 1, 1, 1, 3, 3);
INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE) VALUES (true, 'Zdjęcie rodzinne z chrztu kylie Jenner', 7, 2, 3, 1);
INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE, LOCALIZATION) VALUES (true, 'Akt małżeństwa Kim Kardashian i Kanye Westa', 2, 7, 4, 2, 2);
INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE) VALUES (false, 'List od Kim Kardashian', 4, 6, 3, 8);
INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE) VALUES (false, 'Akt urodzenia Stormi Webster', 2, 1, 2, 3);
INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE) VALUES (false, 'Zdjęcie aktu urodzenia Stormi Webster', 2, 1, 2, 1);
INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE) VALUES (true, 'Raport szkolny Kendalla Jenner', 6, 2, 1, 5);
INSERT INTO document(CONFIRMED, TITLE, "Date", PLACE, OWNERID, TYPE, LOCALIZATION) VALUES (true, 'Plakat z wyjazdu Jennerów do Włoch', 7, 8, 4, 7, 3);

INSERT INTO notification(TITLE, CONTEXT, DISPLAYED, "Date", "User", DOCUMENT) VALUES ('Dodano nowy dokument', 'Context', false, '10/06/2003', 3, 4);
INSERT INTO notification(TITLE, CONTEXT, DISPLAYED, "Date", "User", DOCUMENT) VALUES ('Dodano nowy dokument', 'Context', false, '02/18/2021', 2, 5);
INSERT INTO notification(TITLE, CONTEXT, DISPLAYED, "Date", "User", DOCUMENT) VALUES ('Dodano nowy dokument', 'Context', false, '02/20/2021', 2, 6);