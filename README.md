# java-filmorate
Template repository for Filmorate project.

# ЕР диаграмма filmorate

<img src = "src/main/resources/filmorateER.png" width="" height = "">

[Ссылка на диаграмму](src/main/resources/filmorateER.png)\
[Ссылка на диаграмму в редакторе диаграмм](https://dbdiagram.io/d/6436b0d58615191cfa8d5bbd)

## Описание ER диаграммы filmorate:

Таблица film с внешним ключом film_id, по которому она связана с таблицей genre_list, так же содержащей поле genre_id.
Имена жанров располагаются в таблице genre, genre_id является внешним ключом. И так же связана с таблицей rating, в 
которой есть три колонки: rating_id (внешний ключ), name_rating description (текстовое описание рейтинга). 

Таблица user с внешним ключом user_id. В Таблице likes имеются поля film_id и user_id, по которой мы можем определить
какие пользователи какому фильму ставили лайки. Чтобы определить, какой из пользователей какому является другом, есть
таблица request_list содержит три поля: first_user_id (user отправляющий запрос на добавление в друзья), second_user_id
и состояние запроса принято/не принято. 

## Примеры запросов

### Film

#### Получение всех фильмов

```
SELECT title FROM film;
```

#### Выдать фильм по id

```
SELECT title FROM film
WHERE film id = 'somenumber'
```

#### Добавить фильм

```
INSERT INTO film (film_id, name, description, releaseDate, duration, rating_id)
VALUES ( 'aaa', 'bbb', 'yyyy.mm.dd', 'ccc', x); 
```

#### Обновить фильм

```
INSERT INTO film (name, description, releaseDate, duration, rating_id)
VALUES (q, 'aaa', 'bbb', 'yyyy.mm.dd', 'ccc', x); 
ON CONFLICT (q) DO UPDATE SET name = EXCLUDED.name, 
                              description = EXCLUDED.description, 
                              releaseDate = EXCLUDED.releaseDate, 
                              duration = EXCLUDED.duration, 
                              rating_id = EXCLUDED.rating_id; 
```

#### Удалить фильм

```
DELETE FROM film WHERE id = q; 
```

#### Выдать топ count фильмов

```
SELECT f.title, count(1.user_id) AS count
FROM film AS f
LEFT JOIN likes AS 1 ON f.film_ id = 1.film_id
GROUP BY f.title
ORDER BY count DESK;
```
### User

#### Получение списка всех пользователей

```
SELECT name FROM users;
```

#### Добавление пользователя

```
INSERT INTO user (email, name, login, birthday)
VALUES ( 'aaa', 'bbb', 'ccc', 'yyyy.mm.dd'); 
```

#### Обновление пользователя

```
INSERT INTO user (user_id, email, name, login, birthday)
VALUES (q, 'aaa', 'bbb', 'ccc', 'yyyy.mm.dd'); 
ON CONFLICT (q) DO UPDATE SET email = EXCLUDED.email, 
                              name = EXCLUDED.name, 
                              login = EXCLUDED.login, 
                              birthday = EXCLUDED.birthday; 

```

#### Удаление пользователя

```
DELETE FROM users WHERE id = q; 
```

#### Найти пользователя по id

```
SELECT name FROM users
WHERE user id = 'somenumber';
```

#### Список друзей пользователя

```
SELECT name 
FROM users 
WHERE users_id =  (SELECT id 
		  FROM (
			SELECT second_user_id
		  	FROM fiendship
		  	WHERE first_user_id ='somenumber" AND status = 'confirm'
		  UNION 
			SELECT first_user_id
		  	FROM fiendship
		  	WHERE second_user_id ='somenumber' AND status = 'confirm);
```

#### Общий список друзей пользователя

```
SELECT name 
FROM users 

WHERE users_id = (SELECT id 
		  FROM (
			SELECT second_user_id
		  	FROM fiendship
		  	WHERE first_user_id ='somenumber" AND status = 'confirm'
		  UNION 
			SELECT first_user_id
		  	FROM fiendship
		  	WHERE second_user_id ='somenumber' AND status = 'confirm) AS t1
		  INNER JOIN (
			SELECT second_user_id
			FROM fiendship
		  	WHERE first_user_id ='somenumber_2" AND status = 'confirm'
		  UNION 
			SELECT first_user_id
		  	FROM fiendship
		  	WHERE second_user_id ='somenumber_2' AND status = 'confirm) AS t2
		  ON t1.second_user_id = t2.second_user_id); 
```