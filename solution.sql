CREATE TABLE D(
    c INTEGER, 
    d VARCHAR(64), 
    e INTEGER, 
    PRIMARY KEY(c)
    ); /* persons.cvs - correct*/

CREATE TABLE S(
    j INTEGER,
    r INTEGER,
    a DATE,
    g VARCHAR(64),
    c, 
    PRIMARY KEY (r), 
    FOREIGN KEY(c) REFERENCES D(A_c), 
    FOREIGN KEY(c) REFERENCES D(K_c)
    ); /* language.csv - correct*/

CREATE TABLE F(
    r INTEGER, 
    b INTEGER, 
    f VARCHAR(64), 
    PRIMARY KEY(b,f), 
    FOREIGN KEY(r) REFERENCES S(r)
    ); /*planets.cvs -  correct - Q_r*/

CREATE TABLE B(
    i VARCHAR(64), 
    l VARCHAR(64), 
    PRIMARY KEY(i)
    ); /*heroes.csv - correct*/

CREATE TABLE L(
    c VARCHAR(64), 
    h VARCHAR(64), 
    u, 
    PRIMARY KEY(h), 
    FOREIGN KEY(c) REFERENCES D(c)
    ); /*powers.csv - correct*/

CREATE TABLE T(
    t, 
    k, 
    PRIMARY KEY(t)
    ); /*correct*/

CREATE TABLE R(
    r INTEGER, 
    q VARCHAR(64), 
    p VARCHAR(64), 
    c INTEGER, 
    FOREIGN KEY(r) REFERENCES S(r), 
    FOREIGN KEY(c) REFERENCES D(c)
    ); /*missions.csv - correct*/

CREATE TABLE G(
    i, 
    r, 
    FOREIGN KEY(i) REFERENCES B(i), 
    FOREIGN KEY(r) REFERENCES S(r)
    ); /*correct*/

CREATE TABLE H(
    c, 
    t, 
    r, 
    FOREIGN KEY(r) REFERENCES S(r), 
    FOREIGN KEY(t) REFERENCES T(t), 
    FOREIGN KEY(c) REFERENCES D(c)
    ); /*correct*/

CREATE TABLE C(
    h, 
    i, 
    FOREIGN KEY(h) REFERENCES L(h), 
    FOREIGN KEY(i) REFERENCES B(i)
    ); /*correct*/

CREATE TABLE I(
    c, 
    i, 
    FOREIGN KEY(c) REFERENCES D(c), 
    FOREIGN KEY(i) REFERENCES B(i)
    ); /*correct*/

/*many to many - new table*/
/*many to one - reference the inbetween table