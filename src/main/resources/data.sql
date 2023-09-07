CREATE TABLE users(
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(30) NOT NULL,
    password VARCHAR(100) NOT NULL,
    created DATE,
    modified DATE,
    last_login DATE,
    token VARCHAR(500) NOT NULL,
    is_active BOOLEAN
);

CREATE TABLE phone(
    id bigint auto_increment,
    number VARCHAR(20) NOT NULL,
    citycode VARCHAR(2) NOT NULL,
    countrycode VARCHAR(2) NOT NULL,
    USER_ID UUID NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (USER_ID) REFERENCES users(id)
);
