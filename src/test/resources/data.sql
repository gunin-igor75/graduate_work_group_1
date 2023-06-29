-- Создание пользователей
insert into users (id, email, first_name, last_name, image, password, phone, role)
values (1, 'user@mail.ru', 'igor', 'igoreck', '/image/1',
        '$2a$12$p5PSTNhGEPAwMPPlYcrK7u6KSGNlqIha5Zrgs7ar9/0Qx5wkQq/0G',
        '+79139792520', 'USER');
-- password 11111111

insert into users (id, email, first_name, last_name, image, password, phone, role)
values (2, 'admin@mail.ru', 'oleg', 'olegock', '/image/2',
        '$2a$12$dwYq8m70KfYWYf4Z67Yj8OxRwjQ2TjdeBRtmVCW/a30uxznnr9fa',
        '+79139792530', 'ADMIN');
-- password 55555555
select setval('users_id_seq', (select max(id) from users));

-- Создание объявлений
insert into ads (id, description, image, price, title, users_id)
values (1, 'крепкий кофе', '/image/2', 1000, 'Чашка кофе', 1);

insert into ads (id, description, image, price, title, users_id)
values (2, 'свежий фрукт', '/image/3', 2000, 'Сладкий банан', 1);

insert into ads (id, description, image, price, title, users_id)
values (3, 'коричневой зерно', '/image/4', 5000, 'Свежий желудь', 2);

insert into ads (id, description, image, price, title, users_id)
values (4, 'Обыкновеный початок', '/image/5', 900, 'Свежая кукуруза', 2);

select setval('ads_id_seq', (select max(id) from ads));

-- Создание комментариев
insert into comment (id, created_at, text, ads_id, users_id)
values (1, '2023-05-17 14:31:25.660095', 'хороший кофе', 1, 1);

insert into comment (id, created_at, text, ads_id, users_id)
values (2, '2023-05-17 14:33:25.661095', 'плохой кофе', 1, 1);

insert into comment (id, created_at, text, ads_id, users_id)
values (3, '2023-05-17 20:31:25.660095', 'хреновый кофе', 1, 2);

insert into comment (id, created_at, text, ads_id, users_id)
values (4, '2023-05-18 14:33:25.661095', 'классный желудь', 2, 1);

select setval('comment_id_seq', (select max(id) from comment));

-- Создание картинок пользователей и объявлений
insert into photo (id, dtype, file_path, file_size, media_type, users_id)
values (1, 'Avatar', 'avatar\3c6a9fa9-c9df-45ac-ae65-bff1bfa146a2.jpg', 259009, 'image/jpeg', 1);

insert into photo (id, dtype, file_path, file_size, media_type, ads_id)
values (2, 'Picture', 'picture\a915b911-be55-4545-b968-7a7d94e79a28.png', 136014, 'image/jpeg', 1);

insert into photo (id, dtype, file_path, file_size, media_type, ads_id)
values (3, 'Picture', 'picture\a915b911-1111-4545-b968-7a7d94e79a28.png', 236014, 'image/jpeg', 2);

insert into photo (id, dtype, file_path, file_size, media_type, ads_id)
values (4, 'Picture', 'picture\a915b911-be55-4545-5555-7a7d94e79a28.png', 156014, 'image/jpeg', 3);

insert into photo (id, dtype, file_path, file_size, media_type, ads_id)
values (5, 'Picture', 'picture\a915b911-be55-8888-b968-7a7d94e79a28.png', 306014, 'image/jpeg', 4);

select setval('photo_id_seq', (select max(id) from photo));