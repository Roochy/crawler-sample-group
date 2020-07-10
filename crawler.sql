CREATE TABLE info_type
(
    id bigint(8) PRIMARY KEY NOT NULL,
    `desc` varchar(255) NOT NULL
);
CREATE UNIQUE INDEX info_type_desc_uindex ON info_type (`desc`);

CREATE TABLE info
(
    id bigint(8) PRIMARY KEY,
    title varchar(255) NOT NULL,
    content mediumtext NOT NULL,
    tags varchar(255) NOT NULL,
    author varchar(255) NOT NULL,
    source varchar(255) NOT NULL,
    publish_time varchar(255) NOT NULL
);