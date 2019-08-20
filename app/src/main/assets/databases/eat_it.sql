--
-- Fichier généré par SQLiteStudio v3.2.1 sur lun. août 19 18:01:09 2019
--
-- Encodage texte utilisé : UTF-8
--
PRAGMA foreign_keys = off;
BEGIN TRANSACTION;

-- Table : OrderFood
CREATE TABLE OrderFood (id INTEGER PRIMARY KEY NOT NULL, foodId VARCHAR (255) NOT NULL, foodNom VARCHAR (255) NOT NULL, qte VARCHAR (255) NOT NULL, prix VARCHAR (255), reduction VARCHAR (255) NOT NULL);

COMMIT TRANSACTION;
PRAGMA foreign_keys = on;
