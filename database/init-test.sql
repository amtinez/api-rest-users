SET character_set_client = utf8mb4;

DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `password_reset_tokens`;
DROP TABLE IF EXISTS `user_verification_tokens`;

CREATE TABLE `users`
(
    `id`                        BIGINT          NOT NULL        AUTO_INCREMENT,
    `first_name`                VARCHAR(50)     NOT NULL,
    `last_name`                 VARCHAR(50)     NOT NULL,
    `email`                     VARCHAR(100)    NOT NULL,
    `password`                  VARCHAR(100)    NOT NULL,
    `birthday_date`             DATE            NOT NULL,
    `last_access_date`          TIMESTAMP       DEFAULT NULL,
    `last_password_update_date` TIMESTAMP       DEFAULT NULL,
    `enabled`                   BOOLEAN         DEFAULT NULL,
    `locked`                    BOOLEAN         DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_USER_EMAIL` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `roles`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_ROLE_NAME` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `users_roles`
(
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `FK_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `password_reset_tokens`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `code`          VARCHAR(50) NOT NULL,
    `creation_date` TIMESTAMP   NOT NULL,
    `expiry_date`   TIMESTAMP   NOT NULL,
    `user_id`       BIGINT      NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_PASSWORD_RESET_TOKEN_CODE` (`code`),
    UNIQUE KEY `UK_PASSWORD_RESET_TOKEN_USER` (`user_id`),
    CONSTRAINT `FK_PASSWORD_RESET_TOKEN_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `user_verification_tokens`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `code`          VARCHAR(50) NOT NULL,
    `creation_date` TIMESTAMP   NOT NULL,
    `expiry_date`   TIMESTAMP   NOT NULL,
    `user_id`       BIGINT      NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_USER_VERIFICATION_TOKEN_CODE` (`code`),
    UNIQUE KEY `UK_USER_VERIFICATION_TOKEN_USER` (`user_id`),
    CONSTRAINT `FK_USER_VERIFICATION_TOKEN_USER` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
