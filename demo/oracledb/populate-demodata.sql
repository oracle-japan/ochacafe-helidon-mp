-- alter session set container PDB1

WHENEVER SQLERROR CONTINUE NONE;

CREATE USER DEMO IDENTIFIED BY "OCHaCafe6666" DEFAULT TABLESPACE users TEMPORARY TABLESPACE temp;

GRANT RESOURCE, CONNECT TO DEMO;

GRANT UNLIMITED TABLESPACE TO DEMO;

DROP TABLE DEMO.GREETING;
CREATE TABLE DEMO.GREETING (
    SALUTATION VARCHAR(64) NOT NULL PRIMARY KEY,
    RESPONSE VARCHAR(64) NOT NULL
);

INSERT INTO DEMO.GREETING (SALUTATION, RESPONSE) VALUES ('Marco', 'Polo');

DROP TABLE DEMO.COUNTRY;
CREATE TABLE DEMO.COUNTRY (
    COUNTRY_ID NUMBER NOT NULL PRIMARY KEY,
    COUNTRY_NAME VARCHAR(64) NOT NULL
);

INSERT INTO DEMO.COUNTRY (COUNTRY_ID, COUNTRY_NAME) VALUES ('1', 'USA');
INSERT INTO DEMO.COUNTRY (COUNTRY_ID, COUNTRY_NAME) VALUES ('81', 'Japan');
/

SELECT * FROM DEMO.GREETING;
SELECT * FROM DEMO.COUNTRY;
/

-- create procesure
GRANT EXECUTE ON DBMS_LOCK TO DEMO;
/

CREATE OR REPLACE PROCEDURE DEMO.INSERT_COUNTRY (
    ID IN NUMBER,
    NAME IN VARCHAR,
    DELAY IN NUMBER DEFAULT 30
  )
AS 
BEGIN
    INSERT INTO DEMO.COUNTRY (COUNTRY_ID, COUNTRY_NAME)
    VALUES (ID, NAME);
    DBMS_LOCK.SLEEP(DELAY);
END;
/

CREATE OR REPLACE PROCEDURE DEMO.UPDATE_COUNTRY (
    ID IN NUMBER,
    NAME IN VARCHAR,
    DELAY IN NUMBER DEFAULT 30
  )
AS 
BEGIN
    UPDATE DEMO.COUNTRY 
    SET COUNTRY_NAME = NAME
    WHERE COUNTRY_ID = ID;
    DBMS_LOCK.SLEEP(DELAY);
END;
/

EXIT;