use dogsdb;

DROP TABLE IF EXISTS dog;

create TABLE dog (
dog_id bigint(20) not null AUTO_INCREMENT,
dog_nickname varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
dog_breed varchar(45) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
dog_age int NOT NULL,
PRIMARY KEY (dog_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
