CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    email varchar(128) unique not null,
    first_name varchar(64),
    last_name varchar(64),

    version bigint default 0,
    created timestamp not null default now(),
    modified timestamp
);