DROP TABLE IF EXISTS Member;

create table Member
(
    id integer not null,
    name varchar(255) not null,
    primary key (id)
);

DROP TABLE IF EXISTS SHORT_URL;

CREATE TABLE SHORT_URL (
                           ID INTEGER AUTO_INCREMENT PRIMARY KEY,
                           ORI_URL VARCHAR(2048) NOT NULL,
                           SHORT_URL VARCHAR(8) NOT NULL UNIQUE,
                           CREATED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS URL_ACCESS_LOG;

CREATE TABLE URL_ACCESS_LOG (
                                ID INTEGER AUTO_INCREMENT PRIMARY KEY,
                                SHORT_URL VARCHAR(8) NOT NULL,
                                ACCESSED_AT TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
