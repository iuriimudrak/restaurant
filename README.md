[![Codacy Badge](https://app.codacy.com/project/badge/Grade/394c782d44424a549c07af881f485abc)](https://www.codacy.com/gh/iuriimudrak/restaurant/dashboard)
[![Build Status](https://travis-ci.com/iuriimudrak/restaurant.svg?branch=main)](https://travis-ci.com/github/iuriimudrak/restaurant)

# Restaurant REST API
##### [Graduation project](https://github.com/JavaOPs/topjava/blob/master/graduation.md) for TopJava intership
**********

## The task is:

Build a voting system for deciding where to have lunch.

* 2 types of users: admin and regular users
* Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
* Menu changes each day (admins do the updates)
* Users can vote on which restaurant they want to have lunch at
* Only one vote counted per user
* If user votes again the same day:
   * If it is before 11:00 we assume that he changed his mind.
   * If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides a new menu each day.

> Design and implement a REST API using Hibernate/Spring/SpringMVC (or Spring-Boot) without frontend.

**********

## REST API
The application was deployed and testing by using:
* **Application context** (as same as artifactId): `/restaurant-rest-api`
* **HSQLDB** (it's deployed automatically)

The API was tested by Postman and Bash.

===========================================================

### An unauthorized person
Each unauthorized person can register and get list of restaurants with dishes.

#### Register
```shell
curl -s -X POST -d '{"name":"newName","email":"email@gmail.com","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/profile/register
```
Result:
```json
{"id":100031,"name":"newName","email":"email@gmail.com","enabled":true,"registered":"2021-01-08T00:03:02.161+00:00","roles":["USER"]}
```

#### Get list of restaurants (with dishes)
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/restaurants'
```
Result:
```json
[{"id":100002,"name":"Hide","enabled":true,"registered":"2021-01-08T20:14:23.746+00:00","votes":null,"dishes":[{"id":100019,"name":"Cheese plate","price":999,"localDate":"2021-01-08"}]},{"id":100003,"name":"Palkin","enabled":true,"registered":"2021-01-08T20:14:23.746+00:00","votes":null,"dishes":[{"id":100025,"name":"Cashue","price":1000,"localDate":"2021-01-08"},{"id":100026,"name":"Meatballs","price":1200,"localDate":"2021-01-08"},{"id":100027,"name":"Salmon roll","price":700,"localDate":"2021-01-08"}]},{"id":100004,"name":"Moskovskiy","enabled":true,"registered":"2021-01-08T20:14:23.746+00:00","votes":null,"dishes":[{"id":100030,"name":"Shark meat","price":100000,"localDate":"2021-01-08"}]}]
```
**********
### Regular user
#### Get profile
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/profile' --user user@gmail.com:user
```
Result:
```json
{"id":100000,"name":"User","email":"user@gmail.com","enabled":true,"registered":"2021-01-08T09:20:56.540+00:00","roles":["USER"],"votes":null}
```

#### Get profile (with votes)
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/profile/with-votes' --user user@gmail.com:user
```
Result:
```json
{"id":100000,"name":"User","email":"user@gmail.com","enabled":true,"registered":"2021-01-08T09:20:56.540+00:00","roles":["USER"],"votes":[{"id":100008,"localDate":"2020-12-12"},{"id":100007,"localDate":"2020-12-11"},{"id":100006,"localDate":"2020-12-10"}]}
```

#### Update profile
```shell
curl -s -X PUT -d '{"name":"newName","email":"newemail@gmail.com","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/profile/ --user user@gmail.com:user
```
Result:

New user's data was set. It can be checked by `Get profile` command.

#### Delete profile
```shell
curl -s -X DELETE 'http://localhost:8080/restaurant-rest-api/profile' --user user@gmail.com:user
```

#### Get all user's votes (history)
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/profile/votes/' --user user@gmail.com:user
```
Result:
```json
[{"id":100008,"localDate":"2020-12-12"},{"id":100007,"localDate":"2020-12-11"},{"id":100006,"localDate":"2020-12-10"}]
```

#### Get vote by date
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/profile/votes/by?date=2020-12-11' --user user@gmail.com:user
```
Result:
```json
{"id":100007,"localDate":"2020-12-11"}
```

**********

### Admin
#### Create new user
```shell
curl -s -X POST -d '{"name":"newName","email":"newemail@gmail.com","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/admin/users --user admin@gmail.com:admin
```
Result:
```json
{"id":100031,"name":"newName","email":"newemail@gmail.com","enabled":true,"registered":"2021-01-08T12:00:43.243+00:00"}
```

#### Update user
```shell
curl -s -X PUT -d '{"name":"newName","email":"newemail@gmail.com","password":"newPassword"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/admin/users/100000 --user admin@gmail.com:admin
```
Result:

New user's data was set. It can be checked by `Get a single user` command.

#### Delete user
```shell
curl -s -X DELETE 'http://localhost:8080/restaurant-rest-api/admin/users/100000' --user admin@gmail.com:admin
```

#### Switch enable/disable user
Set `enabled` parameter by using `true` or `false` for switching enable/disable option

```shell
curl -s -X PATCH 'http://localhost:8080/restaurant-rest-api/admin/users/100000?enabled=false' --user admin@gmail.com:admin
```
Result:

User was disabled. It can be checked by `Get a single user` command.

#### Get all users
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/users' --user admin@gmail.com:admin
```
Result:
```json
[{"id":100001,"name":"Admin","email":"admin@gmail.com","enabled":true,"registered":"2021-01-08T12:39:01.203+00:00","roles":["ADMIN"],"votes":null},{"id":100000,"name":"User","email":"user@gmail.com","enabled":true,"registered":"2021-01-08T12:39:01.203+00:00","roles":["USER"],"votes":null}]
```

#### Get a single user
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/users/100000' --user admin@gmail.com:admin
```
Result:
```json
{"id":100000,"name":"User","email":"user@gmail.com","enabled":true,"registered":"2021-01-08T12:10:35.408+00:00","roles":["USER"],"votes":null}
```

#### Get user by Email
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/users/by?email=user@gmail.com' --user admin@gmail.com:admin
```
Result:
```json
{"id":100000,"name":"User","email":"user@gmail.com","enabled":true,"registered":"2021-01-08T12:10:35.408+00:00","roles":["USER"],"votes":null}
```

#### Get user with votes
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/users/100000/with-votes' --user admin@gmail.com:admin
```
Result:
```json
{"id":100000,"name":"User","email":"user@gmail.com","enabled":true,"registered":"2021-01-08T12:10:35.408+00:00","roles":["USER"],"votes":[{"id":100008,"localDate":"2020-12-12"},{"id":100007,"localDate":"2020-12-11"},{"id":100006,"localDate":"2020-12-10"}]}
```

#### Get user's votes (history)
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/users/100000/votes/' --user admin@gmail.com:admin
```
Result:
```json
[{"id":100008,"localDate":"2020-12-12"},{"id":100007,"localDate":"2020-12-11"},{"id":100006,"localDate":"2020-12-10"}]
```

#### Get user's vote by date
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/users/100000/votes/by?date=2020-12-11' --user admin@gmail.com:admin
```
Result:
```json
{"id":100007,"localDate":"2020-12-11"}
```

===========================================================
#### Create new restaurant
```shell
curl -s -X POST -d '{"name":"NewRestaurant"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/admin/restaurants/ --user admin@gmail.com:admin
```
Result:
```json
{"id":100031,"name":"NewRestaurant","enabled":true,"registered":"2021-01-08T13:00:39.856+00:00"}     
```

#### Upgrade restaurant
```shell
curl -s -X PUT -d '{"id":100002,"name":"UpdatedName","enabled":true}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/admin/restaurants/100002 --user admin@gmail.com:admin

```
Result:

New restaurant's data was set. It can be checked by `Get restaurant` command.

#### Delete restaurant
```shell
curl -s -X DELETE 'http://localhost:8080/restaurant-rest-api/admin/restaurants/100002' --user admin@gmail.com:admin
```
Result:

Entity was deleted. It can be checked by `Get restaurant` command.

#### Switch enable/disable restaurant
Set `enabled` parameter by using `true` or `false` for switching enable/disable option
```shell
curl -s -X PATCH 'http://localhost:8080/restaurant-rest-api/admin/restaurants/100002?enabled=false' --user admin@gmail.com:admin
```
Result:

Restaurant was disabled. It can be checked by `Get restaurant` command.

#### Get restaurant
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/restaurants/100003' --user admin@gmail.com:admin
```
Result:
```json
{"id":100003,"name":"Palkin","enabled":true,"registered":"2021-01-08T13:17:20.799+00:00","votes":null,"dishes":null}
```

===========================================================
#### Create new dish
```shell
curl -s -X POST -d '{"name":"New","price":1000}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/admin/restaurants/100002/dishes --user admin@gmail.com:admin
```
Result:
```json
{"id":100031,"name":"New","price":1000,"localDate":"2021-01-08"}
```

#### Update dish
```shell
curl -s -X PUT -d '{"id":100013,"localDate":"2020-12-10","name":"UpdatedName","price":500}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant-rest-api/admin/restaurants/100002/dishes/100013 --user admin@gmail.com:admin
```
Result:

New dish's data was set. It can be checked by `Get dish` command.

#### Delete dish
```shell
curl -s -X DELETE 'http://localhost:8080/restaurant-rest-api/admin/restaurants/100002/dishes/100013' --user admin@gmail.com:admin
```
Result:

Entity was deleted. It can be checked by `Get dish` command.

#### Get all dishes for restaurant
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/restaurants/100002/dishes/' --user admin@gmail.com:admin
```
Result:
```json
[{"id":100019,"name":"Cheese plate","price":999,"localDate":"2021-01-08"},{"id":100016,"name":"Bolognese","price":250,"localDate":"2020-12-11"},{"id":100017,"name":"Chicken Noodles","price":150,"localDate":"2020-12-11"},{"id":100018,"name":"Secret from chief","price":500,"localDate":"2020-12-11"},{"id":100014,"name":"Borsch","price":300,"localDate":"2020-12-10"},{"id":100015,"name":"Cheesecake","price":350,"localDate":"2020-12-10"},{"id":100013,"name":"Risotto","price":500,"localDate":"2020-12-10"}]
```

#### Get a single dish for the restaurant
```shell
curl -s 'http://localhost:8080/restaurant-rest-api/admin/restaurants/100002/dishes/100016' --user admin@gmail.com:admin
```
Result:
```json
{"id":100016,"name":"Bolognese","price":250,"localDate":"2020-12-11"}
```
