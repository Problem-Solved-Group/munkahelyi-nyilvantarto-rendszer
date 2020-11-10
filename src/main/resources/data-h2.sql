INSERT INTO user(name,username,password,email, role) VALUES ('Admin', 'admin','$2a$10$TENUJbNC03RkEuwd8.eki.M1XLW2oRFGI2hogHJXjJUwS9p7PB1N.', 'admin@admin.hu', 'ADMIN');
-- admin : admin
INSERT INTO user(name,username,password,email, role) VALUES ('Worker','worker','$2a$10$bUxp2G.lgDOE3jxA9sCOjuwbaMgwXCXQleAIrqmz.nqBW9DNYxJwO', 'worker@worker.hu', 'WORKER');
-- worker : worker
INSERT INTO user(name,username,password,email, role) VALUES ('Leader','leader','$2a$10$nAe0KiTtNTpHKBcYrobrN.yr/NHRorb5mwxw9lAbUUqTAfLNNEBcC', 'leader@leader.hu', 'LEADER');
--leader : leader

INSERT INTO site(name,location) VALUES ('HQ','1023,Budapest Tök utca 12.');
INSERT INTO site(name,location) VALUES ('Telephely','1023,Budapest Tök utca 26.');
INSERT INTO site(name,location) VALUES ('Raktár','1023,Budapest Ajtósi Dürer fasor 4.');
INSERT INTO site(name,location) VALUES ('Gyár','1023,Egérút 20.');

INSERT INTO holiday_request (requested_day, user_id, status) VALUES ('2020-07-12','1', 'UNSEEN');
INSERT INTO holiday_request (requested_day, user_id, status) VALUES ('2020-07-13','1', 'UNSEEN');
INSERT INTO holiday_request (requested_day, user_id, status) VALUES ('2020-07-14','1', 'UNSEEN');
INSERT INTO holiday_request (requested_day, user_id, status) VALUES ('2020-07-14','2', 'UNSEEN');
INSERT INTO holiday_request (requested_day, user_id, status) VALUES ('2020-07-19','2', 'UNSEEN');
INSERT INTO holiday_request (requested_day, user_id, status) VALUES ('2020-07-14','3', 'UNSEEN');

INSERT INTO user_sites (users_id, sites_id) VALUES (1,1);
INSERT INTO user_sites (users_id, sites_id) VALUES (2,1);
INSERT INTO user_sites (users_id, sites_id) VALUES (2,4);
INSERT INTO user_sites (users_id, sites_id) VALUES (3,2);


INSERT INTO announcement (title, message, show_until, user_id) VALUES ('Hirdetmeny 1', 'Home office', '2020-12-31 23:59:59', 1);
INSERT INTO announcement (title, message, show_until, user_id) VALUES ('Hirdetmeny 2', 'Kötelező maszk', '2020-12-31 23:59:59', 3);
INSERT INTO announcement (title, message, show_until, user_id) VALUES ('Hirdetmeny 2', '1,5 méter távolság', '2020-12-31 23:59:59', 3);

INSERT INTO message (title, message, receiver_id, sender_id) VALUES ('Üzenet 1', 'Ez egy rendszer által generált tesztüzenet', 3,1);
INSERT INTO message (title, message, receiver_id, sender_id) VALUES ('Válasz: Üzenet 1', 'Köszönöm', 1,3);

INSERT INTO working_time (start, end, validated, user_id) VALUES ('2020-07-13 8:00:00', '2020-07-13 17:50:00', false, 2);
INSERT INTO working_time (start, end, validated, user_id) VALUES ('2020-07-18 8:00:00', '2020-07-19 17:50:00', false, 2);