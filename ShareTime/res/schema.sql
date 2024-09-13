use ureca;

drop table if exists `book`;

create table `book`(
   `isbn`   varchar(20) primary key,
   `title`  varchar(60) not null,
   `author` varchar(30) not null,
   `price`  int not null,
   `desc`   varchar(3000) not null
);

drop table if exists `book`;

create table `book`(
   `isbn`   varchar(20) primary key,
   `title`  varchar(60) not null,
   `author` varchar(30) not null,
   `price`  int not null,
   `desc`   varchar(3000) not null,
   `originImg`    varchar(300),
   `saveImg`    varchar(300)
);


drop table if exists `member`;

create table `member`(
   `id`   varchar(20) primary key,
   `pwd`  varchar(20) not null,
   `name` varchar(50) not null
);

insert into member (id,pwd,name)
values 
('gildong','1234','나길동'),
('lime','2345','너라임'),
('juwon','3456','우주원');

commit;