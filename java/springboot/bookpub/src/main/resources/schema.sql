-- Create syntax for TABLE 'author'
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255)        DEFAULT NULL,
  `last_name`  VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
);
-- CREATE syntax FOR TABLE 'publisher'
DROP TABLE IF EXISTS `publisher`;
CREATE TABLE `publisher` (
  `id`   BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
);
-- CREATE syntax FOR TABLE 'reviewer'
DROP TABLE IF EXISTS `reviewer`;
CREATE TABLE `reviewer` (
  `id`         BIGINT(20) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255)        DEFAULT NULL,
  `last_name`  VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
);
-- CREATE syntax FOR TABLE 'book'
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id`           BIGINT(20) NOT NULL AUTO_INCREMENT,
  `description`  VARCHAR(255)        DEFAULT NULL,
  `isbn`         VARCHAR(255)        DEFAULT NULL,
  `title`        VARCHAR(255)        DEFAULT NULL,
  `author_id`    BIGINT(20)          DEFAULT NULL,
  `publisher_id` BIGINT(20)          DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_publisher` FOREIGN KEY (`publisher_id`) REFERENCES
    `publisher` (`id`),
  CONSTRAINT `FK_author` FOREIGN KEY (`author_id`) REFERENCES `author`
  (`id`)
);
-- CREATE syntax FOR TABLE 'book_reviewers'
DROP TABLE IF EXISTS `book_reviewers`;
CREATE TABLE `book_reviewers` (
  `book_id`      BIGINT(20) NOT NULL,
  `reviewers_id` BIGINT(20) NOT NULL,
  CONSTRAINT `FK_book` FOREIGN KEY (`book_id`) REFERENCES `book`
  (`id`),
  CONSTRAINT `FK_reviewer` FOREIGN KEY (`reviewers_id`) REFERENCES
    `reviewer` (`id`)
);