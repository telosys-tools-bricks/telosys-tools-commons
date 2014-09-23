drop table if exists EMPLOYEE ;

create table if not exists EMPLOYEE  (
   ID          INTEGER NOT NULL      comment 'The unique id',
   FIRST_NAME  CHAR(25)              comment 'The first name',
   LAST_NAME   VARCHAR(25) NOT NULL,
   BIRTH_DATE  DATE,
  PRIMARY KEY (ID)
);

insert into EMPLOYEE (ID, FIRST_NAME, LAST_NAME ) values ( 1, 'John', 'Wayne' );
insert into EMPLOYEE (ID, FIRST_NAME, LAST_NAME ) values ( 2, 'Phil', 'Collins' );

