# Create the schema if necessary.
CREATE SCHEMA IF NOT EXISTS SkiAPI;
USE SkiAPI;

# Drop tables if necessary.
DROP TABLE IF EXISTS Seasons;
DROP TABLE IF EXISTS Resorts;
DROP TABLE IF EXISTS LiftRides;

# Create tables if necessary.
CREATE TABLE Resorts (
ResortId	INT UNSIGNED NOT NULL AUTO_INCREMENT,
ResortName VARCHAR(255) NOT NULL,
CONSTRAINT pk_Resorts_ResortId 
  PRIMARY KEY (ResortId)
);

CREATE TABLE Seasons (
SeasonId INT UNSIGNED NOT NULL AUTO_INCREMENT,
Season INT NOT NULL,
ResortId INT UNSIGNED,
CONSTRAINT pk_Seasons_SeasonId
  PRIMARY KEY (SeasonId),
CONSTRAINT fk_Seasons_Resorts_ResortId
  FOREIGN KEY (ResortId)
  REFERENCES Resorts (ResortId)
  ON UPDATE CASCADE ON DELETE CASCADE,
CONSTRAINT unique_resort_season UNIQUE (ResortId, Season)# row updated/deleted if data in parent table updated/deleted
);

CREATE TABLE LiftRides (
ResortId	INT  NOT NULL,
SeasonId INT NOT NULL,
DayId INT NOT NULL,
SkierId INT NOT NULL,
StartTime INT NOT NULL,
LiftId INT NOT NULL,
Vertical INT NOT NULL,
PRIMARY KEY (ResortId, SeasonId, DayId, SkierId, StartTime)
);
