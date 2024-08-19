SELECT * FROM family;

DELETE FROM family where id=15;

INSERT INTO family (cid, fid, mid) VALUES (8, 2, 6);

INSERT INTO family (cid, fid, mid) VALUES (3, 1, 2);

INSERT INTO family (cid, fid, mid) VALUES (3, 2, 1);

INSERT INTO document(place, ownerid, type )
VALUES ('6', '4', '3');

DELETE FROM document WHERE id IN (9,10);