create table account
(
    id         bigserial primary key,
    email      varchar(256) unique not null,
    password   varchar(32)         not null,
    nickname   varchar(20) unique  not null,
    age        integer,
    created_at timestamp           not null default current_timestamp,
    edited_at  timestamp           not null default current_timestamp
);

create table cutlink
(
    id       bigserial primary key,
    owner_id bigint            not null,
    cut      varchar(8) unique not null,
    link     varchar(2048)     not null,
    clicks   integer           not null default 0,
    added_at timestamp         not null default current_timestamp,
    foreign key (owner_id) references account (id)
);

create table multilink
(
    id       bigserial primary key,
    owner_id bigint        not null,
    link     varchar(2048) not null,
    clicks   integer       not null default 0,
    added_at timestamp     not null default current_timestamp,
    foreign key (owner_id) references account (id)
);

create table subscription
(
    who_id    bigint not null,
    sub_to_id bigint not null,
    foreign key (who_id) references account (id),
    foreign key (sub_to_id) references account (id),
    constraint sub unique (who_id, sub_to_id)
);
