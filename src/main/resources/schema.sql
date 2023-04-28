CREATE TABLE IF NOT EXISTS mpa (
    mpa_id              INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name 		        VARCHAR(10) NOT NULL UNIQUE

);

CREATE TABLE IF NOT EXISTS films (
    film_id             INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name 		        VARCHAR(40) NOT NULL CHECK (name <> ''),
    description 	    VARCHAR(200) NOT NULL,
    releaseDate 	    DATE NOT NULL CHECK (releaseDate > '1895-09-28'),
    duration 	        INTEGER NOT NULL CHECK (duration > 0),
    mpa_id  	        INTEGER REFERENCES mpa (mpa_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id            INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name 		        VARCHAR(40) NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS film_uindex
    ON films (name);

CREATE TABLE IF NOT EXISTS genre_list (
    film_id             INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    genre_id            INTEGER REFERENCES genre (genre_id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS users (
    user_id             INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email 		        VARCHAR(40) NOT NULL CHECK (email <> ''),
    name 		        VARCHAR(40),
    login 		        VARCHAR(40) NOT NULL CHECK (name <> ''),
    birthday            DATE NOT NULL CHECK (birthday < CURRENT_DATE),
    CONSTRAINT constraint_user UNIQUE (email, login)
);

CREATE TABLE IF NOT EXISTS likes (
    film_id             INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    user_id             INTEGER REFERENCES users (user_id) ON DELETE RESTRICT
);

CREATE UNIQUE INDEX IF NOT EXISTS user_uindex
    ON users (email, login);

CREATE TABLE IF NOT EXISTS friendship (
    first_user_id       INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    second_user_id      INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    status              BOOLEAN DEFAULT FALSE
); 