SET
  FOREIGN_KEY_CHECKS=0;

CREATE DATABASE IF NOT EXISTS `uni_bbs` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `uni_bbs`;

DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS
  `user` (
    `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(32),
    `account` VARCHAR(64) UNIQUE,
    `password` VARCHAR(64),
    `state` TINYINT
  );

DROP TABLE IF EXISTS `action`;

CREATE TABLE IF NOT EXISTS
  `action` (
    `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `user_id` INT UNSIGNED NOT NULL,
    `target_id` INT UNSIGNED,
    `type` TINYINT NOT NULL,
    `content_id` INT UNSIGNED,
    `time` DATETIME NOT NULL,
    `visibility_id` INT UNSIGNED
  );

DROP TABLE IF EXISTS `comment`;

CREATE TABLE IF NOT EXISTS
  `comment` (
    `id` INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `content` TEXT,
    `likes_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `comments_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `collections_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `pull_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `state` TINYINT,
    `last_comment_id` INT UNSIGNED
  );