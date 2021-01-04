DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM users;
DELETE FROM restaurants;
DELETE FROM dishes;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@gmail.com', '{noop}user'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO restaurants (name)
VALUES ('Hide'),
       ('Palkin'),
       ('Moskovskiy');

INSERT INTO votes (user_id, restaurant_id, localdate)
VALUES (100000, 100002, '2020-12-10'),
       (100000, 100002, '2020-12-11'),
       (100000, 100004, '2020-12-12'),
       (100001, 100003, '2020-12-10'),
       (100001, 100003, '2020-12-11'),
       (100001, 100005, '2020-12-12'),
       (100001, 100002, now());

INSERT INTO dishes (restaurant_id, name, price, localdate)
VALUES (100002, 'Risotto', 500, '2020-12-10'),
       (100002, 'Borsch', 300, '2020-12-10'),
       (100002, 'Cheesecake', 350, '2020-12-10'),
       (100002, 'Bolognese', 250, '2020-12-11'),
       (100002, 'Chicken Noodles', 150, '2020-12-11'),
       (100002, 'Secret from chief', 500, '2020-12-11'),
       (100002, 'Cheese plate', 999, now()),
       (100003, 'Fruit plate', 1200, '2020-12-11'),
       (100003, 'Salmon roll', 700, '2020-12-11'),
       (100003, 'California Roll', 1000, '2020-12-12'),
       (100003, 'Octopulus Icecream', 1200, '2020-12-12'),
       (100003, 'Peach salad', 700, '2020-12-12'),
       (100003, 'Cashue', 1000, now()),
       (100003, 'Meatballs', 1200, now()),
       (100003, 'Salmon roll', 700, now()),
       (100004, 'Capers', 10000, '2020-12-10'),
       (100004, 'Fugu Fish', 50000, '2020-12-11'),
       (100004, 'Shark meat', 100000, now());