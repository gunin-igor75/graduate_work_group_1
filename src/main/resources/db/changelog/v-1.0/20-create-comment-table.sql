
create table if not exists comment (

    id              integer generated by default as identity primary key,
    created_at      timestamp not null constraint  dateUnique unique,
    text            varchar(255) not null,
    ads_id          integer constraint commentAdsId references ads,
    users_id        integer constraint commentUsersId references users

);

GO