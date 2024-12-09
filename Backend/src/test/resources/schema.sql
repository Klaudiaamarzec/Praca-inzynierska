DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Location CASCADE;
DROP TABLE IF EXISTS PhysicalLocations CASCADE;
DROP TABLE IF EXISTS URLS CASCADE;
DROP TABLE IF EXISTS Person CASCADE;
DROP TABLE IF EXISTS LocalAddress CASCADE;
DROP TABLE IF EXISTS Role CASCADE;
DROP TABLE IF EXISTS Document CASCADE;
DROP TABLE IF EXISTS PersonInDocument CASCADE;
DROP TABLE IF EXISTS "date" CASCADE;
DROP TABLE IF EXISTS Address CASCADE;
DROP TABLE IF EXISTS Notification CASCADE;
DROP TABLE IF EXISTS Family CASCADE;
DROP TABLE IF EXISTS DocumentType CASCADE;
DROP TABLE IF EXISTS AdditionalFields CASCADE;
CREATE TABLE Users (
                       ID              BIGSERIAL NOT NULL,
                       IDRole          int4 NOT NULL,
                       UserName        varchar(64) NOT NULL UNIQUE,
                       Password        varchar(256) NOT NULL,
                       Mail            text NOT NULL UNIQUE,
                       ResetToken      text,
                       TokenExpiration timestamp);
CREATE TABLE Location (
                          ID     BIGSERIAL NOT NULL,
                          IDPhys int8 UNIQUE,
                          IDURL  int8 NOT NULL UNIQUE);
CREATE TABLE PhysicalLocations (
                                   ID             BIGSERIAL NOT NULL,
                                   "date"         date NOT NULL,
                                   isOriginal     bool NOT NULL,
                                   "condition"    varchar(64),
                                   Type           text,
                                   Description    text,
                                   IDPhys         int8 NOT NULL,
                                   LocalAddressID int8 NOT NULL,
                                   "user"         int8 NOT NULL);
CREATE TABLE URLS (
                      ID      BIGSERIAL NOT NULL,
                      IDURL   int8 NOT NULL,
                      URL     varchar(256) NOT NULL,
                      Comment text);
CREATE TABLE Person (
                        ID        BIGSERIAL NOT NULL,
                        Name      varchar(256) NOT NULL,
                        Surname   varchar(256) NOT NULL,
                        RIN       int8,
                        BirthDate date);
CREATE TABLE LocalAddress (
                              ID          BIGSERIAL NOT NULL,
                              Country     varchar(128),
                              Voivodeship varchar(128),
                              Community   varchar(128),
                              City        varchar(128),
                              Address     text,
                              PostalCode  varchar(15));
CREATE TABLE Role (
                      ID       SERIAL NOT NULL,
                      RoleName varchar(64) NOT NULL UNIQUE);
CREATE TABLE Document (
                          ID           BIGSERIAL NOT NULL,
                          Confirmed    bool NOT NULL,
                          Title        text,
                          StartDate    date,
                          EndDate      date,
                          Description  text,
                          Path         varchar(128),
                          "Date"       int8,
                          Place        int8 NOT NULL,
                          OwnerID      int8 NOT NULL,
                          Type         int4 NOT NULL,
                          Localization int8,
                          PhotoRefers  int8);
CREATE TABLE PersonInDocument (
                                  ID       BIGSERIAL NOT NULL,
                                  PersonID int8 NOT NULL,
                                  Document int8 NOT NULL,
                                  X        float4,
                                  Y        float4,
                                  Comment  text);
CREATE TABLE "date" (
                        DateID BIGSERIAL NOT NULL,
                        Year   int4 NOT NULL,
                        Month  int4,
                        Day    int4);
CREATE TABLE Address (
                         PlaceID     BIGSERIAL NOT NULL,
                         Country     varchar(128),
                         Voivodeship varchar(128),
                         Community   varchar(128),
                         City        varchar(128),
                         Address     text,
                         PostalCode  varchar(15),
                         Longitude   float4,
                         Latitude    float4,
                         Parish      text,
                         Secular     text);
CREATE TABLE Notification (
                              ID          BIGSERIAL NOT NULL,
                              Title       text NOT NULL,
                              Context     text NOT NULL,
                              Displayed   bool NOT NULL,
                              "Date"      date NOT NULL,
                              "User"      int8 NOT NULL,
                              Document    int8 NOT NULL,
                              NewDocument int8);
CREATE TABLE Family (
                        ID  BIGSERIAL NOT NULL,
                        CID int8 NOT NULL,
                        FID int8 NOT NULL,
                        MID int8 NOT NULL);
CREATE TABLE DocumentType (
                              ID       SERIAL NOT NULL,
                              TypeName varchar(64) NOT NULL UNIQUE,
                              Template text NOT NULL);
CREATE TABLE AdditionalFields (
                                  ID         SERIAL NOT NULL,
                                  Document   int8 NOT NULL,
                                  fieldName  varchar(255) NOT NULL,
                                  fieldValue varchar(255));

