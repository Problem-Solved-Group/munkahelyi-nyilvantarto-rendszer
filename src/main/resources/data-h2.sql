INSERT INTO site(name,location) VALUES ('HQ','1023,Budapest Tök utca 12.');
INSERT INTO site(name,location) VALUES ('Workplace 1','1023,Budapest Tök utca 26.');
INSERT INTO site(name,location) VALUES ('Workplace 2','1023,Budapest Ajtósi Dürer fasor 4.');

INSERT INTO user(username,password,email, role) VALUES ('admin','$2a$10$TENUJbNC03RkEuwd8.eki.M1XLW2oRFGI2hogHJXjJUwS9p7PB1N.', 'admin@admin.hu', 'ADMIN');

INSERT INTO holiday_request (requested_day, user_id, status) values ('2020-07-12','1', 'UNSEEN');
INSERT INTO holiday_request (requested_day, user_id, status) values ('2020-07-13','1', 'UNSEEN');
INSERT INTO holiday_request (requested_day, user_id, status) values ('2020-07-14','1', 'UNSEEN');