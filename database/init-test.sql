SET character_set_client = utf8mb4;

DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `roles`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `password_reset_tokens`;
DROP TABLE IF EXISTS `user_verification_tokens`;

CREATE TABLE `users`
(
    `id`                        BIGINT       NOT NULL AUTO_INCREMENT,
    `first_name`                VARCHAR(50)  NOT NULL,
    `last_name`                 VARCHAR(50)  NOT NULL,
    `email`                     VARCHAR(100) NOT NULL,
    `password`                  VARCHAR(100) NOT NULL,
    `birthday_date`             DATE         NOT NULL,
    `last_access_date`          DATE    DEFAULT NULL,
    `last_password_update_date` DATE    DEFAULT NULL,
    `enabled`                   BOOLEAN DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO users (first_name, last_name, email, password, birthday_date, enabled)
VALUES ('User', 'One', 'user@one.com', 'testing', sysdate(), TRUE);
INSERT INTO users (first_name, last_name, email, password, birthday_date, enabled, last_access_date, last_password_update_date)
VALUES ('User', 'Two', 'user@two.com', 'testing', sysdate(), FALSE, '2008-01-01', '2008-01-01');
INSERT INTO users (first_name, last_name, email, password, birthday_date, enabled, last_access_date, last_password_update_date)
VALUES ('User', 'Three', 'user@three.com', 'testing', sysdate(), FALSE, sysdate(), sysdate());

CREATE TABLE `roles`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO roles (name)
VALUES ('Role_One');
INSERT INTO roles (name)
VALUES ('Role_Two');
INSERT INTO roles (name)
VALUES ('Role_Three');

CREATE TABLE `users_roles`
(
    `id_user` BIGINT NOT NULL,
    `id_role` BIGINT NOT NULL,
    PRIMARY KEY (`id_role`, `id_user`),
    CONSTRAINT `FK_ROLE` FOREIGN KEY (`id_role`) REFERENCES `roles` (`id`),
    CONSTRAINT `FK_USER` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO users_roles (id_user, id_role)
VALUES (1, 1);
INSERT INTO users_roles (id_user, id_role)
VALUES (2, 2);
INSERT INTO users_roles (id_user, id_role)
VALUES (3, 3);

CREATE TABLE `password_reset_tokens`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `token`         VARCHAR(50) NOT NULL,
    `creation_date` DATE        NOT NULL,
    `expiry_date`   DATE        NOT NULL,
    `id_user`       BIGINT      NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_USER` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `user_verification_tokens`
(
    `id`            BIGINT      NOT NULL AUTO_INCREMENT,
    `token`         VARCHAR(50) NOT NULL,
    `creation_date` DATE        NOT NULL,
    `expiry_date`   DATE        NOT NULL,
    `id_user`       BIGINT      NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `FK_USER` FOREIGN KEY (`id_user`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;