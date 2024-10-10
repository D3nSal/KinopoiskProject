create table films
(
    id          int auto_increment primary key,
    film_id     int,
    film_name   varchar(255),
    year_from   int,
    rating      float,
    description varchar(2048),
    type        varchar(255),
    votes       int
);

create table countries
(
    id         int auto_increment primary key,
    film       int references films (id) on delete set null,
    country_id int,
    country    varchar(255)
);

create table genres
(
    id       int auto_increment primary key,
    film     int references films (id) on delete set null,
    genre_id int,
    genre    varchar(255)
);