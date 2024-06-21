DROP DATABASE IF EXISTS 'e_invoice_db';
CREATE DATABASE IF NOT EXISTS 'e_invoice_db';
USE 'e_invoice_db';

SET NAMES utf8mb4;


-- t_user table create
DROP TABLE IF EXISTS 't_user';
CREATE TABLE 't_user' (
                        user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        email VARCHAR(255) NOT NULL,
                        firstname VARCHAR(255),
                        lastname VARCHAR(255),
                        username VARCHAR(255),
                        password VARCHAR(255) NOT NULL,
                        update_time DATETIME,
                        create_time DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- t_file table create
DROP TABLE IF EXISTS 't_file';
CREATE TABLE 't_file' (
                        file_id INT PRIMARY KEY AUTO_INCREMENT,
                        file_name VARCHAR(255) NOT NULL,
                        user_id BIGINT NOT NULL,
                        file_content LONGTEXT,
                        file_validation TINYINT(1),
                        FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);

-- t_login_fail create
DROP TABLE IF EXISTS 't_login_fail';
CREATE TABLE 't_login_fail' (
                              login_fail_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              user_id BIGINT,
                              user_type INT,
                              login_name VARCHAR(255),
                              lock_flag TINYINT(1),
                              login_fail_count INT,
                              login_lock_begin_time DATETIME,
                              update_time DATETIME,
                              create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (user_id) REFERENCES t_user(user_id)
);
