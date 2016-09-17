# Users schema
 
# --- !Ups

CREATE TABLE users (
    id long NOT NULL,
    name varchar,
    isActive varchar
);
 
# --- !Downs
 
DROP TABLE user;