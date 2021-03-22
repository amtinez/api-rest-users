SET character_set_client = utf8mb4;

CREATE TABLE IF NOT EXISTS `users`
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

INSERT INTO users (first_name, last_name, email, password, birthday_date, last_access_date, last_password_update_date, enabled)
VALUES ('Admin', 'Admin', 'admin@admin.com', '$2a$10$9FLFNGN/dql1T7eyeiSE8e9RuUYQMb9dqj0SL82BTyHkaX0nCXuPC', sysdate(), sysdate(), sysdate(), TRUE);

CREATE TABLE IF NOT EXISTS `roles`
(
    `id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO roles (name)
VALUES ('admin');
INSERT INTO roles (name)
VALUES ('user');

CREATE TABLE IF NOT EXISTS `users_roles`
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

CREATE TABLE IF NOT EXISTS `password_reset_tokens`
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

CREATE TABLE IF NOT EXISTS `user_verification_tokens`
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