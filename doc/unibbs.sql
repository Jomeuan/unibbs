SET
  FOREIGN_KEY_CHECKS=0;

CREATE DATABASE IF NOT EXISTS `uni_bbs` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE `uni_bbs`;

-- 用户
DROP TABLE IF EXISTS `user`;

CREATE TABLE IF NOT EXISTS
  `user` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `account` VARCHAR(64) UNIQUE,
    `password` VARCHAR(64),
    `state` TINYINT,
    `exipration` DATETIME
  );

-- 角色
DROP TABLE IF EXISTS `role`;

CREATE TABLE IF NOT EXISTS
  `role` (
    `id` TINYINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(16)
  );

INSERT INTO
  `role` (`id`, `name`)
VALUES
  (1, "ADMIN"),
  (2, "VISITOR"),
  (3, "MODERATOR");

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE IF NOT EXISTS
  `user_role` (
    `user_id` BIGINT UNSIGNED NOT NULL,
    `role_id` TINYINT UNSIGNED NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`)
  );

-- profile
DROP TABLE IF EXISTS `profile`;

CREATE TABLE
  `profile` (
    `id` BIGINT UNSIGNED PRIMARY KEY,
    `nickname` VARCHAR(32),
    `avatar` VARCHAR(255),
    `phone` VARCHAR(32),
    `email` VARCHAR(64),
    `join_date` DATETIME,
    `last_login_time` DATETIME,
    `birthday` DATE,
    `gender` TINYINT,
    `country` VARCHAR(255),
    `address1` VARCHAR(32),
    `address2` VARCHAR(32),
    `address3` VARCHAR(32),
    `detail_address` VARCHAR(128)
  );

-- forum
DROP TABLE IF EXISTS `action`;

CREATE TABLE IF NOT EXISTS
  `action` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `target_id` BIGINT UNSIGNED,
    `type` TINYINT NOT NULL,
    `content_id` BIGINT UNSIGNED,
    `time` DATETIME NOT NULL DEFAULT NOW()
  );

DROP TABLE IF EXISTS `comment`;

CREATE TABLE IF NOT EXISTS
  `comment` (
    `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `content` TEXT,
    `likes_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `comments_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `collections_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `pull_count` INT UNSIGNED NOT NULL DEFAULT 0,
    `state` TINYINT
  );

DROP TABLE IF EXISTS `moderator`;

CREATE TABLE IF NOT EXISTS
  `moderator` (
    `id` BIGINT UNSIGNED PRIMARY KEY,
    `community_id` BIGINT UNSIGNED NOT NULL,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `title` VARCHAR(8)
  );

DROP TABLE IF EXISTS `community_content`;

CREATE TABLE IF NOT EXISTS
  `community_content` (
    `id` BIGINT UNSIGNED PRIMARY KEY,
    `title` VARCHAR(32),
    `avatar` VARCHAR(255),
    `introduction` VARCHAR(255),
    `rule` VARCHAR(255)
  );

DROP TABLE IF EXISTS `thread`;

CREATE TABLE IF NOT EXISTS
  `thread` (
    `id` BIGINT UNSIGNED PRIMARY KEY,
    `creator_id` BIGINT UNSIGNED,
    `title` VARCHAR(32),
    `introduction` VARCHAR(255),
    `avatar` VARCHAR(255)
  );