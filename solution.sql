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
    c_A INTEGER,
    c_K INTEGER, 
    PRIMARY KEY(r), 
    FOREIGN KEY(c_A) REFERENCES D(c), 
    FOREIGN KEY(c_K) REFERENCES D(c)
    ); /* language.csv - correct*/

CREATE TABLE F(
    r INTEGER, 
    b INTEGER, 
    f VARCHAR(64), 
    PRIMARY KEY(b,f), 
    FOREIGN KEY(r) REFERENCES S(r)
    ); /*planets.cvs -  correct - check for Q_r*/

CREATE TABLE B(
    i VARCHAR(64), 
    l VARCHAR(64), 
    PRIMARY KEY(i)
    ); /*heroes.csv - correct*/

CREATE TABLE L(
    c VARCHAR(64), 
    h VARCHAR(64), 
    u TEXT, 
    PRIMARY KEY(h), 
    FOREIGN KEY(c) REFERENCES D(c)
    ); /*powers.csv - correct*/

CREATE TABLE R(
    r INTEGER, 
    q VARCHAR(64), 
    p VARCHAR(64), 
    c INTEGER,
    PRIMARY KEY(r,c),
    FOREIGN KEY(r) REFERENCES S(r), 
    FOREIGN KEY(c) REFERENCES D(c)
    ); /*missions.csv - correct*/

CREATE TABLE T(
    t VARCHAR(64), 
    k VARCHAR(64), 
    PRIMARY KEY(t)
    ); /*correct check if its actually varchar*/

CREATE TABLE G(
    i VARCHAR(64), 
    r INTEGER,
    PRIMARY KEY(i,r),
    FOREIGN KEY(i) REFERENCES B(i), 
    FOREIGN KEY(r) REFERENCES S(r)
    ); /*correct - check for primary keys, put both i and r together as one*/

CREATE TABLE H(
    c INTEGER, 
    t VARCHAR(64), 
    r INTEGER,
    PRIMARY KEY(c,t,r),
    FOREIGN KEY(c) REFERENCES D(c),
    FOREIGN KEY(t) REFERENCES T(t),
    FOREIGN KEY(r) REFERENCES S(r)
    ); /*correct*/

CREATE TABLE C(
    h VARCHAR(64), 
    i VARCHAR(64),
    PRIMARY KEY(h,i),
    FOREIGN KEY(h) REFERENCES L(h), 
    FOREIGN KEY(i) REFERENCES B(i)
    ); /*correct*/

CREATE TABLE I(
    c INTEGER, 
    i VARCHAR(64),
    PRIMARY KEY(c,i),
    FOREIGN KEY(c) REFERENCES D(c), 
    FOREIGN KEY(i) REFERENCES B(i)
    ); /*correct*/

/*many to many - new table*/
/*many to one - reference the inbetween table*/

/*INSERT STATEMENTS*/

INSERT INTO D(c,d,e) VALUES (11,'Hyrakius',NULL);
INSERT INTO D(c,d,e) VALUES (2,'Bgztl',8);
INSERT INTO D(c,d,e) VALUES (17,'Rimbor',30);
INSERT INTO D(c,d,e) VALUES (13,'Krypton',0);
INSERT INTO D(c,d,e) VALUES (6,'Colu',30);

INSERT INTO S(j,r,a,g) VALUES (22,14,2000-01-01,'');
INSERT INTO S(j,r,a,g) VALUES (22,29,2000-01-01,'Brin Londo ');
INSERT INTO S(j,r,a,g) VALUES (22,34,2000-01-01,'Mysa Nal ');
INSERT INTO S(j,r,a,g) VALUES (22,26,2000-01-01,"Projectra Wind'zzor ");
INSERT INTO S(j,r,a,g) VALUES (22,19,2000-01-01,'Lar Gand ');

INSERT INTO F(r,b,f) VALUES (14,49,'Telescopic/Microscopic Vision');
INSERT INTO F(r,b,f) VALUES (33,102,'Superhuman strength');
INSERT INTO F(r,b,f) VALUES (36,108,'Magnetism manipulation');
INSERT INTO F(r,b,f) VALUES (19,72,'X-Ray Vision');
INSERT INTO F(r,b,f) VALUES (13,39,'Eidetic memory');

INSERT INTO B(i,l) VALUES ('En','English');
INSERT INTO B(i,l) VALUES ('Kl','Klingon');
INSERT INTO B(i,l) VALUES ('02','Obfiscation');
INSERT INTO B(i,l) VALUES ('Ob','Obduron');
INSERT INTO B(i,l) VALUES ('Fr','French');

INSERT INTO L(c,h,u) VALUES ('Zorgorn','Mission on non-existant planet',NULL);
INSERT INTO L(c,h,u) VALUES ('Earth','Earth War',NULL);
INSERT INTO L(c,h,u) VALUES ('Daxam','Planet Kidnap',NULL);
INSERT INTO L(c,h,u) VALUES ('Apocalypse','Darkseid',NULL);

INSERT INTO R(r,q,p,c) VALUES (27,'Tasmia Mallor ','Shadow Lass ',19);
INSERT INTO R(r,q,p,c) VALUES (19,'Lar Gand ','Mon-El ',7);
INSERT INTO R(r,q,p,c) VALUES (4,'Luornu Durgo ','Triplicate Girl ',5);
INSERT INTO R(r,q,p,c) VALUES (24,'Andrew Nolan ','Ferro Lad ',10);
INSERT INTO R(r,q,p,c) VALUES (36,'Pol Krinn ','Magnetic Kid ',4);

/*CREATING INDEXES*/

CREATE UNIQUE INDEX IF NOT EXISTS d_idx ON D(c asc) WHERE c IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS s_idx ON S(r asc) WHERE r IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS f_idx ON F(b asc) WHERE b IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS b_idx ON B(i asc) WHERE i IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS l_idx ON L(h asc) WHERE h IS NOT NULL;
CREATE UNIQUE INDEX IF NOT EXISTS r_idx ON R(r asc) WHERE r IS NOT NULL;