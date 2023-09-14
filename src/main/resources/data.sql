INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO RESTAURANT (user_id, name, zone_id, offset_time)
VALUES (1, 'restaurant_1', 'Europe/Moscow', 0),
       (1, 'restaurant_2', 'Europe/Moscow', 0),
       (1, 'restaurant_3', 'Europe/Moscow', 0);

INSERT INTO DISH (name, price_in_coins, restaurant_id)
VALUES ('dish_1', 100, 1),
       ('dish_2', 150, 1),
       ('dish_1', 100, 2),
       ('dish_2', 150, 2);

INSERT INTO MENU_ITEM (date, dish_id)
VALUES ('2020-01-29', 1),
       ('2020-01-29', 2),
       ('2020-01-29', 3),
       ('2020-01-29', 4),
       ('2020-01-30', 1),
       ('2020-01-30', 2),
       ('2020-01-30', 3),
       ('2020-01-30', 4);

INSERT INTO VOTE (user_id, restaurant_id, date)
VALUES (1, 1, '2020-01-29'),
       (2, 1, '2020-01-29'),
       (1, 2, '2020-01-30'),
       (2, 2, '2020-01-30');
