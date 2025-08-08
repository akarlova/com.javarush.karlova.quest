CREATE TABLE IF NOT EXISTS teams
(
    id          INT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(30)  NOT NULL,
    avatar_path VARCHAR(255) NOT NULL
);
INSERT INTO teams (id, name, avatar_path)
SELECT * FROM (
    SELECT 1, 'PuffinClaw', 'Puffin_Ravenclaw_Icon.png' UNION ALL
    SELECT 2, 'DeerPuff',   'Deer_HufflePuff_Icon.png'  UNION ALL
    SELECT 3, 'KittenDor',  'Gryffindor_Kitty_Icon.png' UNION ALL
    SELECT 4, 'RaptorRin',  'Slytherin_Raptor_Icon.png'
) AS v(id, name, avatar_path)
WHERE NOT EXISTS (SELECT 1 FROM teams);

CREATE TABLE IF NOT EXISTS users
(
    id       BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(30) NOT NULL UNIQUE,
    role     VARCHAR(10) NOT NULL,
    team_id  INT         NOT NULL,
    FOREIGN KEY (team_id) REFERENCES teams (id)
);

CREATE TABLE IF NOT EXISTS results
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id          BIGINT NOT NULL,
    test_id          BIGINT NOT NULL,
    score            INT    NOT NULL,
    duration_seconds INT    NOT NULL,
    taken_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);
