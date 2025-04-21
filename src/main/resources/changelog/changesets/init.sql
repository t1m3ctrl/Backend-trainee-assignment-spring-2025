CREATE SCHEMA IF NOT EXISTS pvz;

CREATE TABLE pvz.users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email TEXT NOT NULL UNIQUE,
    role TEXT NOT NULL CHECK (role IN ('ROLE_CLIENT', 'ROLE_MODERATOR')),
    password TEXT NOT NULL
);

INSERT INTO pvz.users(email, role, password) VALUES
('client@mail.com', 'ROLE_CLIENT', '$2a$10$Kpa9e0ta1rI7OorabFiGrOB9N1Iii9xLnuc6hZZj1EgdWeetmwoni'),
('moderator@mail.com', 'ROLE_MODERATOR', '$2a$10$Kpa9e0ta1rI7OorabFiGrOB9N1Iii9xLnuc6hZZj1EgdWeetmwoni');