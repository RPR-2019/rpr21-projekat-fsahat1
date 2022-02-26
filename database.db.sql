BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "voters" (
	"id"	INTEGER,
	"name"	TEXT,
	"soc_number" INTEGER,
	"vote_status"	INTEGER,
	PRIMARY KEY("id")
);
INSERT INTO voters VALUES("1","Tutan Kamon","12345","0");
INSERT INTO voters VALUES("2","Demo Kratovic","12234","2","0");
CREATE TABLE IF NOT EXISTS "parties" (
	"id"	INTEGER,
	"name"	TEXT,
	"party_leader"	TEXT,
	PRIMARY KEY("id")
);

INSERT INTO parties VALUES("1","Stranka razularene mase","Divljo Nesagovornicic");
INSERT INTO parties VALUES("2","Ispod stola","Hapo Mazijalic");
INSERT INTO parties VALUES("3","Europski put","Hans Jans Centrotrans");

CREATE TABLE IF NOT EXISTS "waitlist"(
    "id"	INTEGER,
    "name"	TEXT,
    "party_leader"	TEXT,
    PRIMARY KEY("id")
)

INSERT INTO waitlist VALUES("1","Partija Nidje Nas Nema", "Sabir Nestasica");
INSERT INTO waitlist VALUES("2","Podrska Programerima Freelancerima", "Demetra Dvoklik");
INSERT INTO waitlist VALUES("3","Stranka politicki neobrazovanih", "Dok se snadjemo pa javimo");
COMMIT;