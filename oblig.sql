DROP SCHEMA IF EXISTS oblig3 CASCADE;
CREATE SCHEMA oblig3;
SET search_path TO oblig3;

CREATE TABLE ansatt (
  ansatt_id SERIAL PRIMARY KEY,
  brukernavn VARCHAR(10) UNIQUE NOT NULL,
  fornavn VARCHAR(50) NOT NULL,
  etternavn VARCHAR(50) NOT NULL,
  dato_for_ansettelse DATE NOT NULL,
  stilling VARCHAR(50) NOT NULL,
  manedslonn NUMERIC NOT NULL
);

CREATE TABLE avdeling (
  avdeling_id SERIAL PRIMARY KEY,
  navn VARCHAR(50) NOT NULL
);

ALTER TABLE ansatt
ADD COLUMN avdeling_id INTEGER REFERENCES avdeling(avdeling_id);

ALTER TABLE avdeling 
ADD COLUMN sjef_id INTEGER REFERENCES ansatt(ansatt_id); 

INSERT INTO avdeling (navn)
VALUES
('Utvikling'),
('Databaser'),
('Markedsføring');

INSERT INTO ansatt (brukernavn, fornavn, etternavn, dato_for_ansettelse, stilling, manedslonn, avdeling_id)
VALUES
('abc', 'Anne', 'Bakken', '2021-01-01', 'Systemutvikler', 50000, 1),
('def', 'Bjørn', 'Dahl', '2022-04-02', 'Databaseutvikler', 55000, 2),
('ghi', 'Camilla', 'Eriksen', '2023-03-05', 'Prosjektleder', 60000, 3),
('jkl', 'David', 'Foss', '2020-12-01', 'Systemutvikler', 49000, 1),
('mno', 'Emma', 'Gundersen', '2022-05-01', 'Databaseadministrator', 60000, 2),
('pqr', 'Fredrik', 'Hansen', '2023-04-01', 'Prosjektleder', 65000, 3),
('stu', 'Gina', 'Iversen', '2021-06-01', 'Systemutvikler', 47000, 1),
('vwx', 'Håkon', 'Jensen', '2022-07-01', 'Databaseutvikler', 56000, 2),
('yzæ', 'Ingrid', 'Karlsen', '2023-08-01', 'Prosjektleder', 62000, 3),
('øåa', 'Jan', 'Larsen', '2021-02-01', 'Systemutvikler', 48000, 1);

UPDATE avdeling SET sjef_id = ansatt_id FROM ansatt WHERE avdeling.avdeling_id = ansatt.avdeling_id;
ALTER TABLE ansatt ADD COLUMN ersjef BOOLEAN NOT NULL DEFAULT FALSE;

UPDATE ansatt
SET ersjef = TRUE
WHERE brukernavn IN ('vwx', 'yzæ', 'øåa');

