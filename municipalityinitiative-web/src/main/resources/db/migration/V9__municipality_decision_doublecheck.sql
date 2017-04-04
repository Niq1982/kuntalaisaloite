alter table municipality_user add column login_hash varchar(40);
alter table municipality_user add column login_hash_create_time timestamp;