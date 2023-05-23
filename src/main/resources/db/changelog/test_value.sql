insert into users (email, first_name, lastname, image, password, phone, role)
values ('user@mail.ru', 'igor', 'igoreck', '/image/1', '$2a$12$p5PSTNhGEPAwMPPlYcrK7u6KSGNlqIha5Zrgs7ar9/0Qx5wkQq/0G'),
       '+79139792520', 'USER');
-- password 11111111

insert into users (email, first_name, lastname, image, password, phone, role)
values ('admin@mail.ru', 'oleg', 'olegock', '/image/2', '$2a$12$dwYq8m70KfYWYf4Z67Yj8OxRwjQ2TjdeBRtmVCW/a30uxznnr9fa'),
    '+79139792530', 'ADMIN');
-- password 55555555

insert into ads (description, image, price, title, users_id)
values ('крепкий кофе', '/image/2', 1000, 'Чашка кофе', 1);
insert into ads (description, image, price, title, users_id)
values ('свежий фрукт', '/image/3', 2000, 'Сладкий банан', 1);
insert into ads (description, image, price, title, users_id)
values ('коричневой зерно', '/image/4', 5000, 'Свежий желудь', 2);
insert into ads (description, image, price, title, users_id)
values ('Обыкновеный початок', '/image/5', 900, 'Свежая кукуруза', 2);

insert into comment (created_at, text, ads_id, users_id)
values ('2023-05-17 14:31:25.660095', 'хороший кофе', 1, 1);
insert into comment (created_at, text, ads_id, users_id)
values ('2023-05-17 14:33:25.661095', 'плохой кофе', 1, 1);
insert into comment (created_at, text, ads_id, users_id)
values ('2023-05-17 20:31:25.660095', 'хреновый кофе', 1, 2);
insert into comment (created_at, text, ads_id, users_id)
values ('2023-05-17 14:33:25.661095', 'классный желудь', 2, 1);

insert into photo (dtype, fale_path, file_size, media_type, users_id)
values ('Avatar', 'avatar\3c6a9fa9-c9df-45ac-ae65-bff1bfa146a2.jpg', 259009, 'image/jpeg',1);
insert into photo (dtype, fale_path, file_size, media_type, ads_id)
values ('Picture', 'picture\a915b911-be55-4545-b968-7a7d94e79a28.png', 136014, 'image/jpeg',1);
insert into photo (dtype, fale_path, file_size, media_type, ads_id)
values ('Picture', 'picture\a915b911-1111-4545-b968-7a7d94e79a28.png', 236014, 'image/jpeg',2);
insert into photo (dtype, fale_path, file_size, media_type, ads_id)
values ('Picture', 'picture\a915b911-be55-4545-5555-7a7d94e79a28.png', 156014, 'image/jpeg',3);
insert into photo (dtype, fale_path, file_size, media_type, ads_id)
values ('Picture', 'picture\a915b911-be55-8888-b968-7a7d94e79a28.png', 306014, 'image/jpeg',4);