# java-filmorate
Template repository for Filmorate project.

![DB schema diagram (under construction).](https://github.com/IHukumka/java-filmorate/blob/database/DB%20schema.png)

База данных для проекта filmorate.
Хранение данных для сборки объектов классов:

User
- Integer id = user.id integer PRIMARY KEY,
- String name = user.name varchar,
- String login = user.login varchar,
- String email = user.email varchar,
- LocalDate birthday = user.birthday date,
- HashSet<Integer> friends =
                            SELECT friend_id
                            FROM user_friends
                            WHERE user_id = {id} AND confirmed = true),
- HashSet<Integer> requests =
                            SELECT friend_id
                            FROM user_friends
                            WHERE user_id = {id} AND confirmed = false);

Film
- Integer id = film.id integer PRIMARY KEY,
- String name = film.name varchar,
- String description = film.description varchar(200),
- LocalDate release_date = release_date date,
- Integer duration = duration integer,
- Integer rating_id = rating_id integer,
- Integer likes =
                  select count(ul.user_id)
                  from user_likes as ul
                  inner join film as f on ul.film_id = f.id
                  where f.id = {id};,
- HashSet<Integer> userLikes =
                              select user_id
                              from user_likes
                              where film_id = {1};,
- String rating =
                  select r.name
                  from rating as r
                  right join film as f on f.rating_id = r.id
                  where f.id = 1;,
