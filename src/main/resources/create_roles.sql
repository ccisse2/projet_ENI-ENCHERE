
--DROP TABLE [ROLES];

--Crée la table ROLES
CREATE TABLE roles (
                       role NVARCHAR(50) NOT NULL,
                       is_admin BIT NOT NULL,
                       PRIMARY KEY (role, is_admin)
);

INSERT INTO roles (role, is_admin) VALUES ('ROLE_USER', 0);
INSERT INTO roles (role, is_admin) VALUES ('ROLE_ADMIN', 1);
