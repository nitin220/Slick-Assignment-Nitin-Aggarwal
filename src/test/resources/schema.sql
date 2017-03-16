CREATE TABLE employee(id integer PRIMARY KEY ,name character varying,experience double precision );

CREATE TABLE project(projId integer PRIMARY KEY ,name character varying,empId integer references employee(id),teamSize integer , teamLead character varying);

CREATE TABLE dependent(dependentid integer PRIMARY KEY auto_increment,empId integer references employee(id),name character varying,relation character varying,age integer null );
