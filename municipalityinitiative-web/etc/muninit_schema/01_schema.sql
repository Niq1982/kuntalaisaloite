drop type if exists initiativeType;
drop type if exists initiativeState;

create type initiativeType as enum ('UNDEFINED', 'SINGLE','COLLABORATIVE','COLLABORATIVE_COUNCIL','COLLABORATIVE_CITIZEN');
create type initiativeState as enum('DRAFT','REVIEW','ACCEPTED', 'PUBLISHED');
create type membershipType as enum('community','company','property','none');
create type fixState as enum ('FIX', 'REVIEW', 'OK');

create table municipality (
	id bigserial,
	name varchar(30) constraint municipality_name_nn not null,
	name_sv varchar(30) constraint municipality_name_sv_nn not null,
	email varchar(100),
	active boolean default false,

	constraint municipality_pk primary key(id),
	constraint municipality_name_u unique(name),
	constraint municipality_name_sv_u unique(name)
);
create index municipality_id_index on municipality(id);

create table municipality_initiative (
	id bigserial,
	municipality_id bigserial,
	type initiativeType constraint initiative_type_nn not null,
	state initiativeState constraint initiative_state_nn not null default 'DRAFT',
	fix_state fixState constraint initiative_fix_state_nn not null default 'OK',
	state_timestamp timestamp constraint initiative_state_timestamp_nn not null default now(),

    name varchar(512),
    proposal text,
    extra_info varchar(1024),

    modified timestamp constraint municipality_initiative_modified_nn not null default now(),

    moderator_comment varchar(1024),
    participant_count integer default 0,

    sent timestamp,
    sent_comment varchar(1024),

	constraint municipality_initiative_pk primary key (id),
	constraint municipality_initiative_municipality_fk foreign key (municipality_id) references municipality(id)

);

create index municipality_initiative_id_index on municipality_initiative(id);

create table participant (
	id bigserial,
	municipality_initiative_id bigserial,

	name varchar(100),

	participate_time date default now(),

	municipality_id bigserial,
    show_name boolean,

    email varchar(100),
    confirmation_code varchar(20), -- Being set as null after confirmation

    membership_type membershipType constraint participant_membership_type_nn not null default 'none',

    constraint participant_pk primary key (id),
    constraint participant_municipality_initiative_id_fk foreign key (municipality_initiative_id) references municipality_initiative(id),
    constraint participant_municipality_fk foreign key (municipality_id) references municipality(id)
);
create index participant_id_index on participant(id);


create table author (
    id bigserial,
    participant_id bigserial,
    management_hash varchar(40), -- TODO unique

    phone varchar(30),
    address varchar(256),

    constraint author_pk primary key (id),
    constraint author_participant_fk foreign key (participant_id) references participant(id)
);

create index author_management_hash_index on author(management_hash);

create table author_invitation (
    id bigserial,
    initiative_id bigserial,
    confirmation_code varchar(20),
    email varchar(100) constraint author_invitation_email_nn not null,
    name varchar(100) constraint author_invitation_name_nn not null,
    invitation_time timestamp constraint author_invitation_time_nn not null,
    reject_time timestamp,

    constraint author_invitation_pk primary key(id),
    constraint author_invitation_initiative_id_fk foreign key (initiative_id) references municipality_initiative(id)
);

