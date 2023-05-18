MERGE INTO GENRE (GENRE_ID, NAME)
       VALUES (1, 'Комедия'),
              (2, 'Драма'),
              (3, 'Мультфильм'),
              (4, 'Триллер'),
              (5, 'Документальный'),
              (6, 'Боевик');


MERGE INTO MPA (MPA_ID, NAME)
       VALUES (1, 'G'),
              (2, 'PG'),
              (3, 'PG-13'),
              (4, 'R'),
              (5, 'NC-17');

--
-- INSERT INTO GENRE (NAME) VALUES ('Комедия');
-- INSERT INTO GENRE (NAME) VALUES ('Драма');
-- INSERT INTO GENRE (NAME) VALUES ('Мультфильм');
-- INSERT INTO GENRE (NAME) VALUES ('Триллер');
-- INSERT INTO GENRE (NAME) VALUES ('Документальный');
-- INSERT INTO GENRE (NAME) VALUES ('Боевик');
--
-- INSERT INTO MPA (NAME) VALUES ('G');
-- INSERT INTO MPA (NAME) VALUES ('PG');
-- INSERT INTO MPA (NAME) VALUES ('PG-13');
-- INSERT INTO MPA (NAME) VALUES ('R');
-- INSERT INTO MPA (NAME) VALUES ('NC-17');

