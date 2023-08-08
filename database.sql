CREATE DATABASE connect4 CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs;

USE connect4;

CREATE TABLE user(
    login VARCHAR(255) NOT NULL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    registration_date DATE NOT NULL,
    last_connection TIMESTAMP NOT NULL,
    is_online BOOLEAN NOT NULL,
    is_in_game BOOLEAN NOT NULL
);

CREATE TABLE user_user(
    user1_login VARCHAR(255) NOT NULL,
    user2_login VARCHAR(255) NOT NULL,
    confirmed BOOLEAN DEFAULT false NOT NULL,
    FOREIGN KEY (user1_login) REFERENCES user (login) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (user2_login) REFERENCES user (login) ON DELETE CASCADE ON UPDATE CASCADE,
    PRIMARY KEY (user1_login, user2_login)
);

CREATE TABLE score(
    user VARCHAR(255) NOT NULL PRIMARY KEY,
    wins INT NOT NULL,
    defeats INT NOT NULL,
    draws INT NOT NULL,
    winStreak INT NOT NULL,
    bestWinStreak INT NOT NULL,
    FOREIGN KEY (user) REFERENCES user (login) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) 
VALUES ('Ryu', 'ryu@gmail.com', 'test', CURDATE(), NOW(), false, false);

INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) 
VALUES ('Ken', 'ken@gmail.com', 'test', CURDATE(), NOW(), false, false);

INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) 
VALUES ('Chun-Li', 'chun-li@gmail.com', 'test', CURDATE(), NOW(), false, false);

INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) 
VALUES ('Guile', 'guile@gmail.com', 'test', CURDATE(), NOW(), false, false);

INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) 
VALUES ('Cammy', 'cammy@gmail.com', 'test', CURDATE(), NOW(), false, false);

INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) 
VALUES ('Dhaslim', 'dhaslim@gmail.com', 'test', CURDATE(), NOW(), false, false);

INSERT INTO user (login, email, password, registration_date, last_connection, is_online, is_in_game) 
VALUES ('Honda', 'honda@gmail.com', 'test', CURDATE(), NOW(), false, false);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ryu', 'Honda', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ryu', 'Ken', false);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ryu', 'Chun-Li', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ryu', 'Dhaslim', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ryu', 'Guile', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ryu', 'Cammy', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ken', 'Honda', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Ken', 'Guile', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Honda', 'Chun-Li', false);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Chun-Li', 'Cammy', true);

INSERT INTO user_user (user1_login, user2_login, confirmed) 
VALUES ('Cammy', 'Guile', true);

INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES ('Ryu', 15, 1, 0, 0, 15);

INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES ('Ken', 1, 8, 0, 0, 1);

INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES ('Chun-Li', 5, 2, 1, 0, 4);

INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES ('Guile', 0, 1, 2, 0, 0);

INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES ('Cammy', 1, 4, 3, 0, 1);

INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES ('Dhaslim', 0, 2, 0, 0, 0);

INSERT INTO score (user, wins, defeats, draws, winStreak, bestWinStreak) VALUES ('Honda', 0, 6, 1, 0, 0);