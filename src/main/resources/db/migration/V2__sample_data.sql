insert into products
values ('GAD');
insert into products
values ('Exchange');


insert into deals (uuid, name, product, telegram_link, created_at, updated_at, current_state, last_message,
                   author_username, telegram_chat_id, last_message_received_at)
values ('f596c437-f529-4128-8707-4553ef80c2c0', 'test', 'GAD', 'http://tlg.me', '2022-12-03 01:26:45',
        '2022-12-03 00:26:58', 'QUALIFICATION', 'Hola my friend, i want some drugs', 'testerss', 0,
        '2022-12-03 09:06:34'),
       ('TCT-a2b2bdb4-da15-4ee5-a2e9-12dea313a368', 'testGroup', null, 'https://t.me/+OQQy8P3NltYyYTUy',
        '2022-12-03 21:37:10', '2022-12-03 21:37:10', 'QUALIFICATION', 'Message: asdas sent by Murmur_me

', 'Murmur_me', -845979502, '2022-12-03 21:37:10');


insert into state_history (uuid, state, deal_uuid, to_date, from_date)
values ('d94b1aef-baa9-4455-8bd4-7e586a1fe796', 'QUALIFICATION', 'TCT-a2b2bdb4-da15-4ee5-a2e9-12dea313a368',
        '2022-12-04 00:54:02', '2022-12-03 21:37:10'),
       ('dae42da9-0ca2-46bc-b53c-f806ca01862b', 'KYC', 'TCT-a2b2bdb4-da15-4ee5-a2e9-12dea313a368',
        '2022-12-04 00:54:32', '2022-12-04 00:54:02');

insert into notes (uuid, text, deal_uuid, created_at, updated_at)
values ('f596c437-f529-4128-8707-4553ef80c2c2', 'Ima test Note', 'f596c437-f529-4128-8707-4553ef80c2c0',
        '2022-12-03 00:28:14', '2022-12-03 00:28:14');