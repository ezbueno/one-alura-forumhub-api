ALTER TABLE course
ADD CONSTRAINT fk_course_user
FOREIGN KEY (user_id) REFERENCES users(id);