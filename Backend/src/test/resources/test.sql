SELECT * FROM family;

DELETE FROM family where id=15;

INSERT INTO family (cid, fid, mid) VALUES (8, 2, 6);

INSERT INTO family (cid, fid, mid) VALUES (3, 1, 2);

INSERT INTO family (cid, fid, mid) VALUES (3, 2, 1);

INSERT INTO document(place, ownerid, type )
VALUES ('6', '4', '3');

DELETE FROM document WHERE id IN (9,10);

SELECT unaccent(lower('California')) = unaccent(lower('california'));

SELECT d.* FROM Document d
            JOIN Personindocument pd ON d.id = pd.document
            JOIN Person p ON p.id = pd.personid
            WHERE d.confirmed = true
            AND unaccent(lower(p.name)) LIKE unaccent(lower(concat('%kri%')))
            AND unaccent(lower(p.surname)) LIKE unaccent(lower(concat('Jenner')));

SELECT p.name, p.surname FROM Person p;





