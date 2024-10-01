-- Accedi a PostgreSQL come utente postgres
psql -U postgres

-- Crea un nuovo database
CREATE DATABASE authdb;

-- Connettiti al nuovo database
\c authdb

-- Crea la tabella utenti
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);
