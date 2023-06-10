CREATE DATABASE Concert_Scheduler;

CREATE TABLE Occupation
(
 Tstp TIME NOT NULL, -- The occupied time of the day --
 Kog DATE NOT NULL, -- The occupation date --
 Reason VARCHAR(30), -- For what aim the stage will be busy? --
 ByWho VARCHAR(30) NOT NULL, -- User who is occupying --
 Constraint PK1 Primary Key (Tstp, Kog)
);

CREATE TABLE Client
(
 Username VARCHAR(30) NOT NULL,
 Email VARCHAR(30) NOT NULL,
 Contact_Phone VARCHAR(14),
 Tip VARCHAR(10) NOT NULL CHECK(Tip IN('Client', 'Staff', 'Label')),
 [Password] VARCHAR(200) NOT NULL,
 Constraint PK3 Primary Key (Username)
);

CREATE TABLE SuperCennik
(
 Username VARCHAR(30) NOT NULL,
 Summary INTEGER,
 Constraint PK2 Primary Key (Username),
 Constraint FK1 Foreign Key (Username) REFERENCES Client (Username)
);

DROP TABLE SuperCennik;

SELECT * FROM Occupation ORDER BY Kog, Tstp;

SELECT * FROM Client;

DELETE FROM Occupation;