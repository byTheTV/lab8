DROP TABLE IF EXISTS study_groups CASCADE;
DROP TABLE IF EXISTS persons CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS locations CASCADE;

DROP SEQUENCE IF EXISTS study_group_id_seq;

DROP TYPE IF EXISTS form_of_education_enum CASCADE;
DROP TYPE IF EXISTS color_enum CASCADE;

CREATE TYPE form_of_education_enum AS ENUM (
    'DISTANCE_EDUCATION',
    'FULL_TIME_EDUCATION',
    'EVENING_CLASSES'
);

CREATE TYPE color_enum AS ENUM (
    'GREEN',
    'RED',
    'YELLOW',
    'WHITE'
);

CREATE TABLE locations (
   id SERIAL PRIMARY KEY,
   x FLOAT NOT NULL,
   y FLOAT NOT NULL,
   z FLOAT NOT NULL
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(40) NOT NULL
);

CREATE TABLE persons (
     id SERIAL PRIMARY KEY,
     name VARCHAR(255) NOT NULL,
     birthday DATE, -- Может быть NULL, тип DATE вместо TIMESTAMP
     passport_id VARCHAR(26) UNIQUE, -- Уникальное, длина до 26
     eye_color color_enum NOT NULL,
     location_id INTEGER NOT NULL REFERENCES locations(id)
);

CREATE SEQUENCE study_group_id_seq START 1;

CREATE TABLE study_groups (
    id INTEGER PRIMARY KEY DEFAULT nextval('study_group_id_seq'),
    name VARCHAR(255) NOT NULL,
    coordinates_x BIGINT NOT NULL,
    coordinates_y INTEGER NOT NULL,
    creation_date DATE NOT NULL DEFAULT CURRENT_DATE,
    students_count BIGINT NOT NULL CHECK (students_count > 0),
    expelled_students INTEGER CHECK (expelled_students IS NULL OR expelled_students > 0),
    transferred_students INTEGER CHECK (transferred_students IS NULL OR transferred_students > 0),
    form_of_education form_of_education_enum NOT NULL,
    group_admin_id INTEGER NOT NULL REFERENCES persons(id),
    user_id INTEGER NOT NULL REFERENCES users(id)
);