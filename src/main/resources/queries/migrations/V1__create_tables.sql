CREATE TABLE tags_tags
(
    id    INTEGER,
    owner CHAR(36),

    PRIMARY KEY (id, owner)
);

CREATE TABLE tags_users
(
    uuid    CHAR(36),

    PRIMARY KEY (uuid)
);