ALTER TABLE Users ADD PRIMARY KEY (ID);
ALTER TABLE Location ADD PRIMARY KEY (ID);
ALTER TABLE PhysicalLocations ADD PRIMARY KEY (ID);
ALTER TABLE URLS ADD PRIMARY KEY (ID);
ALTER TABLE Person ADD PRIMARY KEY (ID);
ALTER TABLE LocalAddress ADD PRIMARY KEY (ID);
ALTER TABLE Role ADD PRIMARY KEY (ID);
ALTER TABLE Document ADD PRIMARY KEY (ID);
ALTER TABLE PersonInDocument ADD PRIMARY KEY (ID);
ALTER TABLE "date" ADD PRIMARY KEY (DateID);
ALTER TABLE Address ADD PRIMARY KEY (PlaceID);
ALTER TABLE Notification ADD PRIMARY KEY (ID);
ALTER TABLE Family ADD PRIMARY KEY (ID);
ALTER TABLE DocumentType ADD PRIMARY KEY (ID);
ALTER TABLE AdditionalFields ADD PRIMARY KEY (ID);

CREATE INDEX Person_Name
    ON Person (Name);
CREATE INDEX Person_Surname
    ON Person (Surname);

ALTER TABLE URLS DROP CONSTRAINT FKURLS357596;
ALTER TABLE Users DROP CONSTRAINT FKUsers107645;
ALTER TABLE PhysicalLocations DROP CONSTRAINT FKPhysicalLo14733;
ALTER TABLE PhysicalLocations DROP CONSTRAINT FKPhysicalLo420501;
ALTER TABLE Document DROP CONSTRAINT FKDocument599710;
ALTER TABLE Document DROP CONSTRAINT FKDocument318718;
ALTER TABLE PersonInDocument DROP CONSTRAINT FKPersonInDo473623;
ALTER TABLE PersonInDocument DROP CONSTRAINT FKPersonInDo722953;
ALTER TABLE Document DROP CONSTRAINT FKDocument228946;
ALTER TABLE Document DROP CONSTRAINT FKDocument485963;
ALTER TABLE Document DROP CONSTRAINT FKDocument18188;
ALTER TABLE PhysicalLocations DROP CONSTRAINT FKPhysicalLo27241;
ALTER TABLE Notification DROP CONSTRAINT FKNotificati683382;
ALTER TABLE Notification DROP CONSTRAINT FKNotificati290421;
ALTER TABLE Notification DROP CONSTRAINT FKNotificati228421;
ALTER TABLE Family DROP CONSTRAINT FKFamily193583;
ALTER TABLE Family DROP CONSTRAINT FKFamily196466;
ALTER TABLE Family DROP CONSTRAINT FKFamily203193;
ALTER TABLE Document DROP CONSTRAINT FKDocument886774;
ALTER TABLE AdditionalFields DROP CONSTRAINT FKAdditional166539;
ALTER TABLE URLS ADD CONSTRAINT FKURLS357596 FOREIGN KEY (IDURL) REFERENCES Location (IDURL);
ALTER TABLE Users ADD CONSTRAINT FKUsers107645 FOREIGN KEY (IDRole) REFERENCES Role (ID);
ALTER TABLE PhysicalLocations ADD CONSTRAINT FKPhysicalLo14733 FOREIGN KEY (LocalAddressID) REFERENCES LocalAddress (ID);
ALTER TABLE PhysicalLocations ADD CONSTRAINT FKPhysicalLo420501 FOREIGN KEY (IDPhys) REFERENCES Location (IDPhys);
ALTER TABLE Document ADD CONSTRAINT FKDocument599710 FOREIGN KEY (Localization) REFERENCES Location (ID);
ALTER TABLE Document ADD CONSTRAINT FKDocument318718 FOREIGN KEY (OwnerID) REFERENCES Users (ID);
ALTER TABLE PersonInDocument ADD CONSTRAINT FKPersonInDo473623 FOREIGN KEY (PersonID) REFERENCES Person (ID);
ALTER TABLE PersonInDocument ADD CONSTRAINT FKPersonInDo722953 FOREIGN KEY (Document) REFERENCES Document (ID);
ALTER TABLE Document ADD CONSTRAINT FKDocument228946 FOREIGN KEY (PhotoRefers) REFERENCES Document (ID);
ALTER TABLE Document ADD CONSTRAINT FKDocument485963 FOREIGN KEY ("Date") REFERENCES "date" (DateID);
ALTER TABLE Document ADD CONSTRAINT FKDocument18188 FOREIGN KEY (Place) REFERENCES Address (PlaceID);
ALTER TABLE PhysicalLocations ADD CONSTRAINT FKPhysicalLo27241 FOREIGN KEY ("user") REFERENCES Users (ID);
ALTER TABLE Notification ADD CONSTRAINT FKNotificati683382 FOREIGN KEY (Document) REFERENCES Document (ID);
ALTER TABLE Notification ADD CONSTRAINT FKNotificati290421 FOREIGN KEY (NewDocument) REFERENCES Document (ID);
ALTER TABLE Notification ADD CONSTRAINT FKNotificati228421 FOREIGN KEY ("User") REFERENCES Users (ID);
ALTER TABLE Family ADD CONSTRAINT FKFamily193583 FOREIGN KEY (CID) REFERENCES Person (ID);
ALTER TABLE Family ADD CONSTRAINT FKFamily196466 FOREIGN KEY (FID) REFERENCES Person (ID);
ALTER TABLE Family ADD CONSTRAINT FKFamily203193 FOREIGN KEY (MID) REFERENCES Person (ID);
ALTER TABLE Document ADD CONSTRAINT FKDocument886774 FOREIGN KEY (Type) REFERENCES DocumentType (ID);
ALTER TABLE AdditionalFields ADD CONSTRAINT FKAdditional166539 FOREIGN KEY (Document) REFERENCES Document (ID);