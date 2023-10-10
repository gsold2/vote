INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (user_id, name)
VALUES (2, 'restaurant_1'),
       (2, 'restaurant_2');

INSERT INTO DISH (name, price, restaurant_id)
VALUES ('dish_1', 100, 1),
       ('dish_2', 150, 1),
       ('dish_3', 200, 1),
       ('dish_1', 100, 2),
       ('dish_2', 150, 2),
       ('dish_3', 200, 2);

INSERT INTO MENU_ITEM (date, dish_id)
VALUES ('2020-01-29', 1),
       ('2020-01-29', 2),
       ('2020-01-29', 3),
       ('2020-01-30', 1);

INSERT INTO VOTE (user_id, restaurant_id, date)
VALUES (1, 1, '2020-01-30'),
       (2, 1, '2020-01-30'),
       (2, 1, now());
