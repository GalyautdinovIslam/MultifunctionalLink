create table account
(
    id         bigserial primary key,
    email      varchar(256) unique not null,
    password   varchar(256)        not null,
    nickname   varchar(20) unique  not null,
    age        integer,
    created_at timestamp           not null default current_timestamp,
    activated  boolean             not null default false
);

create table cutlink
(
    id       bigserial primary key,
    owner_id bigint            not null,
    cut      varchar(8) unique not null,
    link     text              not null,
    clicks   integer           not null default 0,
    added_at timestamp         not null default current_timestamp,
    foreign key (owner_id) references account (id),
    check ( length(link) <= 2048 )
);

create table multilink
(
    id       bigserial primary key,
    owner_id bigint    not null,
    link     text      not null,
    clicks   integer   not null default 0,
    added_at timestamp not null default current_timestamp,
    foreign key (owner_id) references account (id),
    check ( length(link) <= 2048 )
);

create table subscription
(
    who_id    bigint not null,
    sub_to_id bigint not null,
    foreign key (who_id) references account (id),
    foreign key (sub_to_id) references account (id),
    constraint sub unique (who_id, sub_to_id)
);

create table recovery
(
    account_id    bigint              not null,
    recovery_code varchar(256) unique not null,
    foreign key (account_id) references account (id)
);

create table sign_up
(
    account_id bigint              not null,
    code       varchar(256) unique not null,
    foreign key (account_id) references account (id)
);
