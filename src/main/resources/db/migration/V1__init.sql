drop table if exists state_history;
drop table if exists products;
drop table if exists notes;
drop table if exists deals;

create table deals
(
    uuid                     varchar(256) not null
        primary key,
    name                     text         not null,
    product                  varchar(128) null,
    telegram_link            text         null,
    created_at               timestamp             default CURRENT_TIMESTAMP not null,
    updated_at               timestamp    not null default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
    current_state            varchar(128) not null,
    last_message             text         null,
    author_username          varchar(256) null,
    telegram_chat_id         bigint       not null,
    last_message_received_at timestamp    not null,
    constraint uuid
        unique (uuid)
);

create index deals_product_index
    on deals (product);

create table notes
(
    uuid       varchar(256)                        not null
        primary key,
    text       text                                not null,
    deal_uuid  varchar(256)                        not null,
    created_at timestamp default CURRENT_TIMESTAMP not null,
    updated_at timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint uuid
        unique (uuid),
    constraint notes_ticket_uuid_fk
        foreign key (deal_uuid) references deals (uuid)
);

create table products
(
    name varchar(128) not null
        primary key
);

create table state_history
(
    uuid      varchar(256)                        not null
        primary key,
    state     varchar(128)                        not null,
    deal_uuid varchar(256)                        not null,
    to_date   timestamp default CURRENT_TIMESTAMP not null,
    from_date timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    constraint uuid
        unique (uuid),
    constraint state_aud_ticket_uuid_fk
        foreign key (deal_uuid) references deals (uuid)
);

