CREATE TABLE profile (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE user_profile (
    user_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, profile_id),
    CONSTRAINT fk_userprofile_user FOREIGN KEY (user_id) REFERENCES `user`(id),
    CONSTRAINT fk_userprofile_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);