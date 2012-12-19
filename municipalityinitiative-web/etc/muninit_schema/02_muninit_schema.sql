create table municipality (
	id bigserial,
	name varchar(30),

	constraint municipality_pk primary key(id),
	constraint municipality_name_u unique(name)
);

create index municipality_id_index on municipality(id);

create table municipality_initiative (
	id bigserial,
	municipality_id bigserial,

    modified timestamp constraint municipality_initiative_modified_nn not null default now(),

    name varchar(512) constraint municipality_initiative_name_nn not null,
    proposal text,

    contact_name varchar(256),
    contact_email varchar(256),
    contact_phone varchar(30),
    contact_address varchar(256),

	constraint municipality_initiative_pk primary key (id),
	constraint municipality_initiative_municipality_fk foreign key (municipality_id) references municipality(id)
);

create index municipality_initiative_id_index on municipality_initiative(id);

create table composer (
	id bigserial,
	municipality_initiative_id bigserial,

	name varchar(256),

	municipality_id bigserial,
    show_name boolean constraint composer_public_name not null,
    is_municipality_citizen boolean,

    constraint composer_pk primary key (id),
    constraint composer_municipality_initiative_id foreign key (municipality_initiative_id) references municipality_initiative(id)
);

create index composer_id_index on composer(id);