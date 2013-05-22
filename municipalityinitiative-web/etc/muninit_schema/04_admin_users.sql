create table admin_user (
	username varchar(30),
	password char(40) constraint admin_user_password_nn not null,
	name varchar(40) constraint admin_user_name_nn not null,
	last_online timestamp,

	constraint admin_user_pk primary key(name)
);
create index admin_user_name_index on admin_user(name);

insert into admin_user (username, password, name) values('admin', '034eec6d78bb9d9cfaa36ab4cb2131110cd23c43', 'Oili Oikkonen');