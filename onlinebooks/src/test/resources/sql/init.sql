delete from BOOKS_AUTHORS;
delete from BOOKS;
delete from AUTHORS;
delete from CATEGORIES;
delete from USERS;

insert into USERS(USERNAME, PASSWORD, EMAIL, AUTHORITY, ENABLED) 
values('UsernameD', '$2a$10$.PFOz5ZPjdJs2OtI49CV0.eQBKkaP76N7oAaEIKuF0Wxoc9.zs7ui', 'emailD@gmail.com', 'USER', true);

insert into USERS(USERNAME, PASSWORD, EMAIL, AUTHORITY, ENABLED) 
values('UsernameC', '$2a$10$L5jSDgTsDWeVOWHwT.CZCewXRXO3CBvov5NX0ENYhuM9b5EWKyZ.2', 'emailC@gmail.com', 'USER', true);

insert into USERS(USERNAME, PASSWORD, EMAIL, AUTHORITY, ENABLED)  
values('UsernameA', '$2a$10$cY59Z2/J55pJ3cq0Vo.qxOommLPQ3xVLVsOgsULC1tsTZiBYN5i0S', 'emailA@gmail.com', 'ADMIN', true);

insert into USERS(USERNAME, PASSWORD, EMAIL, AUTHORITY, ENABLED) 
values('UsernameB', '$2a$10$eaE45r4G8SeJDBarbrHDbuD91MM3tQzHK1BpoKj.XQawvxHh6UmQS', 'emailB@gmail.com', 'USER', true);

insert into CATEGORIES(ID, NAME) values(1, 'Category A');
insert into CATEGORIES(ID, NAME) values(2, 'Category B');
insert into CATEGORIES(ID, NAME) values(3, 'Category C');

insert into AUTHORS(ID, NAME, EMAIL, YEAR_OF_BIRTH) values(1, 'Author C', 'authorC@gmail.com', 1955);
insert into AUTHORS(ID, NAME, EMAIL, YEAR_OF_BIRTH) values(2, 'Author SEARCH A', 'authorA1@gmail.com', 1976);
insert into AUTHORS(ID, NAME, EMAIL, YEAR_OF_BIRTH) values(3, 'Author search B', 'authorB@gmail.com', 1989);
insert into AUTHORS(ID, NAME, EMAIL, YEAR_OF_BIRTH) values(4, 'Author A', 'authorA2@gmail.com', 1945);

insert into BOOKS(ISBN, TITLE, LANGUAGE, CATEGORY_ID, CONTENTS, DATE_OF_SAVING)
values('1234567890125', 'Title (search) A','ENGLISH', 1, 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\codeconventions.pdf'), '2018-07-03');

insert into BOOKS(ISBN, TITLE, LANGUAGE, CATEGORY_ID, CONTENTS, DATE_OF_SAVING)
values('1234567890121', 'Title B','SPANISH', 1, 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\codeconventions.pdf'), '2018-07-02');

insert into BOOKS(ISBN, TITLE, LANGUAGE, CATEGORY_ID, CONTENTS, DATE_OF_SAVING)
values('1234567890122', 'Title (Search) C','ENGLISH', 2, 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\codeconventions.pdf'), '2018-07-03');

insert into BOOKS(ISBN, TITLE, LANGUAGE, CATEGORY_ID, CONTENTS, DATE_OF_SAVING)
values('1234567890123', 'Title D','ENGLISH', 1, 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\codeconventions.pdf'), '2018-07-02');

insert into BOOKS(ISBN, TITLE, LANGUAGE, CATEGORY_ID, CONTENTS, DATE_OF_SAVING)
values('1234567890124', 'Title (SEARCH) E','SPANISH', 1, 
LOAD_FILE('C:\\ProgramData\\MySQL\\MySQL Server 5.7\\Uploads\\codeconventions.pdf'), '2018-07-03');

insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values('1234567890125', 1);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values('1234567890125', 2);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values('1234567890121', 2);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values('1234567890122', 2);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values('1234567890122', 3);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values('1234567890123', 4);
insert into BOOKS_AUTHORS(BOOK_ID, AUTHOR_ID) values('1234567890124', 4);


















