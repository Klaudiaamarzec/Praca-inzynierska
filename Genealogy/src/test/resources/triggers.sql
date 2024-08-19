CREATE OR REPLACE FUNCTION check_family_relationship_exists()
    RETURNS TRIGGER AS $$
DECLARE
    parent_id BIGINT;
BEGIN
    -- Sprawdzenie, czy to dziecko już istnieje w tabeli `family`
    IF EXISTS (
        SELECT 1
        FROM family
        WHERE cid = NEW.cid
    ) THEN
        RAISE NOTICE 'Dziecko o ID % już istnieje w tabeli family i nie może być dodane ponownie.', NEW.cid;
        RETURN NULL; -- Zablokowanie wstawiania
    END IF;

    -- Sprawdzenie, czy istnieje relacja ojciec-dziecko
    SELECT fid INTO parent_id
    FROM family
    WHERE cid = NEW.cid
      AND fid = NEW.fid;

    -- Jeśli nie ma relacji ojciec-dziecko, sprawdzamy relację matka-dziecko
    IF parent_id IS NULL THEN
        SELECT mid INTO parent_id
        FROM family
        WHERE cid = NEW.cid
          AND mid = NEW.mid;
    END IF;

    -- Sprawdzenie, czy ta sama osoba jest już ojcem, ale teraz jest dodawana jako matka lub odwrotnie
    IF parent_id IS NOT NULL THEN
        RAISE NOTICE 'Relacja rodzic-dziecko już istnieje dla rodzica o ID % i dziecka o ID %.', parent_id, NEW.cid;
        RETURN NULL; -- Zablokowanie wstawiania
    END IF;

    -- Sprawdzenie, czy osoba dodawana jako ojciec jest już matką w innej relacji
    IF EXISTS (
        SELECT 1
        FROM family
        WHERE mid = NEW.fid
    ) THEN
        RAISE NOTICE 'Osoba o ID % jest już matką w innej relacji i nie może być ojcem.', NEW.fid;
        RETURN NULL; -- Zablokowanie wstawiania
    END IF;

    -- Sprawdzenie, czy osoba dodawana jako matka jest już ojcem w innej relacji
    IF EXISTS (
        SELECT 1
        FROM family
        WHERE fid = NEW.mid
    ) THEN
        RAISE NOTICE 'Osoba o ID % jest już ojcem w innej relacji i nie może być matką.', NEW.mid;
        RETURN NULL; -- Zablokowanie wstawiania
    END IF;

    -- Jeśli nie znaleziono konfliktów, pozwalamy na wstawienie nowego wiersza
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_check_family_relationship
BEFORE INSERT OR UPDATE ON family
FOR EACH ROW
EXECUTE FUNCTION check_family_relationship_exists();

CREATE OR REPLACE FUNCTION set_document_confirmed_status()
    RETURNS TRIGGER AS $$
DECLARE
    user_role INT;
BEGIN
    -- Get user role based on ownerID
    SELECT idrole INTO user_role
    FROM "User"
    WHERE id = NEW.OWNERID;

    -- If it is genealogist (ID = 1)
    IF user_role = 1 THEN
        NEW.confirmed = TRUE;
        -- If it is user (ID = 2)
    ELSIF user_role = 2 THEN
        NEW.confirmed = FALSE;
    ELSE
        RAISE EXCEPTION 'Nieznana rola dla użytkownika o ID %', NEW.OWNERID;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER before_insert_document
    BEFORE INSERT ON document
    FOR EACH ROW
EXECUTE FUNCTION set_document_confirmed_status();


